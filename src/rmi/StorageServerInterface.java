package rmi;

import models.Project;
import models.Response;
import models.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

/**
 * Created by danielgek on 05/10/15.
 */
public interface StorageServerInterface extends Remote{
    public Response register(User user) throws RemoteException;
    public Response login(User user) throws RemoteException;
    public Response createProject(Project project) throws RemoteException;
    public Response getProject(int id) throws RemoteException;
}
