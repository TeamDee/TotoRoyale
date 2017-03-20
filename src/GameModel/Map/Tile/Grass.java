package GameModel.Map.Tile;

/**
 * Created by jowens on 3/8/17.
 */
public class Grass extends TerrainTile {

    @Override
    public boolean isGrass(){
        return true;
    }

    @Override
    public String toString(){
        return "Grass";
    }

    @Override
    public boolean ofSameType(HexTile ht){
        return ht.ofSameType(this);
    }

    public boolean ofSameType(Grass gt){
        return true;
    }
    public boolean ofSameType(Rock gt){
        return false;
    }
    public boolean ofSameType(Lake gt){
        return false;
    }
    public boolean ofSameType(Jungle gt){
        return false;
    }
    public boolean ofSameType(VolcanoTile volcanoTile){
        return false;
    }
}
