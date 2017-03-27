package GameControl.Player;

import GameControl.Placement;
import GameModel.Map.Coordinates.AxialCoordinate;
import GameModel.Map.GameMap;
import GameModel.Map.Tile.HexTile;
import GameModel.Map.Tile.TerrainType;
import GameModel.Map.Tile.TerrainTile;
import GameModel.Map.Tile.VolcanoTile;
import GameModel.Map.TriHexTile;
import GameView.Map.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    //random
    Random random = new Random();
    public Player(){
        totoroCount = Constants.TOTORO_PER_PLAYER;
        meepleCount = Constants.MEEPLES_PER_PLAYER;
        score = 0;
    }

    //TODO add AI logic
    public void takeTurn(GameMap gameMap, TriHexTile tile){
        ArrayList<Placement> placements = gameMap.getLegalMapPlacements(tile);

        if(placements.size() == 0) {   //todo OR the seen placements aren't good enough
            placements.addAll(gameMap.getLegalTablePlacements(tile)); //note this only gets level 0 placements
            System.out.println("NO LEGAL MAP PLACEMENTS");
        }

        Placement stupidPlacement = placements.get(random.nextInt(placements.size())); //todo iterate through placements for the best option

        placeTile(gameMap, stupidPlacement);

        ArrayList<HexTile> tiles = gameMap.getVisible();

        //if we settle... currently the only option
        TerrainTile bestPlaceToSettle = null;
        int currentBest = 0;
        for(HexTile ht: tiles){

            if(ht.terrainType() != TerrainType.VOLCANO){
                if(bestPlaceToSettle == null)
                    bestPlaceToSettle = (TerrainTile)ht;
                if(howGoodIsSettlement((TerrainTile)ht, this) >currentBest) {
                    currentBest = howGoodIsSettlement((TerrainTile)ht, this);
                    buildSettlement((TerrainTile) ht);
                    break;
                }
            }
        }
        VolcanoTile vt = new VolcanoTile();

        buildSettlement((TerrainTile)tile.getTileOne());
    }

    private int howGoodIsSettlement(TerrainTile tt, Player p){
        return 1; //TODO either add logic here or find a better place to do AI stuff
        //ideas
        //get contiguos terrains of a type to see if we could expand there next turn
        //devalue these terrains if the opponent is also adjacent to them, as they may steal them from us

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
    public void placeMeeples(TerrainTile tt) {
        tt.placeMeeple(this);
        removeMeeples(tt.getLevel() + 1);
    }

    public void buildSettlement(TerrainTile tt) {
        if(tt.getMeepleCount() == 0) {
            tt.placeMeeple(this);
            awardPoints(1);
        }
    }


    /*
        Calls settlement and contiguousUnoccupoedTerrainTyesTiles and plaes meeples on legal tiles of same terrain
     */
    public void expandSettlement(List<TerrainTile> settlement) {
        for(TerrainTile expand: settlement) {
            if(expand.getMeepleCount() == 0) {
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

    public boolean isWhite(){
        return false;
    }
}
