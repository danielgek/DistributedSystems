package com.danielgek.models;

import models.Level;
import models.Response;
import rmi.StorageServerInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by danielgek on 08/12/15.
 */
public class LevelRepository {
    private StorageServerInterface storageServer;
    private Level level;
    private ArrayList<Level> levels;
    public LevelRepository() {
        try {
            storageServer = (StorageServerInterface) Naming.lookup("//127.0.0.1:25055/storageServer");
        } catch (NotBoundException |MalformedURLException |RemoteException e ) {
            e.printStackTrace();
        }
    }

    public boolean save(Level level){

        try {
            Response response = storageServer.addLevel(level);
            if (response.isSuccess()){
                this.level = (Level) response.getObject();
                return true;
            }else{
                return false;
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }


    }

    public boolean save(ArrayList<Level> levels){

        try {
            for (int i = 0; i < levels.size(); i++) {
                Response response = storageServer.addLevel(levels.get(i));
                if (response.isSuccess()){
                    return true;
                }else{
                    return false;
                }
            }
            return false;


        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }


    }

    public boolean delete(int id){

        /*
            NEEDS TO BE IMPLEMENTED ON RMI SIDE

            Response response = storageServer.removeLevel(id);
            if (response.isSuccess()){
                return true;
            }else{
                return false;
            }
        */

        return true;


    }

    public ArrayList<Level> getLevels(int id) {
        try {
            Response response = storageServer.getLevels(id);
            return (ArrayList<Level>) response.getObject();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
}
