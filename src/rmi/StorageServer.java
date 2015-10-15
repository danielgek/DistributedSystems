package rmi;



import Util.Debug;
import Util.Util;
import models.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
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
        Debug.m("Servidor RMI ligado!");

    }

    /* implementation of interface methods */

    @Override
    public Response register(User user) throws RemoteException {
        Debug.m("Start registering User" + user.toString());
        try {

            rs = statement.executeQuery("SELECT * FROM users WHERE user = '" + user.getUsername() +"';");
            if(rs.next()){ // se nao esta vazio
                rs.first();// busca o primeiro
                if(user.getUsername().equals(rs.getString("user"))){
                    return new Response(false, "That username is allreaddy in use! pls register with another username!");
                }
            }


            statement.executeUpdate("INSERT INTO users (user,pass,balance) VALUES ('" + user.getUsername() + "', '" + user.getPassword() + "' , 100)");

            //cleanSql(rs, statement);
            return new Response(true, "Registered successfuly");

        }
        catch (SQLException ex){
            // handle any errors
            Debug.m("SQLException: " + ex.getMessage());
            Debug.m("SQLState: " + ex.getSQLState());
            Debug.m("VendorError: " + ex.getErrorCode());
            //cleanSql(rs, statement);
            return new Response(false, "Error on inserting on database");
        }
    }

    @Override
    public Response login(User user) throws RemoteException {
        Debug.m("Starting Login" + user.toString());
        try {
            Debug.m("SELECT * FROM users WHERE user = '" + user.getUsername() + "' AND pass = '" + user.getPassword() + "';");
            rs = statement.executeQuery("SELECT * FROM users WHERE user = '" + user.getUsername() + "' AND pass = '" + user.getPassword() + "';");
            rs.first();
            Debug.m(rs.getString("user"));
            if(user.getUsername().equals(rs.getString("user"))  && user.getPassword().equals(rs.getString("pass"))){
                Debug.m(rs.getString("user"));
                //cleanSql(rs, statement);
                user.setBalance(rs.getDouble("balance"));
                user.setId(rs.getInt("id"));
                return new Response(true, "Logged in successfuly", user);
            }else{
                //cleanSql(rs, statement);
                return new Response(false, "User cardentials wrong!" ,null);
            }



        }
        catch (SQLException ex){
            // handle any errors
            Debug.m("SQLException: " + ex.getMessage());
            Debug.m("SQLState: " + ex.getSQLState());
            Debug.m("VendorError: " + ex.getErrorCode());
            //cleanSql(rs, statement);
            return new Response(false, "Error on login on database");
        }
    }

    @Override
    public Response createProject(Project project) throws RemoteException {

        Debug.m("Creating project! " + project.toString());

        try {


            statement.executeUpdate("INSERT INTO projects (id_admin, title, description, objective, limite)" +
                    "VALUES" +
                    "(" + project.getAdminId() + ", '" +
                    project.getName() + "', '" +
                    project.getDescription() + "', " +
                    project.getObjective() + ", '" +
                    Util.convertFromJAVADateToSQLDate(project.getLimit()) + "');", Statement.RETURN_GENERATED_KEYS);

            rs=statement.getGeneratedKeys();



            Response response = new Response(true, "Project inserted successfully!");

            if (rs.first()){

                //Debug.m(rs.getString("GENERATED_KEY"));
                project.setId(rs.getInt("GENERATED_KEY"));
                response.setObject(project);
            }

            Debug.m(response.toString());

            return response;
        }
        catch (SQLException ex){
            // handle any errors
            Debug.m("SQLException: " + ex.getMessage());
            Debug.m("SQLState: " + ex.getSQLState());
            Debug.m("VendorError: " + ex.getErrorCode());
            //cleanSql(rs, statement);
            return new Response(false, "Error on Project insertion");
        }
    }

    @Override
    public Response getProject(int id) throws RemoteException {
        try {
            rs = statement.executeQuery("SELECT * FROM projects WHERE id = " + id + ";");

            Response response = new Response(true, "There is your project!");

            if(rs.next()){
                response.setObject(new Project(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDouble(5),
                        rs.getDate(6)));
            }
            //cleanSql(rs, statement);
            return response;

        } catch (SQLException e) {
            return new Response(false, "Can't find that project!");
        }
    }

    @Override
    public Response getProjectByAdmin(int id) throws RemoteException {
        try {
            rs = statement.executeQuery("SELECT * FROM projects WHERE id_admin = " + id + ";");

            Response response = new Response(true, "There is your project!");

            ArrayList<Project> projects = new ArrayList<>();
            while(rs.next()){
                projects.add(new Project(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDouble(5),
                        rs.getDate(6)));
            }

            response.setObject(projects);
            //cleanSql(rs, statement);
            return response;

        } catch (SQLException e) {
            return new Response(false, "Can't find that project!");
        }
    }

    @Override
    public Response getProjects() throws RemoteException {
        try {
            rs = statement.executeQuery("SELECT * FROM projects;");

            Response response = new Response(true, "There is your project!");

            ArrayList<Project> projects = new ArrayList<>();
            while(rs.next()){
                projects.add(new Project(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDouble(5),
                        rs.getDate(6)));
            }
            Debug.m(projects.size()+"");

            response.setObject(projects);
            //cleanSql(rs, statement);
            return response;

        } catch (SQLException e) {
            return new Response(false, "Can't find any project!");
        }
    }

    @Override
    public Response createReward(Reward reward) throws RemoteException {
        Debug.m("Creating reward! " + reward.toString());

        try {

            statement.executeUpdate("INSERT INTO rewards (id_project, description, value)" +
                    "VALUES" +
                    "(" + reward.getProjectId() + ", '" +
                    reward.getDescription() + "', " +
                    reward.getValue() + ");");

            rs=statement.getGeneratedKeys();

            Response response = new Response(true, "Reward inserted successfully!");

            if (rs.next()){
                response.setObject(new Reward(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getDouble(4)));
            }
            //cleanSql(rs, statement);
            return response;
        }
        catch (SQLException ex){
            // handle any errors
            Debug.m("SQLException: " + ex.getMessage());
            Debug.m("SQLState: " + ex.getSQLState());
            Debug.m("VendorError: " + ex.getErrorCode());
            //cleanSql(rs, statement);
            return new Response(false, "Error on Reward insertion");
        }

    }

    @Override
    public Response getRewards(int projectId) throws RemoteException {
        try {
            rs = statement.executeQuery("SELECT * FROM rewards WHERE id_project = " + projectId + ";");

            Response response = new Response(true, "There is your reward!");

            ArrayList<Reward> rewards = new ArrayList<>();
            while(rs.next()){
                rewards.add(new Reward(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getDouble(4)));
            }
            response.setObject(rewards);

            return response;

        } catch (SQLException e) {
            return new Response(false, "Can't find that reward!");
        }
    }

    @Override
    public Response createLevel(Level level) throws RemoteException {
        Debug.m("Creating level! " + level.toString());

        try {

            statement.executeUpdate("INSERT INTO levels ( id_project, description, goal)" +
                    "VALUES" +
                    "(" + level.getIdProject() + ", '" +
                    level.getDescription() + "', " +
                    level.getGoal() + ");");

            rs=statement.getGeneratedKeys();

            Response response = new Response(true,"Level inserted successfully!");

            if (rs.next()){
                response.setObject(new Level(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getInt(4)));
            }

            return response;
        }
        catch (SQLException ex){
            // handle any errors
            Debug.m("SQLException: " + ex.getMessage());
            Debug.m("SQLState: " + ex.getSQLState());
            Debug.m("VendorError: " + ex.getErrorCode());
            return new Response(false, "Error on Level insertion");
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
    public Response getLevels(int projectId) throws RemoteException {
        try {
            rs = statement.executeQuery("SELECT * FROM levels WHERE id_project = " + projectId);

            Response response = new Response(true, "There is your level!");

            ArrayList<Level> levels = new ArrayList<>();
            while(rs.next()){
                levels.add(new Level(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getInt(4)));
            }
            response.setObject(levels);

            return response;

        } catch (SQLException e) {
            return new Response(false, "Can't find that level!");
        }
    }

    @Override
    public Response createPoll(Poll poll) throws RemoteException {
        Debug.m("Creating poll! " + poll.toString());

        try {

            statement.executeUpdate("INSERT INTO polls ( title, description, id_project, answer1, answer2)" +
                    "VALUES" +
                    "('" + poll.getTitle() + "', '" +
                    poll.getDescription() + "', " +
                    poll.getIdProject() + ", '" +
                    poll.getAnswer1() + "', '" +
                    poll.getAnswer1() + "');");

            rs=statement.getGeneratedKeys();

            Response response = new Response(true,"Poll inserted successfully!");

            if (rs.next()){
                response.setObject(new Poll(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getInt("id_project"),
                        rs.getString("answer1"),
                        rs.getString("answer2")));
            }

            return response;
        }
        catch (SQLException ex){
            // handle any errors
            Debug.m("SQLException: " + ex.getMessage());
            Debug.m("SQLState: " + ex.getSQLState());
            Debug.m("VendorError: " + ex.getErrorCode());
            return new Response(false, "Error on Poll insertion");
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
    public Response getPolls(int projectId) throws RemoteException {
        try {
            rs = statement.executeQuery("SELECT * FROM polls WHERE id_project = " + projectId);

            Response response = new Response(true, "There is your poll!");

            ArrayList<Poll> polls = new ArrayList<>();
            while(rs.next()){
                polls.add(new Poll(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getInt("id_project"),
                        rs.getString("answer1"),
                        rs.getString("answer2")));
            }
            response.setObject(polls);

            return response;

        } catch (SQLException e) {
            return new Response(false, "Can't find any poll!");
        }
    }

    @Override
    public Response createVote(Vote vote) throws RemoteException {
        Debug.m("Creating vote! " + vote.toString());

        try {

            statement.executeUpdate("INSERT INTO votes ( id_poll, answer_index)" +
                    "VALUES" +
                    "(" + vote.getIdPoll() + ", " +
                    vote.getOption() + ");");

            rs=statement.getGeneratedKeys();

            Response response = new Response(true,"Vote inserted successfully!");

            if (rs.next()){
                response.setObject(new Vote(
                        rs.getInt("id"),
                        rs.getInt("id_poll"),
                        rs.getInt("answer_index")));
            }

            return response;
        }
        catch (SQLException ex){
            // handle any errors
            Debug.m("SQLException: " + ex.getMessage());
            Debug.m("SQLState: " + ex.getSQLState());
            Debug.m("VendorError: " + ex.getErrorCode());
            return new Response(false, "Error on Vote insertion");
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
    public Response getVotes(int pollId) throws RemoteException {
        try {
            rs = statement.executeQuery("SELECT * FROM votes WHERE id_poll = " + pollId);

            Response response = new Response(true, "There is your votes!");

            ArrayList<Vote> votes = new ArrayList<>();
            while(rs.next()){
                votes.add(new Vote(
                        rs.getInt("id"),
                        rs.getInt("id_poll"),
                        rs.getInt("answer_index")));
            }
            response.setObject(votes);

            return response;

        } catch (SQLException e) {
            return new Response(false, "Can't find any votes!");
        }
    }



    public static void cleanSql(ResultSet resultSet, Statement statement){

        if (resultSet != null) {
            try {resultSet.close();} catch (SQLException sqlEx) { } // ignore

            resultSet = null;
        }

        if (statement != null) {
            try {statement.close();} catch (SQLException sqlEx) { } // ignore

            statement = null;
        }
    }
}
