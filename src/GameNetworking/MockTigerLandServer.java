package GameNetworking;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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
        try {

            System.out.println("Waiting for client on port " +
                    serverSocket.getLocalPort() + "...");
            Socket server = serverSocket.accept();
            System.out.println("Just connected to " + server.getRemoteSocketAddress());

            BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            PrintWriter out = new PrintWriter(server.getOutputStream(), true);
            while(true) {
                System.out.println("Start sending out messages...");
                String opponentId = "5678";
                String assignedPlayerId = "1234";
                int rounds = 1;
                int challengeID = 1;
                int GID = 1;
                int score = 1;
                int number = 1;
                int time_move = 1;

                out.println("WELCOME TO ANOTHER EDITION OF THUNDERDOME!");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Read in Tournament Password
                String message = in.readLine();
                System.out.println(message);
                out.println("TWO SHALL ENTER ONE SHALL LEAVE");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Read in Team Username and Password
                message = in.readLine();
                System.out.println("Echo received: " + message);
                out.println("WAIT FOR THE TOURNAMENT TO BEGIN " + assignedPlayerId);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Challenge Protocol
                out.println("NEW CHALLENGE " + challengeID + " YOU WILL PLAY " + rounds + " MATCHES");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Round Protocol
                for(int RID = 1; RID <= rounds; RID ++) {
                    out.println("BEGIN ROUND " + RID + " OF " + rounds);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //Match protocol
                    out.println("NEW MATCH BEGINNING NOW YOUR OPPONENT IS PLAYER " + opponentId);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    boolean flag1 = false, flag2 = false, flag3 = false;
                    for(int moveNumb = 1; (!flag1 || !flag2 || !flag3) && moveNumb <= 48; moveNumb++) {
                        if(!flag2) {
                            String tile = randomHex() + "+" + randomHex();
                            out.println("MAKE YOUR MOVE IN GAME " + GID + " WITHIN " + time_move + " SECOND: MOVE " + moveNumb + " PLACE " + tile);
                        } else{
                            out.println("GAME 1 OVER PLAYER 1234 100 PLAYER 5678 99");
                            break;
                        }

                        message = in.readLine();
                        System.out.println("Echo received: " + message);

                        if(!flag1){
                            out.println("GAME 1 MOVE 2 PLAYER 5678 PLACED AT 1 1 1 1 FOUNDED SETTLEMENT AT 0 1 -1");
                            flag1 = true;
                        } else{
                            if(!flag2){
                                out.println("GAME 1 MOVE 2 PLAYER 5678 FORFEITED: ILLEGAL TILE PLACEMENT");
                                flag2 = true;
                            }
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    //end of game
                    out.println("GAME " + GID + " OVER PLAYER " + assignedPlayerId + " " + score + " PLAYER " + opponentId + " " + score);

                    out.println("END OF ROUND " + RID + " OF " + rounds);
                }

                out.println("END OF CHALLENGES");
//                out.println("WAIT FOR THE NEXT CHALLENGE TO BEGIN");
                out.println("THANK YOU FOR PLAYING! GOODBYE");

            }
//                server.close();
        }
        catch(SocketTimeoutException s) {
            System.out.println("Socket timed out!");
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    private String randomHex(){
        String result = "";
        int randomInt = ((int)( Math.random() * 40)) / 10;
        switch (randomInt){
            case 0:
                result = "GRASS"; break;
            case 1:
                result = "ROCK"; break;
            case 2:
                result = "JUNGLE"; break;
            case 3:
                result = "LAKE"; break;
        }
        return result;
    }
}


/**
 ///ADD MORE OF THE MOVES HERE
 out.println("GAME " + GID + " MOVE " + number + " PLAYER " + PID + " FORFEITED: ILLEGAL TILE PLACEMENT");
 try {
 Thread.sleep(1000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 out.println("GAME " + GID + " MOVE " + number + " PLAYER " + PID + " FORFEITED: ILLEGAL BUILD");
 try {
 Thread.sleep(1000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 out.println("GAME " + GID + " MOVE " + number + " PLAYER " + PID + " FORFEITED: TIMEOUT");
 try {
 Thread.sleep(1000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 out.println("GAME " + GID + " MOVE " + number + " PLAYER " + PID + " LOST: UNABLE TO BUILD");
 try {
 Thread.sleep(1000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

 */