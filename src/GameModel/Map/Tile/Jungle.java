package GameModel.Map.Tile;

/**
 * Created by jowens on 3/13/17.
 */

public class Jungle extends TerrainTile{
    public Jungle(){
        myType = TerrainType.JUNGLE;
    }

    @Override
    public boolean isJungle(){
        return true;
    }

    @Override
    public String toString(){
        return "Jungle";
    }


}


