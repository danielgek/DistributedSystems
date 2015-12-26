package rmi;


import models.Pledge;
import models.Project;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by danielgek on 30/10/15.
 */
public class EndProjectTask extends Thread {

    private Connection connection;
    private Statement statement = null;
    private ResultSet rs = null;

    public EndProjectTask() {

        this.start();
    }

    @Override
    public void run() {
        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sd", "root", "secret");
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        while (true){


            try {
                rs = statement.executeQuery("SELECT * FROM projects WHERE soft_deleted = 0 AND closed = 0 AND limite < NOW()");

                ArrayList<Project> projects = new ArrayList<>();

                while (rs.next()){
                    projects.add(new Project(
                            rs.getInt(1),
                            rs.getInt(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getDouble(5),
                            rs.getDate(6)));
                }

                if (!projects.isEmpty()){
                    //Statement statementPlegdes = connection.createStatement();
                    //ResultSet resultSetPlegdes =


                    for (int i = 0; i < projects.size(); i++) {
                        ArrayList<Pledge> pledges = new ArrayList<>();

                        rs = statement.executeQuery("SELECT  * FROM pledges WHERE id_project = " + projects.get(i).getId());
                        while (rs.next()){
                            pledges.add(new Pledge(rs.getInt("id"),rs.getInt("id_project"), rs.getInt("id_user") ,rs.getDouble("amount")));
                        }




                        if (projects.get(i).reatched()){
                            double sum = 0;
                            for (int j = 0; j < pledges.size(); j++) {
                                sum += pledges.get(i).getAmount();
                            }
                            //meter dinheiro para o proprietario do projecto
                            statement.executeUpdate("UPDATE users SET balance = balance + " + sum + " WHERE id = " + projects.get(i).getAdminId());

                        }else{

                            String query = "DELETE FROM pledges WHERE ";
                            for (int j = 0; j < pledges.size(); j++) {
                                if(j == pledges.size() - 1 ){
                                    query += "id = " + pledges.get(j).getId();
                                }else{
                                    query += "id = " + pledges.get(j).getId() + " OR ";
                                }
                                statement.executeUpdate("UPDATE users SET balance = balance + " + pledges.get(j).getAmount() + " WHERE id = " + pledges.get(i).getUserId());

                            }

                            System.out.println(query);
                            statement.execute(query);

                        }
                        statement.executeUpdate("UPDATE projects SET closed = 1 WHERE id = " + projects.get(i).getId());
                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000*60*60);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
