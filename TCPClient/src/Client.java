import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import models.User;

/**
 * Created by danielgek on 07/10/15.
 */
public class Client {
    private  Socket socket = null;
    private int serversocket = 6000;
    private ObjectInputStream in;
    private ObjectOutputStream out;

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
                    String username = scanner.nextLine();
                    System.out.println("Please insert Password: ");
                    String password = scanner.nextLine();
                    try {
                        out.writeObject(new User(username,password));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

    }



    public static void main(String args[]) {
        //new Client(args[0]);
        new Client("localhost");
    }
}
