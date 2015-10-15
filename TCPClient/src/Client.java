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
import models.Project;
import models.Response;
import models.User;


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
            Response response = (Response) in.readObject();

            if(response.isSuccess()){
                Project project = (Project) response.getObject();
                System.out.println("Title: " + project.getName());
                System.out.println("Description: " + project.getDescription());
                System.out.println("Objective: " + project.getObjective());
                System.out.println("Limit: " + project.getLimit().toString());

                if(project.getAdminId() == user.getId()){
                    System.out.println("You are the owner of the project!");
                    System.out.println("+++++++++++++++++++++++++++++++");
                    System.out.println("+ 1. Add            +");
                    System.out.println("+ 2. View My Projects         +");
                    System.out.println("+ 3. View Projects            +");
                    System.out.println("+++++++++++++++++++++++++++++++");

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


    public static void main(String args[]) {
        //new Client(args[0]);
        new Client("localhost");
    }
}
