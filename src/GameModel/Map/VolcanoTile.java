package GameModel.Map;

import GameModel.Map.Tile.TerrainTile;
import GameModel.Map.Tile.HexTile;

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
}
