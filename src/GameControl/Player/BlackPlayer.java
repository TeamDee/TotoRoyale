package GameControl.Player;

import GameEngine.GameController;
import GameModel.Map.GameMap;
/**
 * Created by jowens on 3/10/17.
 */
public class BlackPlayer extends Player{

    public BlackPlayer(){
        this("BlackPlayer", null);
    }

    public BlackPlayer(String name, GameMap gameMap){
        super(gameMap);
        this.name = name;
    }
}
