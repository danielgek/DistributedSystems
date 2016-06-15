package com.danielgek.repositories;

import models.Response;
import models.User;
import rmi.StorageServerInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by danielgek on 29/11/15.
 */
public class UserRepository {

    private StorageServerInterface storageServer;
    private User user;

    public UserRepository() {
        try {
            storageServer = (StorageServerInterface) Naming.lookup("//127.0.0.1:25055/storageServer");
        } catch (NotBoundException |MalformedURLException |RemoteException e ) {
            e.printStackTrace();
        }
    }

    public boolean authenticate(String username, String password){

        try {
            Response response = storageServer.login(new User(username,password));
            if (response.isSuccess()){
                this.user = (User) response.getObject();

                return true;
            }else{
                return false;
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }


    }

    public User getUser(int id) {
        try {
            Response response = storageServer.getUser(id);
            if (response.isSuccess()){
                User user = (User) response.getObject();

                return user;
            }else{
                return null;
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean joinTumblr(String tumblrUsername){
        try {
            Response response = storageServer.tumblrJoin(tumblrUsername,user);
            if(response.isSuccess()){
                return true;
            }else{
                System.out.println(response.getMessage());
                return false;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;

        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
