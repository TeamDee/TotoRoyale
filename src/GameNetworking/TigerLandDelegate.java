package GameNetworking;

import GameEngine.GameLogicDirector;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.regex.Matcher;

/**
 * Created by Z_K on 4/1/2017.
 */
public class TigerLandDelegate {
    private boolean gameEnded;
    private TigerLandClient client;
    private int playerId;
    private String tournamentPW;
    private String username;
    private String password;
    private GameLogicDirector game1;
    private GameLogicDirector game2;
    private int game1Id;
    private int game2Id;
    private String unexpectedError;
    private Queue<ArrayList<RoundStats>> gameStats;

    public TigerLandDelegate(String serverName, int port){
        client = new TigerLandClient(serverName, port);
        tournamentPW = "password";
        username = "TeamD";
        password = "Password";
        gameEnded = false;
        unexpectedError = "";
        System.out.println("Game Delegate successfully created.");
    }

    public void TournamentProtocol(){
        String serverMessage = "";
        DataInputStream dataInputStream = client.getDataInputStream();
        DataOutputStream dataOutputStream = client.getDataOutputStream();
        boolean authenticated = false;
        try {
            do{
                serverMessage = dataInputStream.readUTF();
                System.out.println("SERVER: " + serverMessage);
            } while(!FrequentlyUsedPatterns.WelcomeMssgPattern.matcher(serverMessage).matches());

            while(!authenticated) {
                authenticated = AuthenticationProtocol(dataInputStream, dataOutputStream);
            }

            ChallengeProtocol(dataInputStream, dataOutputStream);

            //The exit message
            serverMessage = dataInputStream.readUTF();
            System.out.println("SERVER: " + serverMessage);
        }catch(IOException ex){
            unexpectedError = "TournamentProtocol: " + ex.getMessage();
            System.out.println("Failed to obtain message from server.");
        }
    }

