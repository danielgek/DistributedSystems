import Util.Debug;
import rmi.StorageServer;
import rmi.StorageServerInterface;

import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by danielgek on 07/10/15.
 */
public class TCPServer {

    private String naming = "//127.0.0.1:25055/storageServer";
    private StorageServerInterface storageServer;

    private int tcpServerPort = 6000;
    ServerSocket listenSocket;

    public TCPServer() {
        try {
            setupRMI();
        } catch (InterruptedException e) {
            Debug.m("Error starting RMI connection \n" + e.getMessage());
        }
        loop();
    }

    public void setupRMI() throws InterruptedException {
        try {
            storageServer = (StorageServerInterface) Naming.lookup(naming);
            Debug.m("Connected to RMI");
        } catch (NotBoundException e) {
            Debug.m(e.getMessage());
            //wait(1);
            try {
                Thread.sleep(1000);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            Debug.m("Trying to reconnect to RMI server!!");
            setupRMI();
        } catch (MalformedURLException e) {
            Debug.m(e.getMessage());
            //wait(1);
            try {
                Thread.sleep(1000);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            Debug.m("Trying to reconnect to RMI server!!");
            setupRMI();
        } catch (RemoteException e) {
            Debug.m(e.getMessage());
            //wait(1);
            try {
                Thread.sleep(1000);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            Debug.m("Trying to reconnect to RMI server!!");
            setupRMI();
        }
    }

    private void loop(){
        try{
            Debug.m("Listen on 6000");
            listenSocket = new ServerSocket(tcpServerPort);
            while(true) {

                Socket clientSocket = listenSocket.accept();
                Debug.m("New Client!!");
                new ClientHandler(clientSocket,storageServer, this);
            }
        }catch(Exception e) {
            Debug.m("Error listening clients on TCPServer: \n" + e.getMessage());
        }
    }

    public static void main(String args[]) {
        Debug.m("Starting TCP Server!");
        new TCPServer();
    }
}
