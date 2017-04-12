package GameNetworking;

import GameEngine.GameLogicDirector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.Matcher;

/**
 * Created by Z_K on 4/1/2017.
 */
public class TigerLandDelegate {
    private boolean gameEnded;
    private TigerLandClient client;
    private int playerId, opponentId;
    private String tournamentPW, username, password;
    private GameLogicDirector game1, game2;
    private String game1Id, game2Id;
    private String unexpectedError;

    public TigerLandDelegate(){
        String serverName;
        int port;
        Scanner in = new Scanner(System.in);
        System.out.println("What is the serverName?");
        serverName = in.nextLine();
        System.out.println("What is the port number?");
        port = Integer.parseInt(in.nextLine());
        System.out.println("Enter in order: TournamentPassword, Username, Password");
        tournamentPW = in.next(); username = in.next(); password = in.next();
        client = new TigerLandClient(serverName, port);
//        tournamentPW = "FurRealz";
//        username = "D";
//        password = "D";
        gameEnded = false;
        unexpectedError = "";
        System.out.println("Game Delegate successfully created.");
    }

    public TigerLandClient getClient() { return client; }

    public void TournamentProtocol(){
        String serverMessage = "";
        BufferedReader dataInputStream = client.getDataInputStream();
        PrintWriter dataOutputStream = client.getDataOutputStream();
        boolean authenticated = false;
        try {
            do{
                // WELCOME TO ANOTHER EDITION OF THUNDERDOME!
                serverMessage = dataInputStream.readLine();
                System.out.println("SERVER(Tournament): " + serverMessage);
            } while(!FrequentlyUsedPatterns.WelcomeMssgPattern.matcher(serverMessage).matches());

            while(!authenticated) {
                authenticated = AuthenticationProtocol(dataInputStream, dataOutputStream);
            }

            ChallengeProtocol(dataInputStream, dataOutputStream);

            //The exit message
            serverMessage = dataInputStream.readLine();
            System.out.println("SERVER(Tournament): " + serverMessage);
        }catch(IOException ex){
            unexpectedError = "TournamentProtocol: " + ex.getMessage();
            System.out.println("Tournament: Failed to obtain message from server.");
        }
    }

