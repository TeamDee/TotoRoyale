package GameModel.Map.Tile;

/**
 * Created by jowens on 3/13/17.
 */
public class VolcanoTile extends HexTile {
    //TODO test this
    public boolean ofSameType(VolcanoTile vt){
        return true;
    }
    public boolean ofSameType(TerrainTile tt){
        return false;
    }
    @Override
    public String toString(){
        return "Volcano";
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
        return false;
    }


}
