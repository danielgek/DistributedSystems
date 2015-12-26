package com.danielgek.models;

/**
 * Created by danielgek on 08/12/15.
 */

import models.Response;
import rmi.StorageServerInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import models.Message;

public class MessageRepository {
    private StorageServerInterface storageServer;
    private ArrayList<Message> messages;
    private Message message;


    public MessageRepository() {
        try {
            storageServer = (StorageServerInterface) Naming.lookup("//127.0.0.1:25055/storageServer");
        } catch (NotBoundException |MalformedURLException |RemoteException e ) {
            e.printStackTrace();
        }
    }

    public boolean save(Message message){
        try {
            Response response = storageServer.sendMessage(message);
            if (response.isSuccess()){
                this.message = (Message) response.getObject();
                return true;
            }else{
                return false;
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }


    }




    public ArrayList<Message> getMessages(int id) {
        try {
            Response response = storageServer.getMessagesByProject(id);
            System.out.println(response);
            return (ArrayList<Message>) response.getObject();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }


}
