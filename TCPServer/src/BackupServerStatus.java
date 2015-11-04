import Util.Util;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutionException;
import Util.Debug;

/**
 * Created by danielgek on 23/10/15.
 */
public class BackupServerStatus {

    String serverIp;

    int serverPort;
    boolean primary = true;

    public BackupServerStatus(String serverIp, boolean primary) {

        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.primary = primary;

        if(primary){
            //starting server because will receive pings
            new Server();
        }else{
            //starting client because i'm the secundary server, as soon as this gives error badum start tcp :D
            new Client(serverIp);
        }

    }

    public boolean isBackup() {
        return primary;
    }

    private class Server extends Thread {
        private static final int BUFFER_SIZE = 1024;
        DatagramSocket datagramSocket;
        byte[] receiveData = new byte[BUFFER_SIZE];

        public Server() {
           this.start();
        }

        @Override
        public void run() {
            super.run();


            try {
                datagramSocket = new DatagramSocket(43000);
                while(true){

                    DatagramPacket inDatagramPacket = new DatagramPacket(receiveData, BUFFER_SIZE);
                    //whi
                    //datagramSocket.setSoTimeout(1000);
                    datagramSocket.receive(inDatagramPacket);

                    InetAddress clientAddress = inDatagramPacket.getAddress();
                    int clientPort = inDatagramPacket.getPort();

                    System.out.println("Received ping: from backup server");

                    String outMessagae = "Don't turn on !!";

                    DatagramPacket outDatagramPacket= new DatagramPacket(outMessagae.getBytes(), outMessagae.length(), clientAddress, clientPort);

                    datagramSocket.send(outDatagramPacket);
                }

            } catch (SocketException e) {
                Debug.m(e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                Debug.m(e.getMessage());
                e.printStackTrace();
            } finally {
                datagramSocket.close();
            }
        }
    }

    private class Client extends Thread{
        private static final int BUFFER_SIZE = 1024;
        DatagramSocket datagramSocket;
        byte[] receiveData = new byte[BUFFER_SIZE];
        int failedTryCounter;
        private String address;

        public Client(String address) {

            this.address = address;
            failedTryCounter = 0;
            this.start();
        }

        @Override
        public void run() {
            super.run();



            while (!primary){
                if(failedTryCounter >= 5){
                    primary = true;
                    System.out.println("primary=true");

                }
                try {


                    InetAddress inetAddress = InetAddress.getByName(this.address);

                    String data = "Making a ping";

                    datagramSocket = new DatagramSocket();

                    DatagramPacket outDatagramPacket = new DatagramPacket(data.getBytes(), data.length(), inetAddress, 430000);

                    datagramSocket.setSoTimeout(1000*6);//6 secs
                    datagramSocket.send(outDatagramPacket);
                    Debug.m("Sended ping");


                    DatagramPacket inDatagramPacket = new DatagramPacket(receiveData, BUFFER_SIZE);
                    datagramSocket.receive(inDatagramPacket);
                    Debug.m("Receiver response");


                    String response = new String(inDatagramPacket.getData(), 0, inDatagramPacket.getLength());

                    System.out.println(response);
                    try {Thread.sleep(1000);} catch(InterruptedException ex) {Thread.currentThread().interrupt();}



                } catch (SocketException e) {

                    failedTryCounter++;
                    try {Thread.sleep(1000);} catch(InterruptedException ex) {Thread.currentThread().interrupt();}

                    Debug.m(e.getMessage());
                    e.printStackTrace();
                } catch (UnknownHostException e) {

                    failedTryCounter++;
                    try {Thread.sleep(1000);} catch(InterruptedException ex) {Thread.currentThread().interrupt();}

                    Debug.m(e.getMessage());
                    //e.printStackTrace();
                } catch (SocketTimeoutException e) {

                    failedTryCounter++;
                    try {Thread.sleep(1000);} catch(InterruptedException ex) {Thread.currentThread().interrupt();}

                    System.out.println("Timeout!!");

                    Debug.m("Exeption " + e.getMessage());
                    //e.printStackTrace();
                } catch (IOException e) {

                    failedTryCounter++;
                    try {Thread.sleep(1000);} catch(InterruptedException ex) {Thread.currentThread().interrupt();}

                    Debug.m(e.getMessage());
                    //e.printStackTrace();
                } finally {
                    datagramSocket.close();
                }
            }
        }

    }
}
