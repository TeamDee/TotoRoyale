package GameEngine;

import GameModel.Map.GameMap;
import GameControl.Player.Player;
import GameModel.Map.Tile.Deck;

/**
 * Created by jowens on 3/8/17.
 * responsible for running the game temporally
 */
public class GameLogicDirector implements Runnable{
    private static GameLogicDirector me;

    private boolean newGame = true;
    Player p1,p2;
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
            updateView();
        }
    }

    public GameMap getMap(){
        return myMap;
    }

    private void initializeNewGame() {
        p1 = new Player();
        p2 = new Player();
        newGame=false;
        gc = new GameController();
    }

}
