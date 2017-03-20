package GameModel.Map;

import GameModel.Map.Coordinates.AxialCoordinate;
import GameModel.Map.Tile.HexTile;
import GameModel.Map.Tile.TerrainType;

/**
 * Created by conor on 3/20/2017.
 */
public class ContiguousTerrainType extends ContiguousTiles {
    TerrainType terrainType;

    public ContiguousTerrainType(TerrainType terrainType) {
        this.terrainType = terrainType;
    }

    @Override
    public boolean isContiguous(HexTile tile) {
        return super.isContiguous(tile) && tile.terrainType() == terrainType;
    }
}
