
import models.Response;
import models.User;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by danielgek on 07/10/15.
 */
public class ClientHandler extends Thread {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;


    public ClientHandler(Socket socket) {
        this.socket = socket;
        try{
            this.out = new ObjectOutputStream(this.socket.getOutputStream());
            this.in = new ObjectInputStream(this.socket.getInputStream());
        }catch (IOException e){
            System.out.println("Error on Client Contructor:" + e);
        }

        this.start();
    }


    public void run() {

        try{
            while(true){
                User user = (User) in.readObject();
                Response response = new RequestHandler().register(user);
                out.writeObject(response);
                /*if(response.isSuccess()){
                    out.writeObject(new Response(true, "SignedUp with success!"));
                }else{
                    out.writeObject(new Response(true, "SignedUp with success!"));
                }

                out.writeUTF(response);*/
            }
        }catch(EOFException e){
            System.out.println("Error on Client run method EOF:" + e.getMessage());
        }catch(IOException e){
            System.out.println("Error on Client run method IO:" + e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
