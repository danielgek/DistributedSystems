
import Util.Debug;
import models.Response;
import models.User;
import rmi.StorageServerInterface;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

/**
 * Created by danielgek on 07/10/15.
 */
public class ClientHandler extends Thread {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private TCPServer tcpServer;
    private StorageServerInterface storageServer;


    public ClientHandler(Socket socket, StorageServerInterface storageServer, TCPServer tcpServer) {
        this.socket = socket;
        this.tcpServer = tcpServer;
        this.storageServer = storageServer;
        try{
            this.out = new ObjectOutputStream(this.socket.getOutputStream());
            this.in = new ObjectInputStream(this.socket.getInputStream());
        }catch (IOException e){
            Debug.m("Error on Client Contructor:" + e);
        }

        this.start();
    }


    public void run() {

        try{
            while(true){
                Action action = (Action) in.readObject();
                Response response = null;
                switch (action.getAction()){
                    case Action.SIGUP:
                        response = this.storageServer.register((User) action.getObject());
                        break;
                    case Action.LOGIN:
                        response = this.storageServer.login((User) action.getObject());
                        break;
                }
                out.writeObject(response);

            }
        }catch(EOFException e){
            Debug.m("Error on Client run method EOF:" + e.getMessage());
            try {
                tcpServer.setupRMI();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }catch(IOException e){
            Debug.m("Error on Client run method IO:" + e.getMessage());
            try {
                tcpServer.setupRMI();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            Debug.m("Error on Client run method ClassNotFound" + e.getMessage());
            try {
                tcpServer.setupRMI();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }

    }
}
