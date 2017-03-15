package GameControl.Player;

import GameModel.Map.GameMap;
import GameView.Map.Constants;

/**
 * Created by jowens on 3/8/17.
 */
public class Player {
    //units
    private int totoroCount;
    private int meepleCount;

    //scoring
    private int score;

    public Player(){
        totoroCount = Constants.TOTORO_PER_PLAYER;
        meepleCount = Constants.MEEPLES_PER_PLAYER;
        score = 0;
    }

    public boolean removeTotoro(int amount){
        if(amount > totoroCount)
            return false;
        totoroCount-=amount;
        checkGameOver();
        return true;
    }
    public boolean removeMeeples(int amount){
        if(amount > meepleCount)
            return false;
        meepleCount-=amount;
        checkGameOver();
        return true;
    }

    private void checkGameOver(){
        //TODO end game if player runs out of meeples and totoros
    }

    public void awardPoints(int amount){
        score += amount;
    }
    public void takeTurn(GameMap gameMap){

    }
}
