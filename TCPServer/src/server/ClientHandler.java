package server;

import Util.Debug;
import models.*;
import rmi.StorageServerInterface;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
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
            new ClientHandler(this.socket, this.storageServer, this.tcpServer);
            Debug.m("Error on Client Contructor:" + e);
        }

        this.start();
    }


    public void run() {

        try{
            while(true){
                System.out.println("isConnected");
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
                    case Action.GET_OLD_PROJECTS:
                        response = this.storageServer.getOldProjects();
                        Debug.m(response.toString());
                        break;
                    case Action.GET_PROJECT_BY_ADMIN:
                        response = this.storageServer.getProjectByAdmin((Integer) action.getObject());
                        Debug.m(response.toString());
                        break;
                    case Action.REMOVE_PROJECT:
                        response = this.storageServer.removeProject((Integer) action.getObject());
                        Debug.m(response.toString());
                        break;
                    case Action.ADD_LEVEL:
                        response = this.storageServer.addLevel((Level) action.getObject());
                        Debug.m(response.toString());
                        break;
                    case Action.GET_LEVELS_BY_PROJECT:
                        response = this.storageServer.getLevels((Integer) action.getObject());
                        Debug.m(response.toString());
                        break;
                    case Action.ADD_POLL:
                        response = this.storageServer.addPoll((Poll) action.getObject());
                        Debug.m(response.toString());
                        break;
                    case Action.GET_POLL:
                        response = this.storageServer.getPoll((Integer) action.getObject());
                        Debug.m(response.toString());
                        break;
                    case Action.GET_POLL_BY_PROJECT:
                        response = this.storageServer.getPolls((Integer) action.getObject());
                        Debug.m(response.toString());
                        break;
                    case Action.ADD_REWARD:
                        response = this.storageServer.addReward((Reward) action.getObject());
                        Debug.m(response.toString());
                        break;
                    case Action.REMOVE_REWARD:
                        response = this.storageServer.removeReward((Integer) action.getObject());
                        Debug.m(response.toString());
                        break;
                    case Action.GET_REWARDS_BY_PROJECT:
                        response = this.storageServer.getRewards((Integer) action.getObject());
                        Debug.m(response.toString());
                        break;
                    case Action.GET_CURRENT_REWARDS:
                        response = this.storageServer.getCurrentRewards((Integer) action.getObject());
                        Debug.m(response.toString());
                        break;
                    case Action.ADD_VOTE:
                        response = this.storageServer.addVote((Vote) action.getObject());
                        Debug.m(response.toString());
                        break;
                    case Action.GET_VOTES_BY_POLL:
                        response = this.storageServer.getVotes((Integer) action.getObject());
                        Debug.m(response.toString());
                        break;
                    case Action.PLEDGE:
                        response = this.storageServer.pledge((Pledge) action.getObject());
                        Debug.m(response.toString());
                        break;
                    case Action.GET_USER:
                        response = this.storageServer.getUser((Integer) action.getObject());
                        Debug.m(response.toString());
                        break;
                    case Action.SEND_MESSAGE:
                        response = this.storageServer.sendMessage((Message) action.getObject());
                        Debug.m(response.toString());
                        break;
                    case Action.GET_MESSAGES:
                        response = this.storageServer.getMessagesByProject((Integer) action.getObject());
                        Debug.m(response.toString());
                        break;
                }

                out.writeObject(response);

            }
        } catch (ClassNotFoundException e) {
            Debug.m("Error on server.ClientHandler ClassNotFoundException: " + e.getMessage());
            e.printStackTrace();
            try {
                out.writeObject(new Response(false, "Error on Conection! please try again"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (RemoteException e) {
            Debug.m("Error on server.ClientHandler RemoteException: " + e.getMessage());
            try {
                out.writeObject(new Response(false, "Error on Conection! please try again!"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            tcpServer.setupRMI();

        } catch (SocketTimeoutException e){
            Debug.m(e.getMessage());

        } catch (EOFException e){
            Debug.m("Error on server.ClientHandler IOException: problabli disconected");


            //e.printStackTrace();
            try {
                out.writeObject(new Response(false, "Error on Conection! please try again"));
            } catch (IOException e1) {
                //e1.printStackTrace();
            }
            this.interrupt();
        } catch (IOException e) {

            Debug.m("Error on server.ClientHandler IOException: problably disconected");


            //e.printStackTrace();
            try {
                out.writeObject(new Response(false, "Error on Conection! please try again"));
            } catch (IOException e1) {
                //e1.printStackTrace();
            }
            this.interrupt();
        }

    }


}
