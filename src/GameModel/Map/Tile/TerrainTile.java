package GameModel.Map.Tile;
import GameControl.Player.Player;
import GameModel.Map.TriHexTile;

/**
 * Terrain tiles can hold meeples and totoros.
 * Created by jowens on 3/8/17.
 */


public abstract class TerrainTile extends HexTile {

    public TerrainTile(TriHexTile compositor){
        this.triHexTile = compositor;
    }
    public TerrainTile(){

        //System.out.println("warning: building a terrainTile without supplying a compositor");
    }

    public boolean placeMeeple(Player p){
        if(numMeeplesOnTile() != 0) //terrain already has meeples
            return false;
        else {
            if(p.removeMeeples(getLevel()))  //if the player has enough meeples, remove them and award points
                p.awardPoints(getLevel() * getLevel());
            else
                return false;
        }
        return true;
    }

    public boolean ofSameType(VolcanoTile vt){
        return false;
    }
    public boolean ofSameType(TerrainTile tt){
        return myType == tt.getTerrain();
    }

    public TerrainType getTerrain(){
        return myType;
    }

    //this is the OOPy way of determining terrain type, imo
    public boolean isGrass(){
        return false;
    }
    public boolean isLake(){
        return false;
    }
    public boolean isRock(){
        return false;
    }
    public boolean isJungle(){
        return false;
    }
}

