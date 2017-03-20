package GameModel.Map;

import GameModel.Map.Coordinates.AxialCoordinate;
import GameModel.Map.Tile.HexTile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by conor on 3/20/2017.
 */
public abstract class ContiguousTiles {
    List<HexTile> tiles;

    public ContiguousTiles(HexTile startTile) {
        tiles.add(startTile);
        fill(startTile);
    }

    public boolean isContiguous(HexTile tile) {
        for (HexTile contiguousTile : tiles) {
            if (AxialCoordinate.areAdjacent(contiguousTile.getLocation().getAxialCoordinate(), tile.getLocation().getAxialCoordinate()))
                return true;
        }
        return false;
    }


    void fill(HexTile hexTile) {
        ArrayList<HexTile> adjacentHexTiles = getAdjacentHexTiles(hexTile);
        for (HexTile adjacentHexTile : adjacentHexTiles) {
            if (!tiles.contains(adjacentHexTile) && isContiguous(adjacentHexTile)) {
                tiles.add(adjacentHexTile);
                fill(adjacentHexTile);
            }
        }
    }

    ArrayList<HexTile> getAdjacentHexTiles(HexTile hexTile) {
        ArrayList<HexTile> adjacentHexTiles = new ArrayList<HexTile>();
        if (hexTile.getNorth() != null)
            adjacentHexTiles.add(hexTile.getNorth());
        if (hexTile.getNorthEast() != null)
            adjacentHexTiles.add(hexTile.getNorthEast());
        if (hexTile.getSouthEast() != null)
            adjacentHexTiles.add(hexTile.getSouthEast());
        if (hexTile.getSouth() != null)
            adjacentHexTiles.add(hexTile.getSouth());
        if (hexTile.getSouthWest() != null)
            adjacentHexTiles.add(hexTile.getSouthWest());
        if (hexTile.getNorthWest() != null)
            adjacentHexTiles.add(hexTile.getSouth());
        return adjacentHexTiles;
    }
}
