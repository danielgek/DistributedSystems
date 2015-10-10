import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by danielgek on 07/10/15.
 */
public class TCPServer {

    public TCPServer() {
    }

    public static void main(String args[]) {
        try{
            int serverPort = 6000;
            System.out.println("Listen on 6000");
            ServerSocket listenSocket = new ServerSocket(serverPort);
            while(true) {
                Socket clientSocket = listenSocket.accept();
                new ClientHandler(clientSocket);
            }
        }catch(Exception e) {
            System.out.println("Listen:" + e.getMessage());
        }
    }
}
