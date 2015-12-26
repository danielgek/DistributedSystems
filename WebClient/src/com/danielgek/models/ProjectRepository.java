package com.danielgek.models;

import models.Pledge;
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

    public Project newProject(Project project){
        try {
            Response response = storageServer.createProject(project);
            if(response.isSuccess()){
                return (Project) response.getObject();
            }else{
                return null;
            }
        } catch (RemoteException e) {
            System.out.println(e);
            e.printStackTrace();
            return null;
        }
    }

    public Project getProject(int id){
        try {
            Response response = storageServer.getProject(id);
            if(response.isSuccess()){
                return (Project) response.getObject();
            }else{
                return null;
            }
        } catch (RemoteException e) {
            System.out.println(e);
            e.printStackTrace();
            return null;
        }
    }

    public boolean removeProject(int id){
        try {
            Response response = storageServer.removeProject(id);
            if(response.isSuccess()){
                return true;
            }else{
                return false;
            }
        } catch (RemoteException e) {
            System.out.println(e);
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Project project) {
        try {
            Response response = storageServer.updateProject(project);
            if(response.isSuccess())
                return true;
            else
                return false;
        } catch (RemoteException e) {

            e.printStackTrace();
        }
        return false;
    }

    public Response pledge(Pledge pledge){
        try {
            Response response = storageServer.pledge(pledge);
            if(response.isSuccess())
                return response;
            else{
                System.out.println(response.getMessage());
                return response;
            }

        } catch (RemoteException e) {

            e.printStackTrace();
        }
        return null;
    }
}
