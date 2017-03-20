package GameEngine;

import GameControl.Player.BlackPlayer;
import GameControl.Player.WhitePlayer;
import GameModel.Map.GameMap;
import GameControl.Player.Player;
import GameModel.Map.Tile.Deck;

import java.util.ArrayList;

/**
 * Created by jowens on 3/8/17.
 * responsible for running the game temporally
 */
public class GameLogicDirector implements Runnable{
    private static GameLogicDirector me;

    private boolean newGame = true;
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

    public void start(){
        Thread gameThread = new Thread(this);
    }

    public void run(){
        if(newGame){
            initializeNewGame();
        }
        else{
            //game logic
            if(deck.cardsLeft() >0 ) {
                for (Player p : players) {
                    p.takeTurn(myMap, deck.draw());
                }
            }
            else{ //game over
                myMap.printInfoAboutMap();
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

        deck = new Deck();

        newGame=false; //what's this for?
        gc = new GameController();
    }

}
