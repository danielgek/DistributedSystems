import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private int serversocket = 6000;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private User user;

    public Client(String host) {


        try {
            // 1o passo
            socket = new Socket(host, serversocket);

            System.out.println("Ligado ao servidor TCP" + socket);
            // 2o passo
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());



        } catch (UnknownHostException e) {
            System.out.println("Sock:" + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage());
        }
        menu();
    }

    private void menu(){
        System.out.println("+++++++++++++++++++++++++++++++");
        System.out.println("+ 1. Sigup                    +");
        System.out.println("+ 2. Login                    +");
        System.out.println("+++++++++++++++++++++++++++++++");



        Scanner scanner = new Scanner(System.in);

        while(scanner.hasNextInt()){
            switch (scanner.nextInt()){
                case 1:
                    System.out.println("Please insert Username: ");
                    String username = Util.readString();
                    System.out.println("Please insert Password: ");
                    String password = Util.readString();

                    try {
                        Debug.m("writing action");
                        out.writeObject(new Action(Action.SIGUP,new User(username,password)));

                        Response response = (Response) in.readObject();
                        if(response.isSuccess()){
                            System.out.println(response.getMessage() + "\nPlease login!");

                        }else{
                            System.out.println("Failed!\n" + response.getMessage());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    menu();
                    break;
                case 2:
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
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }




    }

    private void menuLogin(){

        System.out.println("+++++++++++++++++++++++++++++++");
        System.out.println("+ 1. Create Project           +");
        System.out.println("+ 2. View My Projects         +");
        System.out.println("+ 3. View Projects            +");
        System.out.println("+++++++++++++++++++++++++++++++");

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextInt()){
            switch (scanner.nextInt()){
                case 1 :


                    System.out.println("Insert project title: ");
                    String title = Util.readString();
                    System.out.println("Insert project description: ");
                    String description = Util.readString();
                    System.out.println("Insert project objective: ");
                    double objective = scanner.nextDouble();
                    System.out.println("Insert project final date(dd/mm/yyyy): ");

                    boolean dateCheck = true;
                    Date limit = null;
                    while (dateCheck){
                        try {
                            limit = new Date(Util.readString());
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
                    } catch (Exception e) {
                        Debug.m("--"+e);
                        e.printStackTrace();
                    }

                    try {
                        Response response = (Response) in.readObject();
                    } catch (IOException e) {
                        Debug.m(e.getMessage());
                    } catch (ClassNotFoundException e) {
                        Debug.m(e.getMessage());
                    }




                    break;
                case 2 :
                    try {
                        out.writeObject(new Action(Action.GET_PROJECT_BY_ADMIN, user.getId()));

                        Response response = (Response) in.readObject();

                        ArrayList<Project> projects = (ArrayList<Project>) response.getObject();



                        for (int i = 1; i <= projects.size(); i++) {
                            System.out.println(i + ". " + projects.get(i-1).getName());

                        }
                        if(projects.isEmpty()){
                            System.out.print("You don't have any projects yet!");
                            menuLogin();
                        }else {
                            System.out.println("Choose one project to view details or 0 to go back");
                        }

                        int option = Util.readInt(projects.size());

                        if(option == 0){
                            menuLogin();
                        }else{
                            showProject(projects.get(option-1).getId());
                        }
                        


                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }


                    break;
                case 3 :
                    try {
                        out.writeObject(new Action(Action.GET_PROJECTS));

                        Response response = (Response) in.readObject();

                        ArrayList<Project> projects = (ArrayList<Project>) response.getObject();



                        for (int i = 1; i <= projects.size(); i++) {
                            System.out.println(i + ". " + projects.get(i-1).getName());

                        }
                        if(projects.isEmpty()){
                            System.out.print("There are no projects yet!");
                            menuLogin();
                        }else {
                            System.out.println("Choose one project to view details or 0 to go back");
                        }

                        int option = Util.readInt(projects.size());

                        if(option == 0){
                            menuLogin();
                        }else{
                            showProject(projects.get(option-1).getId());
                        }



                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }


                    break;
            }
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
                    System.out.println("+ 2. Add Level                +");
                    System.out.println("+ 3. Add Poll                 +");
                    System.out.println("+ 4. Vote                     +");
                    System.out.println("+ 5. Back                     +");
                    System.out.println("+++++++++++++++++++++++++++++++");


                    int option = Util.readInt(3);

                    switch (option){
                        case 1:
                            addReward(project.getId());
                            break;
                        case 2:
                            addLevel(project.getId());
                            break;
                        case 3:
                            addPoll(projectId);
                            break;
                        case 4:
                            vote(projectId);
                            break;
                        case 5:
                            menuLogin();
                            break;
                    }



                }
            }else{
                System.out.println("Cant find project");
                menuLogin();
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

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


            }else{}

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
        } catch (IOException e) {
            Debug.m(e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Debug.m(e.getMessage());
            e.printStackTrace();
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
        } catch (IOException e) {
            Debug.m(e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Debug.m(e.getMessage());
            e.printStackTrace();
        }

    }


    public static void main(String args[]) {
        //new Client(args[0]);
        new Client("localhost");
    }
}
