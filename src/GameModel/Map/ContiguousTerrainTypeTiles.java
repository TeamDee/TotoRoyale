package GameModel.Map;

import GameModel.Map.Tile.HexTile;
import GameModel.Map.Tile.TerrainType;

/**
 * Created by conor on 3/20/2017.
 */
public class ContiguousTerrainTypeTiles extends ContiguousTiles {
    TerrainType terrainType;

    public ContiguousTerrainTypeTiles(HexTile startTile, TerrainType terrainType) {
        super(startTile);
        this.terrainType = terrainType;
    }

    @Override
    public boolean isContiguous(HexTile tile) {
        return super.isContiguous(tile) && tile.terrainType() == terrainType;
    }
}
