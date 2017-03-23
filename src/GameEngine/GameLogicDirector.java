package GameEngine;

import GameControl.Player.BlackPlayer;
import GameControl.Player.Player;
import GameControl.Player.WhitePlayer;
import GameModel.Map.GameMap;
import GameModel.Map.Tile.Deck;

import java.util.ArrayList;

/**
 * Created by jowens on 3/8/17.
 * responsible for running the game temporally
 */
public class GameLogicDirector implements Runnable{
    private static GameLogicDirector me;

    private boolean newGame = true;


    //Game specific objects
    Player p1,p2;
    ArrayList<Player> players;
    public Deck deck;
    GameController gc;
    private GameMap myMap;

    public GameLogicDirector(){
        myMap = new GameMap();
        deck = new Deck();
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

    /*
      NEVER CALL THIS - DAVE
     */
    public void run(){
        while(true) {
            if (newGame) {
                System.out.println("Initializing new game.");
                initializeNewGame();
            } else {
                //game logic
                System.out.println("cards left" + deck.cardsLeft());
                if (deck.cardsLeft() > 0) {
                    for (Player p : players) {
                        System.out.println("Round " + (48 - deck.cardsLeft()));
                        p.takeTurn(myMap, deck.draw());
                        getMap().printInfoAboutMap();
                    }
                } else { //game over
                    myMap.printInfoAboutMap();
                }
            }

            try {
                Thread.sleep(000);
            } catch (InterruptedException ie) {
                System.out.println(ie.getStackTrace());
            }
        }
    }



    public GameMap getMap(){
        return myMap;
    }

    private void initializeNewGame() {
        p1 = new WhitePlayer();
        p2 = new BlackPlayer();
        players = new ArrayList<Player>();
        players.add(p1);
        players.add(p2);

        deck = Deck.newExampleDeck();
        System.out.println(deck.cardsLeft());

        newGame = false; // Q: what's this for? A: see run method
        gc = new GameController();
        myMap.placeFirstTile(deck.draw());
    }

}