    public boolean AuthenticationProtocol(DataInputStream in, DataOutputStream out){
        String outMessage, inMessage;
        boolean authenticated = false;
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
            if(PlayerIDMatcher.matches()) {
                playerId = Integer.parseInt(PlayerIDMatcher.group(1));
                authenticated = true;
            }
            else
                throw new IOException();
            System.out.println("Delegate: Authentication is successful!");
        }catch(IOException ex){
            unexpectedError = "AuthenticationProtocol: " + ex.getMessage();
            System.out.println("Failed to AuthenticationProtocol with server.");
        }
        return authenticated;
    }

    public void ChallengeProtocol(DataInputStream in, DataOutputStream out){
        String serverMessage = "";
        boolean nextGame = true;
        int challengeId, numberOfRounds=0;
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
            System.out.println("Delegate: End of Challenge Protocol!");
        }catch(IOException ex){
            unexpectedError = "ChallengeProtocol: " + ex.getMessage();
            System.out.println("Failed to obtain message from server.");
        }
    }

    public void RoundProtocol(DataInputStream in, DataOutputStream out){
        String serverMessage = "";
        try{
            serverMessage = in.readUTF();
            System.out.println("SERVER: " + serverMessage);

            if(FrequentlyUsedPatterns.RoundBeginMssgPattern.matcher(serverMessage).matches()){
                MockMatchProtocol(in, out);
                serverMessage = in.readUTF();
                System.out.println("SERVER: " + serverMessage);
            }
            System.out.println("Delegate: End of Round Protocol!");
        } catch (IOException ex){
            unexpectedError = "RoundProtocol: " + ex.getMessage();
            System.out.println("Failed to obtain message from server.");
        }
    }

    public void MockMatchProtocol(DataInputStream in, DataOutputStream out){
        String serverMessage = "", clientMessage = "";
        int opponentId;
        try{
            //match start message
            serverMessage = in.readUTF();
            System.out.println("SERVER: " + serverMessage);
            Matcher NewMatchMatcher = FrequentlyUsedPatterns.NewMatchMssgPattern.matcher(serverMessage);
            if(NewMatchMatcher.matches()) {
                opponentId = Integer.parseInt(NewMatchMatcher.group(1));
                game1 = new GameLogicDirector(playerId, opponentId);

                game1Id = -1;

                game1.begin();

                for (int i = 0; (!game1.isGameOver()) && i < 48; i++) {
                    MoveProtocol(in, out);
                }
                //game1 score
                serverMessage = in.readUTF();
                System.out.println("SERVER: " + serverMessage);
            }
            System.out.println("Delegate: End of Match Protocol!");
        }catch (IOException ex){
            unexpectedError = "MatchProtocol: " + ex.getMessage();
            System.out.println("Failed to obtain message from server.");
        }
    }

    public void MatchProtocol(DataInputStream in, DataOutputStream out){
        String serverMessage = "", clientMessage = "";
        int opponentId = -1;

        try{
            //match start message
            serverMessage = in.readUTF();
            System.out.println("SERVER: " + serverMessage);
            Matcher NewMatchMatcher = FrequentlyUsedPatterns.NewMatchMssgPattern.matcher(serverMessage);
            if(NewMatchMatcher.matches()) {
                opponentId = Integer.parseInt(NewMatchMatcher.group(1));
                game1 = new GameLogicDirector(playerId, opponentId);
                game2 = new GameLogicDirector(opponentId, playerId);
                game1Id = -1;
                game2Id = -1;

                game1.begin();
                game2.begin();

                for (int i = 0; (!game1.isGameOver() || !game2.isGameOver()) && i < 48; i++) {
                    MoveProtocol(in, out);
                }
                //game1 score
                serverMessage = in.readUTF();
                System.out.println("SERVER: " + serverMessage);
                //game2 score
                serverMessage = in.readUTF();
                System.out.println("SERVER: " + serverMessage);
            }
            System.out.println("Delegate: End of Match Protocol!");
        }catch (IOException ex){
            unexpectedError = "MatchProtocol: " + ex.getMessage();
            System.out.println("Failed to obtain message from server.");
        }
    }

    public void MoveProtocol(DataInputStream in, DataOutputStream out){
        String serverMessage = "", clientMessage = "", placedAndBuildMssg = "";
        int gameId, moveNumber, pId;
        String tileAssigned;
        int messageCountExpeted = 1;

        try{
            while(messageCountExpeted>0){
                serverMessage = in.readUTF();
                System.out.println("SERVER: " + serverMessage);
                messageCountExpeted--;
                Matcher serverPromptMatcher = FrequentlyUsedPatterns.MoveServerPromptMssgPattern.matcher(serverMessage);
                Matcher gameMovePlayerMatcher = FrequentlyUsedPatterns.GameMovePlayerMssgPattern.matcher(serverMessage);
                Matcher gameForfeitedMatcher = FrequentlyUsedPatterns.GameForfeitedMssgPattern.matcher(serverMessage);
                Matcher gameLostMatcher = FrequentlyUsedPatterns.GameLostMssgPattern.matcher(serverMessage);
                if(serverPromptMatcher.matches()){
                    gameId = Integer.parseInt(serverPromptMatcher.group(1));
                    if(game1Id == -1){
                        game1Id = gameId;
                    } else if(game2Id == -1){
                        game2Id = gameId;
                    }
                    moveNumber = Integer.parseInt(serverPromptMatcher.group(3));
                    tileAssigned = serverPromptMatcher.group(4);

                    //palceAndBuildMessage from Our AI's action
                    if(gameId == game1Id){
                        placedAndBuildMssg = game1.tournamentMove(tileAssigned);
                    } else{
                        placedAndBuildMssg = game2.tournamentMove(tileAssigned);
                    }

                    clientMessage = "GAME " + gameId + " MOVE " + moveNumber + " PLAYER " + playerId + " ";
                    clientMessage += placedAndBuildMssg;
                    out.writeUTF(clientMessage);
                    System.out.println("Client: " + clientMessage);

                } else if(gameMovePlayerMatcher.matches()){
                    //TODO: add function to carry out opponent's move
                    gameId = Integer.parseInt(gameMovePlayerMatcher.group(1));
                    pId = Integer.parseInt(gameMovePlayerMatcher.group(3));
                    String opponentMoveMssg = gameMovePlayerMatcher.group(4);
                    if(pId != playerId) {
                        if (gameId == game1Id) {
                            game1.opponentPlayerMove(opponentMoveMssg);
                        } else {
                            game2.opponentPlayerMove(opponentMoveMssg);
                        }
                    }
                } else if(gameForfeitedMatcher.matches()){
                    gameId = Integer.parseInt(gameMovePlayerMatcher.group(1));
                    pId = Integer.parseInt(gameMovePlayerMatcher.group(3));
                    String opponentForfeitedMssg = gameMovePlayerMatcher.group(4);
                    if (gameId == game1Id) {
                        game1.setGameOver();
                    } else {
                        game2.setGameOver();
                    }
                } else if(gameLostMatcher.matches()){
                    gameId = Integer.parseInt(gameMovePlayerMatcher.group(1));
                    pId = Integer.parseInt(gameMovePlayerMatcher.group(3));
                    String opponentLostMssg = gameMovePlayerMatcher.group(4);
                    if (gameId == game1Id) {
                        game1.setGameOver();
                    } else {
                        game2.setGameOver();
                    }
                }
            }
            System.out.println("Delegate: End of Move Protocol!");
        } catch (IOException ex){
            unexpectedError = "MoveProtocol: " + ex.getMessage();
            System.out.println("Failed to obtain message from server.");
        }
    }
}
