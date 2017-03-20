package GameModel.Map;

import GameModel.Map.Coordinates.AxialCoordinate;
import GameModel.Map.Tile.HexTile;

import java.util.List;

/**
 * Created by conor on 3/20/2017.
 */
public abstract class ContiguousTiles {
    List<HexTile> tiles;

    public boolean isContiguous(HexTile tile) {
        for(HexTile contiguousTile: tiles) {
            if (AxialCoordinate.areAdjacent(contiguousTile.getLocation().getAxialCoordinate(), tile.getLocation().getAxialCoordinate()))
                return true;
        }
        return false;
    }

    public boolean add(HexTile tile) {
        if (isContiguous(tile)) {
            tiles.add(tile);
            return true;
        }
        else {
            return false;
        }
    }
}
