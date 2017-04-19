package GameNetworking;

import GameEngine.GameLogicDirector;

import java.io.BufferedReader;
import java.io.FileReader;
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
    private String game1Id, game2Id, game3Id, game4Id;
    private GameLogicDirector game1, game2, game3, game4, currentGame1, currentGame2;
    private String unexpectedError;
    boolean evenGame = true;

    public TigerLandDelegate(){
        String serverName;
        int port;
//        Scanner input = new Scanner(System.in);
        try {
            Scanner in = new Scanner(new BufferedReader(new FileReader("src/networking/serverInfo")));

            System.out.println("What is the serverName?");
            serverName = in.nextLine();
            System.out.println("What is the port number?");
            port = Integer.parseInt(in.nextLine());

            System.out.println("Enter in order: TournamentPassword, Username, Password");
            tournamentPW = in.next(); username = in.next(); password = in.next();
//            username = input.next(); password = input.next();
            client= new TigerLandClient(serverName, port);

            gameEnded = false;
            unexpectedError = "";
            System.out.println("Game Delegate successfully created.");
        }catch (Exception e){
            System.out.println("Error in scanner");
        }
    }

    public TigerLandDelegate(String servername, int port) {
        client = new TigerLandClient(servername, port);
        gameEnded = false;
        tournamentPW = "hello";
        username = "TEAM_D"; password = "PASS_D";
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
        String outMessage, inMessage = null;
        boolean authenticated = false;
        try {
            // ENTER THUNDERDOME <tournament password>
            outMessage = "ENTER THUNDERDOME " + tournamentPW;
            out.println(outMessage);
            System.out.println("Client: " + outMessage);

            // TWO SHALL ENTER, ONE SHALL LEAVE
            while(inMessage == null)
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

            game1 = new GameLogicDirector(1, 2, true);
            game2 = new GameLogicDirector(2, 1, true);
            game3 = new GameLogicDirector(3, 4, true);
            game4 = new GameLogicDirector(4, 3, true);

            System.out.println("Delegate: Authentication is successful!");
        }catch(IOException ex){
            unexpectedError = "AuthenticationProtocol: " + ex.getMessage();
            System.out.println("Failed to AuthenticationProtocol with server. Error: " + unexpectedError);
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
                // while(serverMessage == null)
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
        boolean endOfRoundMessageRecieved = false;
        try{
            while(!endOfRoundMessageRecieved) {
                // BEGIN ROUND <rid> OF <rounds>
                serverMessage = in.readLine();
                System.out.println("SERVER(Round): " + serverMessage);
                if (FrequentlyUsedPatterns.RoundBeginMssgPattern.matcher(serverMessage).matches()) {
                    MatchProtocol(in, out);
                } else if (FrequentlyUsedPatterns.RoundEndMssgPattern.matcher(serverMessage).matches() ||
                        FrequentlyUsedPatterns.RoundEndNextMssgPattern.matcher(serverMessage).matches()) {
                    // END OF ROUND <rid> OF <rounds> or END OF ROUND <rid> OF <rounds> WAIT FOR THE NEXT MATCH
                    endOfRoundMessageRecieved = true;
                }
            }

            System.out.println("Delegate: End of Round Protocol!");
        } catch (IOException ex){
            unexpectedError = "RoundProtocol: " + ex.getMessage();
            System.out.println("Round Protocol: Failed to obtain message from server.");
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

                game1Id = null;
                game2Id = null;
                game3Id = null;
                game4Id = null;

                if(evenGame){
                    game1.setUpPlayers(playerId, opponentId);
                    game2.setUpPlayers(opponentId, playerId);
                    currentGame1 = game1;
                    currentGame2 = game2;
                    game3.cleanup();
                    game4.cleanup();
                } else{
                    game3.setUpPlayers(playerId, opponentId);
                    game4.setUpPlayers(opponentId, playerId);
                    currentGame1 = game3;
                    currentGame2 = game4;
                    game1.cleanup();
                    game2.cleanup();
                }
                evenGame = !evenGame;
                currentGame1.begin();
                currentGame2.begin();
                serverMessage = in.readLine();
                Matcher gameOverMatcher = FrequentlyUsedPatterns.GameOverMssgPattern.matcher(serverMessage);
                while(serverMessage!=null && serverMessage!="" && !serverMessage.equals("null") && !gameOverMatcher.matches()){
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
            }
            else if(game2Id == null){
                game2Id = gameId;
            }
            else if(game3Id == null){
                game3Id = gameId;
            }
            else if(game4Id == null){
                game4Id = gameId;
            }
            moveNumber = serverPromptMatcher.group(3);
            tileAssigned = serverPromptMatcher.group(4);

            //palceAndBuildMessage from Our AI's action
            if(gameId.equals(game1Id)){
                placedAndBuildMssg = currentGame1.tournamentMove(tileAssigned);
            } else{
                placedAndBuildMssg = currentGame2.tournamentMove(tileAssigned);
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
                currentGame1.setGameOver();
            } else if(gameId.equals(game2Id)) {
                currentGame2.setGameOver();
            }
            else if(gameId.equals(game3Id)) {
                game3.setGameOver();
            }
            else if(gameId.equals(game4Id)) {
                game4.setGameOver();
            }
        } else if(gameLostMatcher.matches()){
            System.out.println("Move Protocol: marking game as lost...");
            gameId = gameLostMatcher.group(1);
            pId = Integer.parseInt(gameLostMatcher.group(3));
            String opponentLostMssg = gameLostMatcher.group(4);
            if (gameId.equals(game1Id)) {
                currentGame1.setGameOver();
            } else if(gameId.equals(game2Id)){
                currentGame2.setGameOver();
            }
            else if(gameId.equals(game3Id)){
                game3.setGameOver();
            }
            else if(gameId.equals(game4Id)){
                game4.setGameOver();
            }
        } else if(gameMovePlayerMatcher.matches()){
            System.out.println("Move Protocol: Executing opponent move...");
            gameId = gameMovePlayerMatcher.group(1);
            pId = Integer.parseInt(gameMovePlayerMatcher.group(3));
            String opponentMoveMssg = gameMovePlayerMatcher.group(4); //could be our move message, if so, ignored
            if(pId != playerId) {
                if (gameId.equals(game1Id)) {
                    currentGame1.opponentPlayerMove(opponentMoveMssg);
                } else {
                    currentGame2.opponentPlayerMove(opponentMoveMssg);
                }
            }
        }
        System.out.println("Delegate: End of Move Protocol!");
    }
}
