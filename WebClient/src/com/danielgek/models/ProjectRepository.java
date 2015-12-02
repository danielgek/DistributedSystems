package com.danielgek.models;

import models.Project;
import models.Response;
import rmi.StorageServerInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by danielgek on 29/11/15.
 */
public class ProjectRepository {

    private StorageServerInterface storageServer;

    public ProjectRepository() {
        try {
            storageServer = (StorageServerInterface) Naming.lookup("//127.0.0.1:25055/storageServer");
        } catch (NotBoundException |MalformedURLException |RemoteException e ) {
            e.printStackTrace();
        }
    }

    public ArrayList<Project> getAllProjects(){
        ArrayList<Project> projects = new ArrayList<>();
        try {
            Response response = storageServer.getProjects();
            if(response.isSuccess()){
                projects = (ArrayList<Project>) response.getObject();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return projects;
    }

    public ArrayList<Project> getMyProjects(int id){
        ArrayList<Project> projects = new ArrayList<>();
        try {
            Response response = storageServer.getProjectByAdmin(id);
            if(response.isSuccess()){
                projects = (ArrayList<Project>) response.getObject();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return projects;
    }

    public ArrayList<Project> getOldProjects() {
        ArrayList<Project> projects = new ArrayList<>();
        try {
            Response response = storageServer.getOldProjects();
            if(response.isSuccess()){
                projects = (ArrayList<Project>) response.getObject();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return projects;
    }
}
