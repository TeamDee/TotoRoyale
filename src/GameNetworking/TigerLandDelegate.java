package GameNetworking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;

/**
 * Created by Z_K on 4/1/2017.
 */
public class TigerLandDelegate {
    private boolean gameEnded;
    private TigerLandClient client;
    private int playerId;
    private int challengeId;
    private int gameId;
    private int numberOfRounds;
    private String tournamentPW;
    private String username;
    private String password;
    private boolean unexpectedError;

    public TigerLandDelegate(String serverName, int port){
        client = new TigerLandClient(serverName, port);
        tournamentPW = "password";
        username = "TeamD";
        password = "Password";
        gameEnded = false;
        unexpectedError = false;
        System.out.println("Game Delegate successfully created.");
    }

    public void TournamentProtocol(){
        String serverMessage = "";
        DataInputStream dataInputStream = client.getDataInputStream();
        DataOutputStream dataOutputStream = client.getDataOutputStream();

        try {
            do{
                serverMessage = dataInputStream.readUTF();
                System.out.println("SERVER: " + serverMessage);
            } while(!FrequentlyUsedPatterns.WelcomeMssgPattern.matcher(serverMessage).matches());

            AuthenticationProtocol(dataInputStream, dataOutputStream);

            ChallengeProtocol(dataInputStream, dataOutputStream);

            //The exit message
            serverMessage = dataInputStream.readUTF();
            System.out.println("SERVER: " + serverMessage);
        }catch(IOException ex){
            System.out.println("Failed to obtain message from server.");
        }
    }

    public void AuthenticationProtocol(DataInputStream in, DataOutputStream out){
        String outMessage, inMessage;
        try {
            outMessage = "ENTER THUNDERDOME " + tournamentPW;
            out.writeUTF(outMessage);
            System.out.println("Client: " + outMessage);

            inMessage = in.readUTF();
            System.out.println("SERVER: " + inMessage);

            outMessage = "I AM " + username + " " + password;
            out.writeUTF(outMessage);
            System.out.println("Client: " + outMessage);

            inMessage = in.readUTF();
            System.out.println("SERVER: " + inMessage);
            Matcher PlayerIDMatcher = FrequentlyUsedPatterns.PlayerIDMssgPattern.matcher(inMessage);
            if(PlayerIDMatcher.matches())
                playerId = Integer.parseInt(PlayerIDMatcher.group(1));
            else
                throw new IOException();
        }catch(IOException ex){
            System.out.println("Failed to AuthenticationProtocol with server.");

        }
    }

    public void ChallengeProtocol(DataInputStream in, DataOutputStream out){
        String serverMessage = "";
        boolean nextGame = true;
        try {
            while(nextGame) {
                serverMessage = in.readUTF();
                System.out.println("SERVER: " + serverMessage);
                Matcher ChallengeMatcher = FrequentlyUsedPatterns.ChallengeMssgPattern.matcher(serverMessage);
                if (ChallengeMatcher.matches()) {
                    challengeId = Integer.parseInt(ChallengeMatcher.group(1));
                    numberOfRounds = Integer.parseInt(ChallengeMatcher.group(2));
                }
                for (int i = 1; i <= numberOfRounds; i++) {
                    RoundProtocol(in, out);
                }
                serverMessage = in.readUTF();
                System.out.println("SERVER: " + serverMessage);
                if (FrequentlyUsedPatterns.ChallengeOverMssgPattern.matcher(serverMessage).matches()) {
                    nextGame = false;
                }
            }
        }catch(IOException ex){
            System.out.println("Failed to obtain message from server.");
        }
    }

    public void RoundProtocol(DataInputStream in, DataOutputStream out){
        String serverMessage = "";
        try{
            serverMessage = in.readUTF();
            System.out.println("SERVER: " + serverMessage);

            if(FrequentlyUsedPatterns.RoundBeginMssgPattern.matcher(serverMessage).matches()){
                do{
                    MatchProtocol(in, out);
                    serverMessage = in.readUTF();
                    System.out.println("SERVER: " + serverMessage);
                } while(FrequentlyUsedPatterns.RoundEndNextMssgPattern.matcher(serverMessage).matches());
            }
        } catch (IOException ex){
            System.out.println("Failed to obtain message from server.");
        }
    }

    public void MatchProtocol(DataInputStream in, DataOutputStream out){
        String serverMessage = "";
        try{
            serverMessage = in.readUTF();
            System.out.println("SERVER: " + serverMessage);


            MoveProtocol(in, out);


        }catch (IOException ex){
            System.out.println("Failed to obtain message from server.");
        }
    }

    public void MoveProtocol(DataInputStream in, DataOutputStream out){
        String serverMessage = "";
        boolean gameOneOver = false;
        boolean gameTwoOver = false;
        //TODO: start Game1 and start Game2
        for(int i=0; (!gameOneOver || !gameTwoOver) && i<48; i++){
            //TODO: take server assignment and make move
        }
    }
}
