import Util.Debug;
import rmi.StorageServer;
import rmi.StorageServerInterface;

import java.net.MalformedURLException;
import java.net.MulticastSocket;
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

    public TCPServer(String rmiAdress, String rmiPort, int port) {
        naming = "//" + rmiAdress + ":" + rmiPort + "/storageServer";
        this.tcpServerPort = port;

        setupRMI();

        loop();
    }

    public boolean setupRMI() {

        System.out.println("Trying to reconnect to RMI server!!");

        boolean verification = false;
        while(!verification) {
            try {
                storageServer = (StorageServerInterface) Naming.lookup(naming);
                Debug.m("Connected to RMI");
                verification = true;
                return true;
            } catch (NotBoundException e) {
                //Debug.m(e.getMessage());

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }

                System.out.print(".");

            } catch (MalformedURLException e) {
                //Debug.m(e.getMessage());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }

                System.out.print(".");


            } catch (RemoteException e) {
                //Debug.m(e.getMessage());

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }

                System.out.print(".");

            }
        }

        return verification;

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


        if(args.length < 4){
            System.out.println("Error on start up options:");
            System.out.println("Options:  true(primary) <rmi server address> <rmi port> <running port>");
            System.out.println("Options:  false(backup) <rmi server address> <rmi port> <running port> <primary server ip> ");
            return ;
        }

        boolean primary;

        if (args[0].equals("true")){
            primary = true;
        }else {
            primary = false;
        }

        String rmiAdress = args[1];
        String rmiPort = args[2];

        String primaryServerAdress;

        if (!primary){
            primaryServerAdress = args[4];
        }else{
            primaryServerAdress = "localhost";
        }

        int port = Integer.parseInt(args[3]);


        BackupServerStatus  backupServerStatus = new BackupServerStatus( primaryServerAdress, primary);

        if(!primary){
            while(!backupServerStatus.isBackup()){

                //System.out.println("passei aqui");
                try {Thread.sleep(1000);} catch(InterruptedException ex) {Thread.currentThread().interrupt();}
                //while is backup it will be stuck here till it can be primary the it will get the job done
            }
        }



        Debug.m("Starting TCP Server on " + port);
        new TCPServer(rmiAdress, rmiPort, port);
    }
}