    public boolean AuthenticationProtocol(BufferedReader in, PrintWriter out){
        String outMessage, inMessage;
        boolean authenticated = false;
        try {
            // ENTER THUNDERDOME <tournament password>
            outMessage = "ENTER THUNDERDOME " + tournamentPW;
            out.println(outMessage);
            System.out.println("Client: " + outMessage);

            // TWO SHALL ENTER, ONE SHALL LEAVE
            inMessage = in.readLine();
            System.out.println("SERVER(Authenticate): " + inMessage);

            // I AM <username> <password>
            outMessage = "I AM " + username + " " + password;
            out.println(outMessage);
            System.out.println("Client: " + outMessage);

            // WAIT FOR THE TOURNAMENT TO BEGIN <pid>
            inMessage = in.readLine();
            System.out.println("SERVER(Authenticate): " + inMessage);
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

    public void ChallengeProtocol(BufferedReader in, PrintWriter out){
        String serverMessage = "";
        boolean nextGame = true;
        int challengeId, numberOfRounds=0;
        try {
            while(nextGame) {
                // NEW CHALLENGE <cid> YOU WILL PLAY <rounds> MATCHES
                serverMessage = in.readLine();
                System.out.println("SERVER(Challenge): " + serverMessage);
                Matcher ChallengeMatcher = FrequentlyUsedPatterns.ChallengeMssgPattern.matcher(serverMessage);
                if (ChallengeMatcher.matches()) {
                    challengeId = Integer.parseInt(ChallengeMatcher.group(1));
                    numberOfRounds = Integer.parseInt(ChallengeMatcher.group(2));
                }
                for (int i = 1; i <= numberOfRounds; i++) {
                    RoundProtocol(in, out);
                }
                // END OF CHALLENGES or WAIT FOR THE NEXT CHALLENGE TO BEGIN
                serverMessage = in.readLine();
                System.out.println("SERVER(Challenge): " + serverMessage);
                if (FrequentlyUsedPatterns.ChallengeOverMssgPattern.matcher(serverMessage).matches()) {
                    nextGame = false;
                }
            }
            System.out.println("Delegate: End of Challenge Protocol!");
        }catch(IOException ex){
            unexpectedError = "ChallengeProtocol: " + ex.getMessage();
            System.out.println("Challenge Protocol: Failed to obtain message from server.");
        }
    }

    public void RoundProtocol(BufferedReader in, PrintWriter out){
        String serverMessage = "";
        try{
            // BEGIN ROUND <rid> OF <rounds>
            serverMessage = in.readLine();
            System.out.println("SERVER(Round): " + serverMessage);

            while(!FrequentlyUsedPatterns.RoundBeginMssgPattern.matcher(serverMessage).matches()){
                serverMessage = in.readLine();
                System.out.println("SERVER(Round): " + serverMessage);
            }

            MatchProtocol(in, out);

            // END OF ROUND <rid> OF <rounds> or END OF ROUND <rid> OF <rounds> WAIT FOR THE NEXT MATCH
            serverMessage = in.readLine();
            System.out.println("SERVER(Round): " + serverMessage);
            System.out.println("Delegate: End of Round Protocol!");
        } catch (IOException ex){
            unexpectedError = "RoundProtocol: " + ex.getMessage();
            System.out.println("Round Protocol: Failed to obtain message from server.");
        }
    }

    public void MockMatchProtocol(BufferedReader in, PrintWriter out){
        String serverMessage = "", clientMessage = "";
        try{
            //match start message
            serverMessage = in.readLine();
            System.out.println("SERVER: " + serverMessage);
            Matcher NewMatchMatcher = FrequentlyUsedPatterns.NewMatchMssgPattern.matcher(serverMessage);
            if(NewMatchMatcher.matches()) {
                opponentId = Integer.parseInt(NewMatchMatcher.group(1));
                game1 = new GameLogicDirector(playerId, opponentId);

                game1Id = null;

                game1.begin();

                serverMessage = in.readLine();
                Matcher gameOverMatcher = FrequentlyUsedPatterns.GameOverMssgPattern.matcher(serverMessage);
                while(!gameOverMatcher.matches()){
                    MoveProtocol(in, out, serverMessage);
                }
                //game1 score
                serverMessage = in.readLine();
                System.out.println("SERVER: " + serverMessage);
            }
            System.out.println("Delegate: End of Match Protocol!");
        }catch (IOException ex){
            unexpectedError = "MatchProtocol: " + ex.getMessage();
            System.out.println("Match Protocol: Failed to obtain message from server.");
        }
    }

    public void MatchProtocol(BufferedReader in, PrintWriter out){
        String serverMessage = "", clientMessage = "";

        try{
            // NEW MATCH BEGINNING NOW YOUR OPPONENT IS PLAYER <pid>
            serverMessage = in.readLine();
            System.out.println("SERVER(Match): " + serverMessage);
            Matcher NewMatchMatcher = FrequentlyUsedPatterns.NewMatchMssgPattern.matcher(serverMessage);
            if(NewMatchMatcher.matches()) {
                opponentId = Integer.parseInt(NewMatchMatcher.group(1));

                game1 = new GameLogicDirector(playerId, opponentId, true);
                game2 = new GameLogicDirector(opponentId, playerId, true);
                game1Id = null;
                game2Id = null;

                game1.begin();
                game2.begin();

                serverMessage = in.readLine();
                Matcher gameOverMatcher = FrequentlyUsedPatterns.GameOverMssgPattern.matcher(serverMessage);
                while(!gameOverMatcher.matches()){
                    MoveProtocol(in, out, serverMessage);
                    serverMessage = in.readLine();
                    gameOverMatcher = FrequentlyUsedPatterns.GameOverMssgPattern.matcher(serverMessage);
                }

                //game1 score Message: GAME <gid> OVER PLAYER <pid> <score> PLAYER <pid> <score>
                System.out.println("SERVER(Match): " + serverMessage);
                //game2 score Message: GAME <gid> OVER PLAYER <pid> <score> PLAYER <pid> <score>
                serverMessage = in.readLine();
                System.out.println("SERVER(Match): " + serverMessage);
            }
            System.out.println("Delegate: End of Match Protocol!");
        }catch (IOException ex){
            unexpectedError = "MatchProtocol: " + ex.getMessage();
            System.out.println("Match Protocol: Failed to obtain message from server.");
        }
    }

    public void MoveProtocol(BufferedReader in, PrintWriter out, String serverMessage){
        String clientMessage = "", placedAndBuildMssg = "";
        String gameId, moveNumber;
        int pId;
        String tileAssigned;

        // MAKE YOUR MOVE IN GAME <gid> WITHIN <timemove> SECOND: MOVE <#> PLACE <tile> or:
        // GAME <gid> MOVE <#> PLAYER <pid> <move>
        System.out.println("SERVER(Move): " + serverMessage);
        Matcher serverPromptMatcher = FrequentlyUsedPatterns.MoveServerPromptMssgPattern.matcher(serverMessage);
        Matcher gameMovePlayerMatcher = FrequentlyUsedPatterns.GameMovePlayerMssgPattern.matcher(serverMessage);
        Matcher gameForfeitedMatcher = FrequentlyUsedPatterns.GameForfeitedMssgPattern.matcher(serverMessage);
        Matcher gameLostMatcher = FrequentlyUsedPatterns.GameLostMssgPattern.matcher(serverMessage);
        if(serverPromptMatcher.matches()){
            System.out.println("Move Protocol: Executing our move...");
            gameId = serverPromptMatcher.group(1);
            if(game1Id == null){
                game1Id = gameId;
            } else if(game2Id == null){
                game2Id = gameId;
            }
            moveNumber = serverPromptMatcher.group(3);
            tileAssigned = serverPromptMatcher.group(4);

            //palceAndBuildMessage from Our AI's action
            if(gameId.equals(game1Id)){
                placedAndBuildMssg = game1.tournamentMove(tileAssigned);
            } else{
                placedAndBuildMssg = game2.tournamentMove(tileAssigned);
            }

            clientMessage = "GAME " + gameId + " MOVE " + moveNumber + " ";
            clientMessage += placedAndBuildMssg;
            out.println(clientMessage);
            System.out.println("Client: " + clientMessage);

        } else if(gameForfeitedMatcher.matches()){
            System.out.println("Move Protocol: marking game as forfeited...");
            gameId = gameForfeitedMatcher.group(1);
            pId = Integer.parseInt(gameForfeitedMatcher.group(3));
            String opponentForfeitedMssg = gameForfeitedMatcher.group(4);
            if (gameId.equals(game1Id)) {
                game1.setGameOver();
            } else if(gameId.equals(game2Id)) {
                game2.setGameOver();
            }
        } else if(gameLostMatcher.matches()){
            System.out.println("Move Protocol: marking game as lost...");
            gameId = gameLostMatcher.group(1);
            pId = Integer.parseInt(gameLostMatcher.group(3));
            String opponentLostMssg = gameLostMatcher.group(4);
            if (gameId.equals(game1Id)) {
                game1.setGameOver();
            } else if(gameId.equals(game2Id)){
                game2.setGameOver();
            }
        } else if(gameMovePlayerMatcher.matches()){
            System.out.println("Move Protocol: Executing opponent move...");
            gameId = gameMovePlayerMatcher.group(1);
            pId = Integer.parseInt(gameMovePlayerMatcher.group(3));
            String opponentMoveMssg = gameMovePlayerMatcher.group(4); //could be our move message, if so, ignored
            if(pId != playerId) {
                if (gameId.equals(game1Id)) {
                    game1.opponentPlayerMove(opponentMoveMssg);
                } else {
                    game2.opponentPlayerMove(opponentMoveMssg);
                }
            }
        }
        System.out.println("Delegate: End of Move Protocol!");
    }
}
