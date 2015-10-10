package rmi;



import Util.Debug;
import models.Project;
import models.Response;
import models.User;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.Date;

/**
 * Created by danielgek on 05/10/15.
 */
public class StorageServer extends UnicastRemoteObject implements StorageServerInterface {

    /**
     *
     */
    private Connection connection;
    private Statement statement = null;
    private ResultSet rs = null;

    public StorageServer() throws RemoteException {
        super();
        // try for java workarraound taken from mysql website
        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sd", "root", "secret");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // setup for querys
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



    public static void main(String args[]) {

        System.setProperty("java.rmi.server.hostname", "127.0.0.1");
        try {
            StorageServerInterface storageServerInterface = new StorageServer();
            LocateRegistry.createRegistry(25055).rebind("storageServer", storageServerInterface);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Servidor RMI ligado!");

    }

    /* implementation of interface methods */

    @Override
    public Response register(User user) throws RemoteException {
        Debug.message("Start registering User");
        try {

            rs = statement.executeQuery("SELECT * FROM users WHERE user = '" + user.getUsername() +"';");
            if(rs.next()){ // se nao esta vazio
                rs.first();// busca o primeiro
                if(user.getUsername() == rs.getString("user")){
                    return new Response(false, "That username is allreaddy in use! pls register with another username!");
                }
            }


            statement.executeUpdate("INSERT INTO users (user,pass,balance) VALUES ('" + user.getUsername() + "', '" + user.getPassword() + "' , 100)");
            return new Response(true, "Registered successfuly");

        }
        catch (SQLException ex){
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return new Response(false, "Error on inserting on database");
        }
        finally {
            // cleaning stuff
            if (rs != null) {
                try {rs.close();} catch (SQLException sqlEx) { } // ignore

                rs = null;
            }

            if (statement != null) {
                try {statement.close();} catch (SQLException sqlEx) { } // ignore

                statement = null;
            }
        }
    }

    @Override
    public Response login(User user) throws RemoteException {
        Debug.message("Starting Login");
        try {

            rs = statement.executeQuery("SELECT * FROM users WHERE username = '" + user.getUsername() + "' AND password = '" + user.getPassword() + "';");
            rs.first();
            if(user.getUsername() == rs.getString("user")){
                return new Response(true, "Logged in successfuly");
            }else{
                return new Response(false,"User cardentials wrong!");
            }


        }
        catch (SQLException ex){
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return new Response(false, "Error on login on database");
        }
        finally {
            // cleaning stuff
            if (rs != null) {
                try {rs.close();} catch (SQLException sqlEx) { } // ignore

                rs = null;
            }

            if (statement != null) {
                try {statement.close();} catch (SQLException sqlEx) { } // ignore

                statement = null;
            }
        }
    }

    @Override
    public Response createProject(Project project) throws RemoteException {
        return null;
    }

    @Override
    public Response getProject(int id) throws RemoteException {
        return null;
    }
}
