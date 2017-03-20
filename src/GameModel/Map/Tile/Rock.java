package GameModel.Map.Tile;

/**
 * Created by jowens on 3/13/17.
 */
public class Rock extends TerrainTile{
    public Rock(){
        myType = TerrainType.ROCK;
    }

    @Override
    public boolean isRock(){
        return true;
    }
    @Override
    public String toString(){
        return "Rock";
    }


}
