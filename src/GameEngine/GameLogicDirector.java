package GameEngine;

import GameControl.Player.BlackPlayer;
import GameControl.Player.Player;
import GameControl.Player.PlayerController;
import GameControl.Player.WhitePlayer;
import GameModel.Map.Coordinates.CubicCoordinate;
import GameModel.Map.Coordinates.OffsetCoordinate;
import GameModel.Map.GameMap;
import GameModel.Map.Tile.Deck;
import GameModel.Map.TriHexTile;
import GameNetworking.FrequentlyUsedPatterns;

import java.util.ArrayList;
import java.util.regex.Matcher;


/**
 * Created by jowens on 3/8/17.
 * responsible for running the game temporally
 */
public class GameLogicDirector implements Runnable{
    private static GameLogicDirector me;
    private boolean newGame = true;
    private boolean AIgame = true;
    private boolean AIvsHuman = false;
    private boolean HumanVsHuman = false;
    private int myId, opponentId;
    private boolean isGameOver = false;

    //Game specific objects
    Player p1,p2;

    Player winner;
    PlayerController activePlayer;
    Player currentPlayer;

    ArrayList<Player> players;
    public Deck deck;
    GameController gc;
    private GameMap myMap;

    public GameLogicDirector(){
        myMap = new GameMap();
        deck = new Deck();
        winner=null;
    }

    public GameLogicDirector(int playerOneId, int playerTwoId){
        myMap = new GameMap();
        initializeNewGame(""+playerOneId, ""+playerTwoId);
        winner=null;
    }

    public String tournamentMove(String tileAssigned){
        String ActionMessage = "PLACE " + tileAssigned + " AT ";
        TriHexTile tht = TriHexTile.makeTriHexTileFromString(tileAssigned);
        ActionMessage += currentPlayer.takeTurn(myMap, tht);
        nextPlayer();
        paint();
        endRoundChecks();
        if(winner != null)
            setGameOver();
        return ActionMessage;
    }

    public void opponentPlayerMove(String moveMssg){
        int cutPoint = 0;
        if(moveMssg.contains("FOUNDED")){
            cutPoint = moveMssg.indexOf("FOUNDED");
        } else if(moveMssg.contains("EXPANDED")){
            cutPoint = moveMssg.indexOf("EXPANDED");
        } else if(moveMssg.contains("BUILT")){
            cutPoint = moveMssg.indexOf("BUILT");
        }
        String placeMssg = moveMssg.substring(0, cutPoint);
        String buildMssg = moveMssg.substring(cutPoint);

        opponentPlayerPlace(placeMssg);
        opponentPlayerBuild(buildMssg);
        System.out.println("Opponent placement: " + placeMssg);
        System.out.println("Opponent built: " + buildMssg);
        nextPlayer();
    }

    public void opponentPlayerPlace(String placement){
        int x,y,z, orientation;
        Matcher placementMatcher = FrequentlyUsedPatterns.PlacementMssgPattern.matcher(placement);
        if(placementMatcher.matches()){
            TriHexTile tht = TriHexTile.makeTriHexTileFromString(placementMatcher.group(1));
            x = Integer.parseInt(placementMatcher.group(2));
            y = Integer.parseInt(placementMatcher.group(3));
            z = Integer.parseInt(placementMatcher.group(4));
            orientation = Integer.parseInt(placementMatcher.group(5));
            OffsetCoordinate location = new CubicCoordinate(x, y, z).getOffsetCoordinate();
            currentPlayer.placeOpponent(tht, location, orientation);
        }
    }

    public void opponentPlayerBuild(String build){
        int x,y,z;
        Matcher buildMatcher = FrequentlyUsedPatterns.BuildPattern.matcher(build);
        if(buildMatcher.matches()){
            x = Integer.parseInt(buildMatcher.group(2));
            y = Integer.parseInt(buildMatcher.group(3));
            z = Integer.parseInt(buildMatcher.group(4));
            OffsetCoordinate location = new CubicCoordinate(x, y, z).getOffsetCoordinate();
            if(build.contains("FOUNDED")){
                currentPlayer.opponentNewSettlement(location);
            } else if(build.contains("EXPANDED")){
                String terrainType = buildMatcher.group(5);
                currentPlayer.opponentExpand(location, terrainType);
            } else if(build.contains("TOTORO")){
                currentPlayer.opponentNewTotoro(location);
            } else if(build.contains("TIGER")){
                currentPlayer.opponentNewTiger(location);
            }
        }
    }

    public static GameLogicDirector getInstance(){
        if(me == null)
            me= new GameLogicDirector();
        return me;
    }

