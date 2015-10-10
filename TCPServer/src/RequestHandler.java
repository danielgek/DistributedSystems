import models.Response;
import models.User;
import rmi.StorageServerInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by danielgek on 07/10/15.
 */
public class RequestHandler {

    private String naming;
    private StorageServerInterface storageServer;


    public RequestHandler() {
        naming = "//127.0.0.1:25055/storageServer";
        try {
            storageServer = (StorageServerInterface) Naming.lookup(naming);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }

    public Response register(User user) throws RemoteException {
        return storageServer.register(user);

    }




}
