package GameControl.Player;

import GameControl.Placement;
import GameModel.Map.Coordinates.AxialCoordinate;
import GameModel.Map.GameMap;
import GameModel.Map.Tile.HexTile;
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
    public boolean placeTileCheck = false; //added for testing

    public Player(){
        totoroCount = Constants.TOTORO_PER_PLAYER;
        meepleCount = Constants.MEEPLES_PER_PLAYER;
        score = 0;
    }

    //TODO add AI logic
    public void takeTurn(GameMap gameMap, TriHexTile tile){
        ArrayList<Placement> placements = gameMap.getLegalTablePlacements(tile); //note this only gets level 0 placements
        //placements.add(gameMap.getLegalPlacementsAtHexTile());
        //ArrayList<Placement> nukePlacement = gameMap.getLegalPlacementsAtHexTile(tile,tile.getTileThree());
        Placement stupidPlacement = placements.get(0);
        /*if(nukePlacement.size() > 3) {
            stupidPlacement = nukePlacement.get(0);
        }
        else {
            stupidPlacement = placements.get(0);
        }*/
        placeTile(gameMap, stupidPlacement);
        buildSettlement(tile.getTileOne());
    }

    public void placeTile(GameMap gameMap, Placement placement) {
        gameMap.implementPlacement(placement);
        placeTileCheck = true; //added for testing
    }
    // Added for testing
    public boolean placementCheck () {
        return placeTileCheck;
    }
    //end testing
    public void placeMeeples(HexTile hexTile) {
        hexTile.placeMeeples(this);
        removeMeeples(hexTile.getLevel() + 1);
    }

    public void buildSettlement(HexTile hexTile) {
        if(hexTile.numMeeplesOnTile() == 0) {
            placeMeeples(hexTile);
            awardPoints(1);
        }
    }


    /*
        Calls settlement and contiguousUnoccupoedTerrainTyesTiles and plaes meeples on legal tiles of same terrain
     */
    public void expandSettlement(List<HexTile> settlement) {
        for(HexTile expand: settlement) {
            if(expand.numMeeplesOnTile() == 0) {
                placeMeeples(expand);
                awardPoints(expand.getLevel()^2);
            }
        }
    }

    public void placeTotoro(List<HexTile> settlement)
    {
        if(settlement.size() >= 5)
        {
            //public ArrayList getLegalTotoroPlacements(List<HexTile> settlement)
            //gets legal place of totoro adjacent to HexTiles in settlement
            //calls something to adjacent tile
            /*
                if(tile.hasTotoro = =false)
                {
                    tile.placeTotoro(); set hasTotoro to true
                    awardPoints(200);
                }

             */

        }
    }

    public int getScore()
    {
        return score;
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
