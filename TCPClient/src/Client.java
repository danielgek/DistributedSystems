import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
//import java.time.format.
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import Util.Debug;
import Util.Util;
import models.*;


/**
 * Created by danielgek on 07/10/15.
 */
public class Client {
    private  Socket socket = null;

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private User user;
    private String host1;
    private int port1;
    private String host2;
    private int port2;
    private final static int MAX_RETRIES = 10;

    public Client(String host1, int port1, String host2, int port2) {


        this.host1 = host1;
        this.host2 = host2;
        this.port1 = port1;
        this.port2 = port2;

        try {
            // 1o passo
            socket = new Socket();
            socket.connect(new InetSocketAddress(host1,port1), 3000);
            //socket.setSoTimeout(3000);

            System.out.println("Ligado ao servidor TCP!!§");
            // 2o passo
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());




        } catch (Exception e) {
            //Debug.m("Sock:" + e.getMessage());
            handleConnectionError(e);
        }
        menu();
    }

    private void menu(){
        System.out.println("+++++++++++++++++++++++++++++++");
        System.out.println("+ 1. Sigup                    +");
        System.out.println("+ 2. Login                    +");
        System.out.println("+ 3. Exit                     +");
        System.out.println("+++++++++++++++++++++++++++++++");

        int option = Util.readInt(3);

        switch (option){
            case 1:

                register();
                menu();
                break;
            case 2:
                login();
                menu();
                break;
            case 3:
                System.exit(0);
                break;
        }

    }

    private void menuLogin(){

        System.out.println("\n+++++++++++++++++++++++++++++++");
        System.out.println("+ 1. Create Project           +");
        System.out.println("+ 2. View My Current Projects +");
        System.out.println("+ 3. View My Old Projects     +");
        System.out.println("+ 4. View Projects            +");
        System.out.println("+ 5. See Balance              +");
        System.out.println("+ 6. See Rewards              +");
        System.out.println("+ 7. Logout                   +");
        System.out.println("+++++++++++++++++++++++++++++++");

        int option = Util.readInt(6);
        switch (option){
            case 1 :


                System.out.println("Insert project title: ");
                String title = Util.readString();
                System.out.println("Insert project description: ");
                String description = Util.readString();
                System.out.println("Insert project objective: ");
                double objective = Util.readDouble();
                System.out.println("Insert project final date(dd/mm/yyyy hh:mm): ");

                boolean dateCheck = true;
                Date limit = null;
                while (dateCheck){
                    try {
                        //limit = new Date(Util.readString());
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        limit = formatter.parse(Util.readString());
                        System.out.println(limit.toString());
                        if(limit != null) dateCheck = false;
                    } catch ( Exception e) {
                        System.out.println("Date not recognized! \nPlease insert again: ");

                    }
                }

                try {
                    Project project = new Project(user.getId(), title,description,objective,limit);

                    out.writeObject(new Action(Action.CREATE_PROJECT, project));

                    Response response = (Response) in.readObject();


                    if(response.isSuccess()){
                        System.out.println("Project " + project.getName() + " created successfully!");
                    }else{
                        System.out.println("Erro! :" + response.getMessage());
                    }
                    menuLogin();
                } catch (Exception e){
                    handleConnectionError(e);
                    menuLogin();

                }


                break;
            case 2 :
                try {

                    out.writeObject(new Action(Action.GET_PROJECT_BY_ADMIN, user.getId()));

                    Response response = (Response) in.readObject();


                    if(response.isSuccess()){


                        ArrayList<Project> projects = (ArrayList<Project>) response.getObject();



                        for (int i = 1; i <= projects.size(); i++) {
                            System.out.println(i + ". " + projects.get(i - 1).getName() + " Progress: " + projects.get(i - 1).getObjective() + "/" + projects.get(i - 1).getProgress());

                        }
                        if(projects.isEmpty()){
                            System.out.print("You don't have any projects yet!");
                            menuLogin();
                        }else {
                            System.out.println("Choose one project to view details or 0 to go back");
                        }

                        int project = Util.readInt(projects.size());

                        if(project == 0){
                            menuLogin();
                        }else{
                            showProject(projects.get(project-1).getId());
                        }
                    }else{
                        System.out.println("There was a proble in the connection please try again");
                        menuLogin();
                    }





                } catch (Exception e){
                    handleConnectionError(e);
                    menuLogin();

                }


                break;
            case 3 :
                try {
                    out.writeObject(new Action(Action.GET_OLD_PROJECTS, user.getId()));

                    Response response = (Response) in.readObject();
                    if(response.isSuccess()){

                        ArrayList<Project> projects = (ArrayList<Project>) response.getObject();



                        for (int i = 1; i <= projects.size(); i++) {
                            System.out.println(i + ". " + projects.get(i - 1).getName() + " Progress: " + projects.get(i - 1).getObjective() + "/" + projects.get(i - 1).getProgress());

                        }
                        if(projects.isEmpty()){
                            System.out.print("You don't have any old projects yet!");
                            menuLogin();
                        }else {
                            System.out.println("Choose one project to view details or 0 to go back");
                        }

                        int project = Util.readInt(projects.size());

                        if(project == 0){
                            menuLogin();
                        }else{
                            showProject(projects.get(project-1).getId());
                        }
                    }else{
                        System.out.println("There was a proble in the connection please try again");
                        menuLogin();
                    }





                } catch (Exception e){
                    handleConnectionError(e);
                    menuLogin();

                }


                break;
            case 4 :
                try {
                    Debug.m("start receiving");
                    out.writeObject(new Action(Action.GET_PROJECTS));

                    Debug.m("start receiving");
                    Response response = (Response) in.readObject();
                    Debug.m("end receiving");
                    if(response.isSuccess()){
                        ArrayList<Project> projects = (ArrayList<Project>) response.getObject();



                        for (int i = 1; i <= projects.size(); i++) {
                            System.out.println(i + ". " + projects.get(i - 1).getName() + " Progress: " + projects.get(i - 1).getObjective() + "/" + projects.get(i - 1).getProgress());

                        }
                        if(projects.isEmpty()){
                            System.out.print("There are no projects yet!");
                            menuLogin();
                        }else {
                            System.out.println("Choose one project to view details or 0 to go back");
                        }

                        int project = Util.readInt(projects.size());

                        if(project == 0){
                            menuLogin();
                        }else{
                            showProject(projects.get(project-1).getId());
                        }

                    }else{
                        System.out.println("There was a proble in the connection please try again");
                        menuLogin();
                    }





                } catch (Exception e){
                    handleConnectionError(e);
                    menuLogin();

                }


                break;
            case 5:
                try {
                    out.writeObject(new Action(Action.GET_USER,user.getId()));
                    User user = (User) ((Response) in.readObject()).getObject();
                    this.user = user;
                    System.out.println("Your user balance is: " + user.getBalance());

                } catch (Exception e){
                    handleConnectionError(e);
                    menuLogin();

                }

                menuLogin();



                break;
            case 6:
                try {
                    out.writeObject(new Action(Action.GET_CURRENT_REWARDS, user.getId()));

                    Response response = (Response) in.readObject();
                    if(response.isSuccess()){
                        ArrayList<CurrentRewardsResult> currentRewardsResults = (ArrayList<CurrentRewardsResult>) response.getObject();
                        System.out.println("* * * * Your rewards:");
                        for (int i = 0; i < currentRewardsResults.size(); i++) {
                            System.out.println("* * Reward description: " + currentRewardsResults.get(i).getRewardsDescription());
                            System.out.println("* * Reward value: " + currentRewardsResults.get(i).getRewardsValue());
                            System.out.println("* * Projects title: " + currentRewardsResults.get(i).getProjectsTitle());
                            System.out.println("* * Projects description: " + currentRewardsResults.get(i).getProjectsDescription());
                        }
                    }else{
                        Debug.m(response.getMessage());
                        System.out.println("You don't have any rewords");
                    }


                } catch (Exception e){
                    handleConnectionError(e);
                    menuLogin();

                }
                menuLogin();
                break;
            case 7:
                user = null;

                menu();

                break;
        }
    }


    public void login(){
        System.out.println("Please insert Username: ");
        String usernameLogin = Util.readString();
        System.out.println("Please insert Password: ");
        String passwordLogin = Util.readString();
        try {
            out.writeObject(new Action(Action.LOGIN,new User(usernameLogin,passwordLogin)));

            Response response = (Response) in.readObject();
            if(response.isSuccess()){
                System.out.println("\nLogged in!");
                this.user = (User) response.getObject();
                menuLogin();
            }else{
                System.out.println("Failed!\n" + response.getMessage());
            }
        } catch (Exception e){

            handleConnectionError(e);
            menuLogin();

        }
    }

    public void register(){
        System.out.println("Please insert Username: ");
        String username = Util.readString();
        System.out.println("Please insert Password: ");
        String password = Util.readString();

        try {
            out.writeObject(new Action(Action.SIGUP,new User(username,password)));

            Response response = (Response) in.readObject();
            if(response.isSuccess()){
                System.out.println(response.getMessage() + "\nPlease login!");

            }else{
                System.out.println("Failed!\n" + response.getMessage());
            }

        } catch (Exception e){
            handleConnectionError(e);
            menuLogin();

        }
    }

    public void showProject(int projectId){

        try {
            out.writeObject(new Action(Action.GET_PROJECT, projectId));
            Response responseProject = (Response) in.readObject();
            out.writeObject(new Action(Action.GET_LEVELS_BY_PROJECT, projectId));
            Response responseLevels = (Response) in.readObject();
            out.writeObject(new Action(Action.GET_REWARDS_BY_PROJECT, projectId));
            Response responseRewards = (Response) in.readObject();
            out.writeObject(new Action(Action.GET_POLL_BY_PROJECT, projectId));
            Response responsePolls = (Response) in.readObject();


            if(responseProject.isSuccess()){
                Project project = (Project) responseProject.getObject();
                System.out.println("Title: " + project.getName());
                System.out.println("Description: " + project.getDescription());
                System.out.println("Objective: " + project.getObjective());
                System.out.println("Limit: " + project.getLimit().toString());
                System.out.println("Progress: " + project.getProgress());

                if(responseRewards.isSuccess()){
                    ArrayList<Reward> rewards = (ArrayList<Reward>) responseRewards.getObject();
                    if(rewards.isEmpty()){
                        System.out.println("This project has no Rewards Yet");
                    }else{
                        System.out.println("* * * Rewards: ");
                        for (int i = 0; i < rewards.size(); i++) {

                            System.out.println("* * Description: " + rewards.get(i).getDescription());
                            System.out.println("* * Value: " + rewards.get(i).getValue());
                            System.out.println("* * ");
                        }
                    }
                }else{
                    Debug.m("Error getting Rewards " + responseRewards.getMessage());
                }

                if(responseLevels.isSuccess()){
                    ArrayList<Level> levels = (ArrayList<Level>) responseLevels.getObject();
                    if(levels.isEmpty()){
                        System.out.println("This project has no Levels Yet");
                    }else{
                        System.out.println("* * * Levels:");
                        for (int i = 0; i < levels.size(); i++) {

                            System.out.println("* * Description: " + levels.get(i).getDescription());
                            System.out.println("* * Goal: " + levels.get(i).getGoal());
                            System.out.println("* * ");
                        }
                    }
                }else{
                    Debug.m("Error on getting Levels: " + responseLevels.getMessage());
                }

                if(responsePolls.isSuccess()){
                    ArrayList<Poll> polls = (ArrayList<Poll>) responsePolls.getObject();
                    if(polls != null && polls.isEmpty()){
                        System.out.println("This project has no Polls Yet");
                    }else{
                        System.out.println("* * * Polls:");
                        for (int i = 0; i < polls.size(); i++) {
                            System.out.println("* * Title: " + polls.get(i).getDescription());
                            System.out.println("* * Description: " + polls.get(i).getDescription());
                            System.out.println("* * Answer a): " + polls.get(i).getAnswer1());
                            System.out.println("* * Answer a): " + polls.get(i).getAnswer2());
                            System.out.println("* * ");

                            out.writeObject(new Action(Action.GET_VOTES_BY_POLL , polls.get(i).getId()));
                            Response response = (Response) in.readObject();
                            if (response.isSuccess()){

                                PollResult pollResult = (PollResult) response.getObject();
                                System.out.println("* * Votes a): " + pollResult.getAnswerAVotes());
                                System.out.println("* * Votes b): " + pollResult.getAnswerBVotes());


                            }else{
                                Debug.m("Error on getting Votes : " + response.getMessage());

                            }
                        }
                    }
                }else{
                    Debug.m("Error on getting Polls " + responsePolls.getMessage());
                }

                if(project.getAdminId() == user.getId()){
                    System.out.println("You are the owner of the project!");
                    System.out.println("+++++++++++++++++++++++++++++++");
                    System.out.println("+ 1. Add Reward               +");
                    System.out.println("+ 2. Remove Rewards           +");
                    System.out.println("+ 3. Add Level                +");
                    System.out.println("+ 4. Add Poll                 +");
                    System.out.println("+ 5. Delete Project           +");
                    System.out.println("+ 6. View/Reply Messages      +");
                    System.out.println("+ 7. Back                     +");
                    System.out.println("+++++++++++++++++++++++++++++++");


                    int option = Util.readInt(7);

                    switch (option){
                        case 1:
                            addReward(project.getId());
                            break;
                        case 2:
                            removeReward(projectId);
                            break;
                        case 3:
                            addLevel(project.getId());
                            break;
                        case 4:
                            addPoll(projectId);
                            break;
                        case 5:
                            removeProject(projectId);
                            break;
                        case 6:
                            menuMessages(projectId);
                            break;
                        case 7:
                            menuLogin();
                            break;
                    }
                }else{
                    System.out.println("You are visiting a project from another one: ");

                    System.out.println("+++++++++++++++++++++++++++++++");
                    System.out.println("+ 1. Pledge                   +");
                    System.out.println("+ 2. Vote                     +");
                    System.out.println("+ 3. View/Reply Messages      +");
                    System.out.println("+ 4. Back                     +");
                    System.out.println("+++++++++++++++++++++++++++++++");


                    int option = Util.readInt(4);

                    switch (option){
                        case 1:
                            pledge(projectId);
                            break;
                        case 2:
                            vote(projectId);
                            break;
                        case 3:
                            menuMessages(projectId);
                            break;
                        case 4:
                            menuLogin();
                            break;
                    }


                }

            }else{
                System.out.println("Cant find project");
                menuLogin();
            }


        } catch (Exception e){
            handleConnectionError(e);
            menuLogin();

        }

    }

    private void menuMessages(int projectId) {
        try {
            out.writeObject(new Action(Action.GET_MESSAGES, projectId));
            Response response = (Response) in.readObject();
            if(response.isSuccess()){
                ArrayList<Message> messages = (ArrayList<Message>) response.getObject();
                if(!messages.isEmpty()){
                    for (int i = 0; i < messages.size(); i++) {
                        out.writeObject(new Action(Action.GET_USER, messages.get(i).getSender()));

                        User user = (User) ((Response) in.readObject()).getObject();
                        System.out.println("U: " + user.getUsername());
                        System.out.println("M: " + messages.get(i).getMessage());
                    }
                }else{
                    System.out.println("No messages yet!");
                }
            }else{
                Debug.m("Error! " + response.getMessage());
            }
            System.out.println("+++++++++++++++++++++++++++++++");
            System.out.println("+ 1. Send Message             +");
            System.out.println("+ 2. Back                     +");
            System.out.println("+++++++++++++++++++++++++++++++");
            int option = Util.readInt(2);
            switch (option){
                case 1:
                    System.out.println("Write your message");
                    String message = Util.readString();
                    out.writeObject(new Action(Action.SEND_MESSAGE, new Message(message,user.getId(),projectId)));
                    Response responseSendMessage = (Response) in.readObject();
                    if(responseSendMessage.isSuccess()){
                        System.out.println("Sended Message!!");
                        menuMessages(projectId);
                    }else{
                        System.out.println("Error sending message!! " + response.getMessage());
                    }
                    break;
                case 2:showProject(projectId);break;
            }
        } catch (Exception e){
            handleConnectionError(e);
            showProject(projectId);

        }
    }

    private void removeReward(int projectId) {

        try {
            out.writeObject(new Action(Action.GET_REWARDS_BY_PROJECT, projectId));
            Response response = (Response) in.readObject();
            if(response.isSuccess()){

                ArrayList<Reward> rewards = (ArrayList<Reward>) response.getObject();

                for (int i = 0; i < rewards.size(); i++) {
                    System.out.println(rewards.get(i).getId() + ". " + rewards.get(i).getDescription());
                }
                System.out.println("Enter reward number to remove: ");
                int option = Util.readInt();
                boolean verification = false;
                while (!verification){
                    for (int i = 0; i < rewards.size(); i++) {
                        if(option == rewards.get(i).getId()) verification = true;
                    }
                    if(!verification){
                        System.out.println("Please enter a valid reward number!!");
                        option = Util.readInt();
                    }
                }

                out.writeObject(new Action(Action.REMOVE_REWARD, option));
                Response responseRemove = (Response) in.readObject();
                if (responseRemove.isSuccess()){
                    System.out.println("Removed successfully!");
                    showProject(projectId);
                }else{
                    System.out.println("Error removing reward!" + responseRemove.getMessage());
                }

            }
        } catch (Exception e){
            handleConnectionError(e);
            showProject(projectId);

        }
    }

    private void removeProject(int projectId) {
        try {
            out.writeObject(new Action(Action.REMOVE_PROJECT, projectId));
            Response response = (Response) in.readObject();

            if(response.isSuccess()){
                System.out.println("Project removed successfully!");
                out.writeObject(new Action(Action.GET_USER, user.getId()));
                Response responseUser = (Response) in.readObject();
                if (response.isSuccess()){
                    this.user = (User) responseUser.getObject();
                }


            }else{
                Debug.m("Error deleting project" + response.getMessage());
            }
            menuLogin();

        } catch (Exception e){
            handleConnectionError(e);
            menuLogin();

        }

    }

    private void pledge(int projectId) {
        System.out.println("Insert the Pledge amount: ");
        double amount = Util.readDouble();
        try {
            out.writeObject(new Action(Action.PLEDGE, new Pledge(projectId,this.user.getId(),amount)));
            Response response = (Response) in.readObject();
            if(response.isSuccess()){
                System.out.println("Pledged successfully!");
            }
            else{
                if(response.getMessage().equals("You don't have enought money!!"))
                    System.out.println("You don't have enought money!!");
                Debug.m("Error in Pledge insertion: " +response.getMessage());
            }
        } catch (Exception e){
            handleConnectionError(e);
            showProject(projectId);

        }
        showProject(projectId);

    }

    private void vote(int projectId) {
        try {
            out.writeObject(new Action(Action.GET_POLL_BY_PROJECT, projectId));
            Response response = (Response) in.readObject();
            if (response.isSuccess()){
                ArrayList<Poll> polls = (ArrayList<Poll>) response.getObject();
                if (polls.isEmpty()){
                    System.out.println("This project don't have any poll yet!");
                    menuLogin();
                }
                for (int i = 0; i < polls.size(); i++) {
                    System.out.println(polls.get(i).getId() + ". " + polls.get(i).getTitle());
                }
                System.out.println("Enter poll number to vote: ");
                int option = Util.readInt();
                boolean verification = false;
                while (!verification){
                    for (int i = 0; i < polls.size(); i++) {
                        if(option == polls.get(i).getId()) verification = true;
                    }
                    if(!verification){
                        System.out.println("Please enter a valid poll number!!");
                        option = Util.readInt();
                    }
                }

                out.writeObject(new Action(Action.GET_POLL, option));
                Response responsePoll = (Response) in.readObject();

                if (responsePoll.isSuccess()){
                    Poll poll = (Poll) responsePoll.getObject();
                    System.out.println("Title: " + poll.getTitle());
                    System.out.println("Description: " + poll.getTitle());

                    System.out.println("Choose one answer: ");
                    System.out.println("a) :" + poll.getAnswer1());
                    System.out.println("b) :" + poll.getAnswer1());

                    ArrayList<String> options = new ArrayList<>();
                    options.add("a");
                    options.add("b");
                    String answer = Util.readStringWithOptions(options);
                    if(answer.equals("a")){
                        out.writeObject(new Action(Action.ADD_VOTE, new Vote(poll.getId(),1)));
                        Response responseVote = (Response) in.readObject();
                        if(responseVote.isSuccess()){
                            System.out.println("Voted successfully!");
                            showProject(projectId);
                        }else{
                            Debug.m(response.getMessage());
                        }
                    }else if (answer.equals("b")){
                        out.writeObject(new Action(Action.ADD_VOTE, new Vote(poll.getId(),2)));
                        Response responseVote = (Response) in.readObject();
                        if(responseVote.isSuccess()){
                            System.out.println("Voted successfully!");
                            showProject(projectId);
                        }else{
                            Debug.m(response.getMessage());
                        }
                    }else{
                        System.out.println("Error on gathering of the vote! pls vote again!");
                        showProject(projectId);
                    }


                }


            }else{
                System.out.println("Error on the conection please try again");
                showProject(projectId);
            }

        } catch (Exception e){
            handleConnectionError(e);
            showProject(projectId);

        }
    }

    private void addLevel(int projectId) {



        System.out.println("Insert goal: ");
        double goal = Util.readDouble();
        System.out.println("Insert description: ");
        String description = Util.readString();

        try {
            out.writeObject(new Action(Action.ADD_LEVEL, new Level(goal, description, projectId)));
            Response response = (Response) in.readObject();
            if(response.isSuccess()){
                System.out.println("Inserted successfully!");
                showProject(projectId);
            }else{
                Debug.m(response.getMessage());
            }
        } catch (Exception e){
            handleConnectionError(e);
            showProject(projectId);

        }

    }

    private void addReward(int projectId) {

        System.out.println("Insert description: ");
        String description = Util.readString();
        System.out.println("Insert value: ");
        double value = Util.readDouble();

        try {
            out.writeObject(new Action(Action.ADD_REWARD, new Reward(projectId, description, value)));
            Response response = (Response) in.readObject();
            if(response.isSuccess()){
                System.out.println("Inserted successfully!");
                showProject(projectId);
            }else{
                System.out.println("Erro: " + response.getMessage());
                System.out.println("Insert again!");
                addReward(projectId);
            }
        } catch (Exception e){
            handleConnectionError(e);
            showProject(projectId);

        }
    }

    public void addPoll(int projectId){


        System.out.println("Insert Title: ");
        String title = Util.readString();
        System.out.println("Insert description: ");
        String description = Util.readString();
        System.out.println("Insert answer 1");
        String answer1 = Util.readString();
        System.out.println("Insert answer 2");
        String answer2 = Util.readString();

        try {
            out.writeObject(new Action(Action.ADD_POLL, new Poll(title, description, projectId, answer1, answer2)));
            Response response = (Response) in.readObject();
            if(response.isSuccess()){
                System.out.println("Inserted successfully!");
                showProject(projectId);
            }else{
                System.out.println("Erro: " + response.getMessage());
                System.out.println("Insert again!");
                addPoll(projectId);
            }

        } catch (Exception e){
            handleConnectionError(e);
            showProject(projectId);

        }

    }

    public boolean handleConnectionError(Exception e){

        if(e instanceof SocketTimeoutException){
            //System.out.println("SocketTimeoutException");
        }else if(e instanceof EOFException){
            //System.out.println("EOFException");
        }else if(e instanceof ClassNotFoundException){
            //System.out.println("ClassNotFoundException");
        }else if(e instanceof IOException){
            //System.out.println("IOException");
        }else{
            //System.out.println("Unhandled Exception " + e.getMessage());
        }





        System.out.print("Error on the conection, reconecting...");


        boolean verification = false;

        int counter = 0;

        while (!verification){
            System.out.print(".");

            String finalHost;
            int finalPort;
            if(counter < MAX_RETRIES)
            {
                finalHost = host1;
                finalPort = port1;
            }else{
                finalHost = host2;
                finalPort = port2;
            }
            try {
                // 1o passo
                //socket.close();
                socket = new Socket();
                socket.connect(new InetSocketAddress(finalHost, finalPort), 3000);

                System.out.println("sucess!!");
                // 2o passo
                in = new ObjectInputStream(socket.getInputStream());

                out = new ObjectOutputStream(socket.getOutputStream());

                verification = true;

            } catch (UnknownHostException e1) {
                try {Thread.sleep(1000);} catch(InterruptedException ex) {Thread.currentThread().interrupt();}
                //Debug.m("Sock:" + e1.getMessage());
            } catch (EOFException e1) {
                try {Thread.sleep(1000);} catch(InterruptedException ex) {Thread.currentThread().interrupt();}
                //Debug.m("EOF:" + e1.getMessage());
            } catch (IOException e1) {
                try {Thread.sleep(1000);} catch(InterruptedException ex) {Thread.currentThread().interrupt();}
                //Debug.m("IO:" + e1.getMessage());
            }
            counter ++;
        }
        return verification;
    }


    public static void main(String args[]) {

        String ipAdress1;
        int port1;
        String ipAdress2;
        int port2;

        if(args.length == 4){
            try {
                ipAdress1 = args[0];
                port1 = Integer.parseInt(args[1]);
                ipAdress2 = args[2];
                port2 = Integer.parseInt(args[3]);
                new Client(ipAdress1, port1, ipAdress2, port2);
            }catch (NumberFormatException e) {
                System.out.println("please only numbers on the ports numbers");
                System.out.println("Client.jar -jar <ipadress of server> <port of server> <ipadress of backup server> <port of backup server>" );
            }


        }else{
            System.out.println("Client.jar -jar <ipadress of server> <port of server> <ipadress of backup server> <port of backup server>" );
        }
        //new Client(args[0]);

    }
}
