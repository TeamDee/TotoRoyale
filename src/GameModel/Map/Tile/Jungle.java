package GameModel.Map.Tile;

/**
 * Created by jowens on 3/13/17.
 */

public class Jungle extends TerrainTile{
    @Override
    public boolean isJungle(){
        return true;
    }

    @Override
    public String toString(){
        return "Jungle";
    }

    @Override
    public boolean ofSameType(HexTile ht){
        return ht.ofSameType(this);
    }

    public boolean ofSameType(Grass gt){
        return false;
    }
    public boolean ofSameType(Rock gt){
        return false;
    }
    public boolean ofSameType(Lake gt){
        return false;
    }
    public boolean ofSameType(Jungle gt){
        return true;
    }
    public boolean ofSameType(VolcanoTile volcanoTile){
        return false;
    }
}


