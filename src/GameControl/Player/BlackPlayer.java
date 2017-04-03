package GameControl.Player;

import GameEngine.GameController;
import GameModel.Map.GameMap;
/**
 * Created by jowens on 3/10/17.
 */
public class BlackPlayer extends Player{

//    public BlackPlayer(){
//        this("BlackPlayer", null, null);
//    }

    public BlackPlayer(String name, GameMap gameMap, Player enemy){
        super(gameMap, enemy);
        this.name = name;
    }
}
