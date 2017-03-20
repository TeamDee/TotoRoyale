package GameControl.Player;

import GameControl.Placement;
import GameModel.Map.Coordinates.AxialCoordinate;
import GameModel.Map.GameMap;
import GameModel.Map.TriHexTile;
import GameView.Map.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jowens on 3/8/17.
 */
public class Player {
    //units
    private int totoroCount;
    private int meepleCount;
    private List<AxialCoordinate> meeplePlacements;
    private List<AxialCoordinate> totoroPlacements;
    private List<List<AxialCoordinate>> settlemments;

    //scoring
    private int score;

    public Player(){
        totoroCount = Constants.TOTORO_PER_PLAYER;
        meepleCount = Constants.MEEPLES_PER_PLAYER;
        score = 0;
    }

    //TODO add AI logic
    public void takeTurn(GameMap gameMap, TriHexTile tile){
        ArrayList<Placement> placements = gameMap.getLegalPlacements(tile);
        Placement stupidPlacement = placements.get(0);
        gameMap.implementPlacement(stupidPlacement);
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
        if (meepleCount == 0 && totoroCount == 0) {
            //TODO end the game
        }
    }

    public void awardPoints(int amount){
        score += amount;
    }

    int getMeepleCount() {
        return meepleCount;
    }

    int getTotoroCount() {
        return totoroCount;
    }
}