    public void begin(){
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    private void nextPlayer(){
        if(currentPlayer == p1)
            currentPlayer =p2;
        else
            currentPlayer =p1;
    }

    public void run(){
        while(!isGameOver){ }
    }

    /*
      NEVER CALL THIS - DAVE
     */
    public void run2() {
        while (winner == null) {
            if (newGame) {
                initializeNewGame("Whitety", "Blackey");
            }
            else {
                //game logic
                System.out.println("cards left" + deck.cardsLeft());
                if (deck.cardsLeft() > 0) {
                    if (AIgame) {
                        AIvsAIGameTurn();
                    } else if (AIvsHuman) {
                        AIvsHumanGameTurn();
                    }
                    endRoundChecks();
                }
                else { //game over
                    gameOver();
                }
            }
        }
    }



    private void gameOver(){
        System.out.println();
        System.out.println(myMap);
        for (Player p : players) {
            System.out.println("Round " + (48 - deck.cardsLeft()));
            System.out.println("cards left" + deck.cardsLeft());
            System.out.println(p.toString() + " Score: " + p.getScore());
            p.takeTurn(myMap, deck.draw());

            //check if the player has only one type of tokens left. If yes, end the game.
            if (p.checkOnlyOneTypeTokenIsLeft()) {
                winner = p;
                break;
            } else if (deck.cardsLeft() == 0) {
                break;
            }

            System.out.println("\n");
            gc.paint();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                System.out.println(ie.getStackTrace());
            }
        }
        //check if the deck is out of unplayed tiles. If yes, run game winner check and end the game.
        if (deck.cardsLeft() == 0) {
            winner = gameEndCheckWinner();
            gc.paint();
        }

        if (deck.cardsLeft() % 10 == 0) {
            gc.paint();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                System.out.println(ie.getStackTrace());
            }
        }
    }

    private Player gameEndCheckWinner(){
        Player winner = compareScores();
        if(winner==null){
            winner = compareTotoroCount();
            if(winner==null){
                winner = compareTigerCount();
                if(winner==null){
                    winner = compareMeepleCount();
                }
            }
        }
        return winner;
    }

    private Player compareScores(){
        Player winner;
        if(p1.getScore() > p2.getScore()){
            winner = p1;
        } else if(p1.getScore() < p2.getScore()){
            winner = p2;
        } else
            winner = null; //score ties
        return winner;
    }
    private void paint(){
        gc.paint();
    }

    private void endRoundChecks(){
        for (Player p : players) {
            //check if the player has only one type of tokens left. If yes, end the game.
            if (p.checkOnlyOneTypeTokenIsLeft()) {
                winner = p;
                break;
            } else if (deck.cardsLeft() == 0) {
                break;
            }
        }
    }

    private void AIvsHumanGameTurn(){}


    public void AIvsAIGameTurn(){

        System.out.println("Player " + currentPlayer);
        System.out.println("Round " + (48 - deck.cardsLeft()));
        System.out.println("Score " + currentPlayer.getScore());

        for(Player p: players){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                System.out.println(ie.getStackTrace());
            }
            AItakeTurn();
            nextPlayer();
            paint();
            //TODO: for the official game tournament, this block should be removed

        }
    }

    public void AItakeTurn() {
        currentPlayer.takeTurn(myMap, deck.draw());
        System.out.println(getMap());
        System.out.println("\n");

        //TODO: for the official game tournament, this block should be removed
        try {
            Thread.sleep(250);
        } catch (InterruptedException ie) {
            System.out.println(ie.getStackTrace());
        }
    }
    private Player compareTotoroCount(){
        Player winner;
        if(p1.getTotoroCount() < p2.getTotoroCount()){
            winner = p1;
        } else if(p1.getTotoroCount() > p2.getTotoroCount()){
            winner = p2;
        } else
            winner = null; //totoro count ties
        return winner;
    }

    private Player compareTigerCount(){
        Player winner;
        if(p1.getTigerCount() < p2.getTigerCount()){
            winner = p1;
        } else if(p1.getTigerCount() > p2.getTigerCount()){
            winner = p2;
        } else
            winner = null; //tiger count ties
        return winner;
    }

    private Player compareMeepleCount(){
        Player winner;
        if(p1.getMeepleCount() < p2.getMeepleCount()){
            winner = p1;
        } else {
            winner = p2;
        }
        return winner;
    }

    public GameMap getMap(){
        return myMap;
    }


    private void initializeNewGame(String WhitePlayerName, String BlackPlayerName) {
        System.out.println("Initializing new game.");

        p1 = new WhitePlayer(WhitePlayerName, myMap, null);
        p2 = new BlackPlayer(BlackPlayerName, myMap, p1);
        p1.setEnemyPlayer(p2);

        players = new ArrayList<Player>();
        players.add(p1);
        players.add(p2);
        activePlayer = new PlayerController(p1);
        currentPlayer = p1;

        gc = GameController.getInstance();
        deck = Deck.newExampleDeck();
//        System.out.println(deck.cardsLeft());
        gc.initViewControllerInteractions(p1, activePlayer);
        newGame = false; // Q: what's this for? A: see run method

        winner = null;
        gc = GameController.getInstance();
        gc.initViewControllerInteractions(p1, activePlayer);
    }

    public boolean isGameOver(){
        return isGameOver;
    }

    public void setGameOver() {
        isGameOver = true;
    }
}
