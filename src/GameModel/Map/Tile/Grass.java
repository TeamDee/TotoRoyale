package GameModel.Map.Tile;

/**
 * Created by jowens on 3/8/17.
 */
public class Grass extends TerrainTile {

    public Grass(){
        myType = TerrainType.GRASS;
    }


    @Override
    public boolean isGrass(){
        return true;
    }

    @Override
    public String toString(){
        return "Grass";
    }




}
