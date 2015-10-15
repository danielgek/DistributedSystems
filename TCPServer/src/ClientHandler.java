
import Util.Debug;
import models.Project;
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
                Debug.m(action.toString());
                Response response = null;
                switch (action.getAction()){
                    case Action.SIGUP:
                        response = this.storageServer.register((User) action.getObject());
                        break;
                    case Action.LOGIN:
                        response = this.storageServer.login((User) action.getObject());
                        Debug.m(response.toString());
                        break;
                    case Action.CREATE_PROJECT:
                        response = this.storageServer.createProject((Project) action.getObject());
                        Debug.m(response.toString());
                        break;
                    case Action.GET_PROJECT:
                        response = this.storageServer.getProject((Integer) action.getObject());
                        Debug.m(response.toString());
                        break;
                    case Action.GET_PROJECTS:
                        response = this.storageServer.getProjects();
                        Debug.m(response.toString());
                        break;
                    case Action.GET_PROJECT_BY_ADMIN:
                        response = this.storageServer.getProjectByAdmin((Integer) action.getObject());
                        Debug.m(response.toString());
                        break;
                }



                out.writeObject(response);

            }
        }catch(Exception e){
            Debug.m("Error on Client run method" + e.getMessage());
            e.printStackTrace();
            try {
                tcpServer.setupRMI();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }

    }
}
