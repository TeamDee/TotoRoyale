package GameNetworking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by Z_K on 4/1/2017.
 */
public class MockTigerLandServer extends Thread{
    private ServerSocket serverSocket;

    public MockTigerLandServer(int port){
        try{
            System.out.println("Attempting to start a server socket on port " + port + " ...");
            serverSocket = new ServerSocket(6066);
            System.out.println("Server socket successfully established.");
        } catch(IOException ex){
            System.out.println("Cannot start a server socket!");
        }
    }

    public void run(){
        while(true) {
            try {

                System.out.println("Waiting for client on port " +
                        serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();
                System.out.println("Just connected to " + server.getRemoteSocketAddress());

                DataInputStream in = new DataInputStream(server.getInputStream());
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                while(true) {
                    String username = null;
                    String serverusername = null;
                    int rounds = 2;
                    int challengeID = 1;
                    int RID = 1;
                    int GID = 1;
                    int PID = 1;
                    int score = 1;
                    int number = 1;
                    int time_move = 1;
                    String message = in.readUTF();
                    System.out.println(message);
                    out.writeUTF("WELCOME TO ANOTHER EDITION OF THUNDERDOME!");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("TWO SHALL ENTER ONE SHALL LEAVE");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    out.writeUTF("Echo received: " + message);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    out.writeUTF("WAIT FOR THE TOURNAMENT TO BEGIN " + serverusername);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    out.writeUTF("NEW CHALLENGE" + challengeID + "YOU WILL PLAY" + rounds + "MATCHES");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    out.writeUTF("BEGIN ROUND" + RID + "OF" + rounds);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    out.writeUTF("NEW MATCH BEGINNING NOW YOUR OPPONENT IS PLAYER" + username);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    out.writeUTF("MAKE YOUR MOVE IN GAME" + GID + "WITHIN" + time_move + "SECOND:MOVE");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ///ADD MORE OF THE MOVES HERE
                    out.writeUTF("GAME" + GID + "MOVE" + number + "PLAYER" + PID + "FORFEITED: ILLEGAL TILE PLACEMENT");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    out.writeUTF("GAME" + GID + "MOVE" + number + "PLAYER" + PID + "FORFEITED: ILLEGAL BUILD");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    out.writeUTF("GAME" + GID + "MOVE" + number + "PLAYER" + PID + "FORFEITED: TIMEOUT");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    out.writeUTF("GAME" + GID + "MOVE" + number + "PLAYER" + PID + "LOST: UNABLE TO BUILD");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //end of game
                    out.writeUTF("GAME" + GID + "OVER PLAYER" + PID + score + "PLAYER" + PID + score);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    out.writeUTF("END OF ROUND" + RID + "OF" + rounds);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    System.out.println("END OF CHALLENGES");
                    System.out.println("WAIT FOR THE NEXT CHALLENGE TO BEGIN");
                    System.out.println("THANK YOU FOR PLAYING! GOODBYE");

                }
//                server.close();
            }
            catch(SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            }
            catch(IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

}
