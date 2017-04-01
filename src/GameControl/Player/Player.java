package GameControl.Player;

import GameControl.Placement;
import GameModel.Map.Coordinates.AxialCoordinate;
import GameModel.Map.GameMap;
import GameModel.Map.Settlement;
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
    private List<Settlement> settlements;

    //scoring
    private int score, scoretemp1;
    public boolean placeTileCheck = false; //added for testing

    //random
    Random random = new Random();
    public Player(){
        totoroCount = Constants.TOTORO_PER_PLAYER;
        meepleCount = Constants.MEEPLES_PER_PLAYER;
        score = 0;
        scoretemp1 = 0;
        settlements = new ArrayList<Settlement>();
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

        //choose the best available settlement
        //if we settle... currently the only option
        TerrainTile bestPlaceToSettle = null;
        int currentBest = 0;
        for(HexTile ht: tiles){
            if(ht.terrainType() != TerrainType.VOLCANO && ht != null){
                if(ht.isOccupied() || ht.getLevel() != 1)
                    continue;
                else if(bestPlaceToSettle == null) {
                    bestPlaceToSettle = (TerrainTile) ht;
                    currentBest = howGoodIsSettlement((TerrainTile)ht, this);
                }
                else if(howGoodIsSettlement((TerrainTile)ht, this) >currentBest) {
                    currentBest = howGoodIsSettlement((TerrainTile)ht, this);
                    bestPlaceToSettle = (TerrainTile)ht;
                    break;
                }
            }
        }
        if(settlements.size() > 0) {
            int whichsettle = random.nextInt(settlements.size());
            ArrayList<TerrainTile> expansion = expandSettlement(settlements.get(whichsettle));
            if (scoretemp1 >= 1) {
                for (TerrainTile add : expansion) {
                    placeMeeples(add);
                    settlements.get(whichsettle).adToSettlement(add);
                    awardPoints(add.getLevel()^2);
                    System.out.println("added");
                }
            } else {
                buildSettlement(bestPlaceToSettle);
                VolcanoTile vt = new VolcanoTile();
            }
        }
        else
        {
            buildSettlement(bestPlaceToSettle);
            VolcanoTile vt = new VolcanoTile();
        }
        //buildSettlement((TerrainTile)tile.getTileOne());
        //buildSettlement(bestPlaceToSettle);
        System.out.println("Meeple COunt: " + meepleCount);
        //VolcanoTile vt = new VolcanoTile();

        //buildSettlement((TerrainTile)tile.getTileOne());
    }

    //TODO this
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
            Settlement settlement = new Settlement();
            settlement.createSettlement(tt);
            settlements.add(settlement);
            awardPoints(1);
        }
    }


    /*
        Calls settlement and contiguousUnoccupoedTerrainTyesTiles and plaes meeples on legal tiles of same terrain
     */
    public ArrayList<TerrainTile> expandSettlement(Settlement settlement1) {
        scoretemp1 = 0;
        int excheck = 0;
        ArrayList<ArrayList<TerrainTile>> allexpand = new ArrayList<>();
        ArrayList<TerrainTile> expansion1 = settlement1.getExpansionTiles(settlement1.getSettlement(), TerrainType.GRASS);
        ArrayList<TerrainTile> expansion2 = settlement1.getExpansionTiles(settlement1.getSettlement(), TerrainType.JUNGLE);
        ArrayList<TerrainTile> expansion3 = settlement1.getExpansionTiles(settlement1.getSettlement(), TerrainType.LAKE);
        ArrayList<TerrainTile> expansion4 = settlement1.getExpansionTiles(settlement1.getSettlement(), TerrainType.ROCK);
        //System.out.println("Expand test: " + expansion1.size() + " " + expansion2.size() + " " + expansion3.size() + " " + expansion4.size());
        allexpand.add(expansion1);
        allexpand.add(expansion2);
        allexpand.add(expansion3);
        allexpand.add(expansion4);
        for(int i = 0; i < allexpand.size(); i ++)
        {
            int tempscore = 0;
            for(TerrainTile checkpoint: allexpand.get(i))
            {
                tempscore = tempscore + checkpoint.getLevel()^2;
            }
            if(tempscore > scoretemp1)
            {
                scoretemp1 = tempscore;
                excheck = i;
            }
        }
        ArrayList<TerrainTile> expansion = allexpand.get(excheck);
        return  expansion;
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
