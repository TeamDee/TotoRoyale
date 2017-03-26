package GameModel.Map;

import GameModel.Map.Tile.HexTile;
import GameModel.Map.Tile.TerrainType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by conor on 3/20/2017.
 */
public class Settlement extends ContiguousTiles{

    public Settlement(HexTile startTile) {
        super(startTile);
    }

    @Override
    public boolean isContiguous(HexTile tile) {
        return super.isContiguous(tile) && !tile.isOccupied();
    }

    public ArrayList<HexTile> getExpansionTiles(TerrainType terrainType) {
        return null; //TODO implement this
    }
    
    public int getSize() {
        return contiguousTiles.size();
    }
}
