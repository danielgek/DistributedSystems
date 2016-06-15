package rmi;



import Util.Debug;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verifier;
import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.exceptions.JumblrException;
import models.*;
import rest.TumblrConnection;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.text.SimpleDateFormat;
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
    private TumblrConnection tumblrConnection;
    private static final String API_APP_KEY = "72bZRYrRpFSVWvq0T8pyP7daDz8rdmMamTovQyakvHcKEhHLyB";
    private static final String API_APP_SECRET = "6tDVLgNsYNbicOzn1gaMPcoIId7FNLNQFOhaJzamtUamha1dKx";

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
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



    public static void main(String args[]) {
        if (args.length < 2){
            System.out.println("java -jar RmiServer.jar <adress> <port>");
            return;
        }
        String rmiAdress = args[0];
        int rmiPort;
        try {
            rmiPort = Integer.parseInt(args[1]);
        } catch (NumberFormatException e ){
            System.out.println("please write a valid port");
            return;
        }


        try {
            StorageServerInterface storageServerInterface = new StorageServer();
            //new EndProjectTask();
            LocateRegistry.createRegistry(rmiPort).rebind("storageServer", storageServerInterface);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Debug.m("Servidor RMI ligado!");

    }

    /* implementation of interface methods */

    @Override
    public Response register(User user) throws RemoteException {
        Debug.m("Start registering User: " + user.toString());
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
        Debug.m("Starting Login: " + user.toString());
        try {




            rs = statement.executeQuery("SELECT * FROM users WHERE user = '" + user.getUsername() + "' AND pass = '" + user.getPassword() + "';");
            rs.first();

            if(user.getUsername().equals(rs.getString("user"))  && user.getPassword().equals(rs.getString("pass"))){

                user.setBalance(rs.getDouble("balance"));
                user.setId(rs.getInt("id"));
                return new Response(true, "Logged in successfuly", user);
            }else{

                return new Response(false, "User cardentials wrong!");
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
    public Response getUser(int id) throws RemoteException {
        Debug.m("getUser: " + id);
        try {

            rs = statement.executeQuery("SELECT * FROM users WHERE id = " + id + ";");


            if(rs.next()){

                User user = new User(rs.getInt("id"),rs.getString("user"),rs.getString("pass"),rs.getDouble("balance"));

                return new Response(true, "There is your", user);

            }else{

                return new Response(false, "Cant't find any user with that id" ,null);

            }

        }
        catch (SQLException ex){

            Debug.m("SQLException: " + ex.getMessage());
            Debug.m("SQLState: " + ex.getSQLState());
            Debug.m("VendorError: " + ex.getErrorCode());

            return new Response(false, "Error on geting user");
        }
    }

    @Override
    public Response createProject(Project project) throws RemoteException {

        Debug.m("Creating project! " + project.toString());

        try {


            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");




            statement.executeUpdate("INSERT INTO projects (id_admin, title, description, objective, limite)" +
                    "VALUES" +
                    "(" + project.getAdminId() + ", '" +
                    project.getName() + "', '" +
                    project.getDescription() + "', " +
                    project.getObjective() + ", '" +
                    simpleDateFormat.format(project.getLimit()) + "');", Statement.RETURN_GENERATED_KEYS);

            rs=statement.getGeneratedKeys();



            Response response = new Response(true, "Project inserted successfully!");

            if (rs.first()){

                //Debug.m(rs.getString("GENERATED_KEY"));
                project.setId(rs.getInt("GENERATED_KEY"));
                project.setProgress(0);
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
        Debug.m("getProject(" + id + ")");
        try {
            rs = statement.executeQuery("SELECT * FROM projects WHERE id = " + id + " AND soft_deleted = 0;");

            Response response = new Response(true, "There is your project!");

            Project project = null;

            if(rs.next()){
                project = new Project(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDouble(5),
                        rs.getDate(6));
            }

            rs = statement.executeQuery("SELECT SUM(amount) AS AMOUNT FROM pledges WHERE id_project = " + project.getId() + ";");
            if(rs.next()){
                project.setProgress(rs.getDouble("AMOUNT"));
            }


            response.setObject(project);


            return response;

        } catch (SQLException e) {
            Debug.m(e.getMessage());
            e.printStackTrace();
            return new Response(false, "Can't find that project!");
        }
    }

    @Override
    public Response getProjectByAdmin(int id) throws RemoteException {
        try {
            rs = statement.executeQuery("SELECT * FROM projects WHERE id_admin = " + id + " and soft_deleted = 0;");

            Response response = new Response(true, "There is your project!");

            ArrayList<Project> projects = new ArrayList<>();
            Project project;
            while(rs.next()){
                project = new Project(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDouble(5),
                        rs.getDate(6));

                Statement statement1 = connection.createStatement();

                ResultSet resultSet = statement1.executeQuery("SELECT SUM(amount) AS AMOUNT FROM pledges WHERE id_user = " + project.getAdminId() + " AND id_project = " + project.getId() + ";");
                if(resultSet.next()){
                    project.setProgress(resultSet.getDouble("AMOUNT"));
                }
                cleanSql(resultSet, statement1);
                projects.add(project);

            }

            response.setObject(projects);
            return response;

        } catch (SQLException e) {
            Debug.m(e.getMessage());
            e.printStackTrace();
            return new Response(false, "Can't find that project!");
        }
    }

    @Override
    public Response getProjects() throws RemoteException {
        try {
            rs = statement.executeQuery("SELECT * FROM projects WHERE limite > NOW() AND soft_deleted = 0;");

            Response response = new Response(true, "There is your project!");

            ArrayList<Project> projects = new ArrayList<>();
            Project project = null;
            while(rs.next()){
                project = new Project(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDouble(5),
                        rs.getDate(6));

                Statement statement1 = connection.createStatement();

                ResultSet resultSet = statement1.executeQuery("SELECT SUM(amount) AS AMOUNT FROM pledges WHERE id_user = " + project.getAdminId() + " AND id_project = " + project.getId() + ";");
                if(resultSet.next()){
                    project.setProgress(resultSet.getDouble("AMOUNT"));
                }
                cleanSql(resultSet, statement1);
                projects.add(project);
            }

            response.setObject(projects);
            return response;

        } catch (SQLException e) {
            return new Response(false, "Can't find any project!");
        }
    }

    public Response getOldProjects() throws RemoteException {
        try {
            rs = statement.executeQuery("SELECT * FROM projects WHERE limite < NOW() AND soft_deleted = 0;");

            Response response = new Response(true, "There is your project!");

            ArrayList<Project> projects = new ArrayList<>();
            Project project = null;
            while(rs.next()){
                project = new Project(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDouble(5),
                        rs.getDate(6));

                Statement statement1 = connection.createStatement();

                ResultSet resultSet = statement1.executeQuery("SELECT SUM(amount) AS AMOUNT FROM pledges WHERE id_user = " + project.getAdminId() + " AND id_project = " + project.getId() + ";");
                if(resultSet.next()){
                    project.setProgress(resultSet.getDouble("AMOUNT"));
                }
                cleanSql(resultSet, statement1);
                projects.add(project);
            }

            response.setObject(projects);
            return response;

        } catch (SQLException e) {
            return new Response(false, "Can't find any project!");
        }
    }

    @Override
    public Response removeProject(int id) throws RemoteException {
        try {
            statement.executeUpdate("UPDATE projects SET soft_deleted = 1 WHERE id = " + id);

            rs = statement.executeQuery("SELECT * FROM pledges WHERE id_project = " + id);

            String query = "DELETE FROM pledges WHERE ";
            while(rs.next()){
                if(rs.isFirst()){
                    query += " id = " + rs.getInt("id");
                }else {
                    query +=  " OR id = " + rs.getInt("id");
                }
                Statement balanceStatement = connection.createStatement();

                balanceStatement.executeUpdate("UPDATE users SET balance = balance + " + rs.getDouble("amount") + " WHERE id = " + rs.getInt("id_user"));
                balanceStatement.close();
            }

            statement.executeUpdate(query);



            return new Response(true, "Project deleted successfully!");
        } catch (SQLException e) {
            Debug.m(e.getMessage());
            e.printStackTrace();
            return new Response(false, "Error on deleting project!");

        }
    }

    @Override
    public Response addReward(Reward reward) throws RemoteException {
        Debug.m("Creating reward! " + reward.toString());

        try {

            statement.executeUpdate("INSERT INTO rewards (id_project, description, value)" +
                    "VALUES" +
                    "(" + reward.getProjectId() + ", '" +
                    reward.getDescription() + "', " +
                    reward.getValue() + ");", Statement.RETURN_GENERATED_KEYS);

            rs=statement.getGeneratedKeys();

            Response response = new Response(true, "Reward inserted successfully!");

            if (rs.next()){
                reward.setId(rs.getInt("GENERATED_KEY"));
                response.setObject(reward);
            }

            return response;
        }
        catch (SQLException ex){

            Debug.m("SQLException: " + ex.getMessage());
            Debug.m("SQLState: " + ex.getSQLState());
            Debug.m("VendorError: " + ex.getErrorCode());

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
    public Response getCurrentRewards(int userId) throws RemoteException {
        try {
            //Debug.m("SELECT * FROM rewards, (Select * from projects ) projects, (SELECT * from pledges WHERE pledges.id_user = " + userId + ") pledges where projects.id =  rewards.id_project and projects.id = pledges.id_project and rewards.value <= pledges.amount;");

            rs = statement.executeQuery("SELECT * FROM rewards, (Select * from projects WHERE soft_deleted = 0 ) projects, (SELECT * from pledges WHERE pledges.id_user = " + userId + ") pledges where projects.id =  rewards.id_project and projects.id = pledges.id_project and rewards.value <= pledges.amount;");
            Debug.m("number" + rs.getFetchSize() + "");
            ArrayList<CurrentRewardsResult> currentRewardsResults = new ArrayList<>();
            while (rs.next()){
                Debug.m("asd");
                int rewardsId =  rs.getInt("rewards.id");
                int rewardsIdProject =  rs.getInt("rewards.id_project");
                String rewardsDescription =  rs.getString("rewards.description");
                double rewardsValue =  rs.getDouble("rewards.value");
                int projectsId =  rs.getInt("projects.id");
                int projectsIdAdmin =  rs.getInt("projects.id_admin");
                String projectsTitle =  rs.getString("projects.title");
                String projectsDescription =  rs.getString("projects.description");
                double projectsObjective =  rs.getDouble("projects.objective");
                Date projectsLimite =  rs.getDate("projects.limite");
                boolean projectsSoftDeleted =  rs.getBoolean("projects.soft_deleted");
                int pledgesId =  rs.getInt("pledges.id");
                int pledgesIdProject =  rs.getInt("pledges.id_project");
                int pledgesIdUser =  rs.getInt("pledges.id_user");
                double pledgesAmount =  rs.getDouble("pledges.amount");

                CurrentRewardsResult currentRewardsResult = new CurrentRewardsResult(rewardsId,rewardsIdProject,rewardsDescription,rewardsValue,projectsId,projectsIdAdmin,projectsTitle,projectsDescription,projectsObjective,projectsLimite,projectsSoftDeleted,pledgesId,pledgesIdProject,pledgesIdUser,pledgesAmount);

                currentRewardsResults.add(currentRewardsResult);
            }
            Debug.m(currentRewardsResults.toString());
            Response response;
            if (currentRewardsResults.isEmpty()) {
                response = new Response(false, "You don't have any rewards!");
            }else{
                response = new Response(true, "There is your rewards!", currentRewardsResults);
            }

            return response;

        } catch (SQLException e) {
            Debug.m(e.getMessage());
            return new Response(false, "Can't find that rewards!");
        }
    }

    @Override
    public Response removeReward(int id) throws RemoteException {
        try {
            statement.execute("DELETE FROM rewards WHERE id = " + id);
            return new Response(true, "Removed successfully!");

        } catch (SQLException e) {
            Debug.m(e.getMessage());
            e.printStackTrace();
            return new Response(true, "Error deleting Reward");
        }
    }

    @Override
    public Response addLevel(Level level) throws RemoteException {
        Debug.m("Creating level! " + level.toString());

        try {

            statement.executeUpdate("INSERT INTO levels ( id_project, description, goal)" +
                    "VALUES" +
                    "(" + level.getProjectId() + ", '" +
                    level.getDescription() + "', " +
                    level.getGoal() + ");", Statement.RETURN_GENERATED_KEYS);

            rs=statement.getGeneratedKeys();

            Response response = new Response(true,"Level inserted successfully!");

            if (rs.next()){
                level.setId(rs.getInt("GENERATED_KEY"));
                response.setObject(level);
            }

            return response;
        }
        catch (SQLException ex){

            Debug.m("SQLException: " + ex.getMessage());
            Debug.m("SQLState: " + ex.getSQLState());
            Debug.m("VendorError: " + ex.getErrorCode());
            return new Response(false, "Error on Level insertion");

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
            Debug.m(e.getMessage());
            e.printStackTrace();
            return new Response(false, "Can't find that level!");
        }
    }

    @Override
    public Response addPoll(Poll poll) throws RemoteException {
        Debug.m("Creating poll! " + poll.toString());

        try {

            statement.executeUpdate("INSERT INTO polls ( title, description, id_project, answer1, answer2)" +
                    "VALUES" +
                    "('" + poll.getTitle() + "', '" +
                    poll.getDescription() + "', " +
                    poll.getProjectId() + ", '" +
                    poll.getAnswer1() + "', '" +
                    poll.getAnswer1() + "');", Statement.RETURN_GENERATED_KEYS);

            rs=statement.getGeneratedKeys();

            Response response = new Response(true,"Poll inserted successfully!");

            if (rs.next()){
                poll.setId(rs.getInt("GENERATED_KEY"));
                response.setObject(poll);
            }

            return response;
        }
        catch (SQLException ex){

            Debug.m("SQLException: " + ex.getMessage());
            Debug.m("SQLState: " + ex.getSQLState());
            Debug.m("VendorError: " + ex.getErrorCode());
            return new Response(false, "Error on Poll insertion");
        }

    }

    @Override
    public Response getPoll(int pollId) throws RemoteException {
        try {
            rs = statement.executeQuery("SELECT * FROM polls WHERE id = " + pollId);

            Response response = new Response(true, "There is your poll!");

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

        } catch (SQLException e) {
            return new Response(false, "Can't find that poll!");
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
    public Response addVote(Vote vote) throws RemoteException {
        Debug.m("Creating vote! " + vote.toString());

        try {

            statement.executeUpdate("INSERT INTO votes ( id_poll, answer_index)" +
                    "VALUES" +
                    "(" + vote.getIdPoll() + ", " +
                    vote.getOption() + ");", Statement.RETURN_GENERATED_KEYS);

            rs=statement.getGeneratedKeys();

            Response response = new Response(true,"Vote inserted successfully!");

            if (rs.next()){
                vote.setId(rs.getInt("GENERATED_KEY"));
                response.setObject(vote);
            }

            return response;
        }
        catch (SQLException ex){

            Debug.m("SQLException: " + ex.getMessage());
            Debug.m("SQLState: " + ex.getSQLState());
            Debug.m("VendorError: " + ex.getErrorCode());
            return new Response(false, "Error on Vote insertion");
        }

    }

    @Override
    public Response getVotes(int pollId) throws RemoteException {
        try {
            int answerACount = 0;
            int answerBCount = 0;
            rs = statement.executeQuery("SELECT COUNT(id) AS ANSWER_A FROM votes WHERE id_poll = " + pollId + " AND answer_index = 1" );
            if(rs.next()){
                answerACount = rs.getInt("ANSWER_A");
            }else {
                // no Votes in Anser A
            }

            rs = statement.executeQuery("SELECT COUNT(id) AS ANSWER_B FROM votes WHERE id_poll = " + pollId + " AND answer_index = 2" );
            if(rs.next()){
                answerBCount = rs.getInt("ANSWER_B");
            }else {
                // no Votes in Anser A
            }

            Response response = new Response(true, "There is your votes!");

            response.setObject(new PollResult(answerACount,answerBCount));

            return response;

        } catch (SQLException e) {
            return new Response(false, "Can't find any votes!");
        }
    }

    @Override
    public Response pledge(Pledge pledge) throws RemoteException {
        Debug.m("Creating vote! " + pledge.toString());

        try {

            double balanceUser = 0;

            rs = statement.executeQuery("SELECT balance FROM users WHERE id = " + pledge.getUserId());

            if(rs.next()){
                balanceUser = rs.getDouble("balance");
                if(pledge.getAmount() > balanceUser ){
                    return new Response(false, "You don't have enought money!!");
                }
            }else {
                return new Response(false, "Cant find user to insert pledge!!");
            }


            statement.executeUpdate("INSERT INTO pledges ( id_project, id_user, amount)" +
                    "VALUES" +
                    "(" + pledge.getProjectId() + ", " +
                    pledge.getUserId() + ", " +
                    pledge.getAmount() + ");", Statement.RETURN_GENERATED_KEYS);

            balanceUser = balanceUser - pledge.getAmount();
            statement.executeUpdate("UPDATE users set balance = " + balanceUser + " WHERE id = " + pledge.getUserId());

            return new Response(true,"Pledged successfully!");
        }
        catch (SQLException ex){

            Debug.m("SQLException: " + ex.getMessage());
            Debug.m("SQLState: " + ex.getSQLState());
            Debug.m("VendorError: " + ex.getErrorCode());
            return new Response(false, "Error on Pledge insertion");
        }
    }


    @Override
    public Response sendMessage(Message message) throws RemoteException {

        try {
            statement.executeUpdate("INSERT INTO messages(message, sender, receiver, project) VALUES ('" + message.getMessage() + "', " + message.getSender() + ", " + message.getReceiver() + ", " + message.getProjectId() + ");");
            return new Response(true, "Message sended!");
        } catch (SQLException e) {
            Debug.m(e.getMessage());
            e.printStackTrace();
            return new Response(false, "Error sending message!");

        }
    }

    @Override
    public Response getMessagesByProject(int id) throws RemoteException {
        try {
            rs = statement.executeQuery("SELECT * FROM messages WHERE project = " +id);
            ArrayList<Message> messages = new ArrayList<>();

            while (rs.next()){
                messages.add(new Message(rs.getInt("id"), rs.getString("message"),rs.getInt("sender"),rs.getInt("receiver")));

            }
            Response response = null;
            if(! messages.isEmpty())
                return new Response(true, "There are your things", messages);
            else
                return new Response(false, "There are no Messages for this project!");
        } catch (SQLException e) {
            Debug.m(e.getMessage());
            e.printStackTrace();
            return new Response(false, "Error getting Messages!");
        }
    }

    @Override
    public Response updateProject(Project project) throws RemoteException {
        try {
            statement.executeUpdate("UPDATE projects SET description='" + project.getDescription() + "', title='" + project.getName() + "' WHERE id= " + project.getId() + "; ");
            return new Response(true, "Updated");

        } catch (SQLException e) {
            Debug.m(e.getMessage());
            e.printStackTrace();
            return new Response(false, "Error getting Messages!");
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        cleanSql(rs,statement);
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


    @Override
    public Response getAuthUrl() throws RemoteException {
        tumblrConnection =  new TumblrConnection();

        return new Response(true,"Auth url", tumblrConnection.getAuthorizationUrl());
    }

    @Override
    public Response tumblrLogin(String verifier) throws RemoteException {



        Token accessToken = tumblrConnection.getAccessToken(new Verifier(verifier));

        try {

            JumblrClient client = new JumblrClient(API_APP_KEY,API_APP_SECRET,accessToken.getToken(), accessToken.getSecret());

            Response response = null;
            rs = statement.executeQuery("SELECT * FROM tumblr_user WHERE name = '" + client.user().getName() + "';");
            if(rs.next()){ // se nao esta vazio
                rs.first();// busca o primeiro
                if(client.user().getName().equals(rs.getString("name")) && (rs.getInt("user_id") != 0)){
                    Response resp = getUser(rs.getInt("user_id"));

                    response = new Response(true, client.user().getName(), resp.getObject());


                }else if(rs.getInt("user_id") == 0 ){
                    response = new Response(false,"not associated", client.user().getName());
                }
                statement.executeUpdate("UPDATE tumblr_user SET api_user_token = '" + accessToken.getToken() + "', api_user_secret = '" + accessToken.getSecret() + "' WHERE name = '" + client.user().getName() + "';");
            }else{
                statement.executeUpdate("INSERT INTO tumblr_user (name,api_user_token, api_user_secret)  VALUES ('" + client.user().getName() + "', '" + accessToken.getToken() +  "','" +  accessToken.getSecret() + "')");
                response = new Response(true, "registered",client.user().getName() );

            }
            //cleanSql(rs, statement);

            return response;


        }catch (JumblrException e){
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Response(false,"boa cena");
    }

    @Override
    public Response tumblrJoin(String username, User user) throws RemoteException {


        try {
            rs = statement.executeQuery("SELECT * FROM users WHERE user = '" + user.getUsername() + "' AND pass = '" + user.getPassword() + "';");
            rs.first();

            if(user.getUsername().equals(rs.getString("user"))  && user.getPassword().equals(rs.getString("pass"))){

                user.setBalance(rs.getDouble("balance"));
                user.setId(rs.getInt("id"));
                rs = statement.executeQuery("SELECT * FROM tumblr_user WHERE name = '" + username + "';");
                if(rs.first()){

                    statement.executeUpdate("UPDATE tumblr_user SET user_id = " + user.getId() + " WHERE name = '" + username + "';");
                    return new Response(true, "Joined Users susseccfully", user);
                }else{

                    return new Response(false, "tumblr user dont exist!");
                }
            }else if (user != null){
                //you ha a user but its not authenticated successfully
                return new Response(false, "User cardentials wrong! ");
            }else{
                //no user but creating one !!
                statement.executeUpdate("INSERT INTO users (user,pass,balance) VALUES ('" + username + "', '' , 100)", Statement.RETURN_GENERATED_KEYS);
                rs = statement.getGeneratedKeys();
                int id = rs.getInt("GENERATED_KEY");
                User user1 = new User(id,username,"",100);
                return new Response(true, username , user1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Response(false,"unkown error" + e.getMessage());
        }

    }
}
