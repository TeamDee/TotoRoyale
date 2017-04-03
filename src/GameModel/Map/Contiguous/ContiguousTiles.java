package GameModel.Map.Contiguous;

import GameModel.Map.Coordinates.OffsetCoordinate;
import GameModel.Map.Tile.HexTile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by conor on 3/20/2017.
 */
public abstract class ContiguousTiles {
    List<HexTile> contiguousTiles;

    public ContiguousTiles(HexTile startTile) {
        contiguousTiles = new ArrayList<HexTile>();
        contiguousTiles.add(startTile);
        fillFromHexTile(startTile);
    }

    public boolean isContiguous(HexTile tile) {
        for (HexTile contiguousTile : contiguousTiles) {
            if (OffsetCoordinate.areAdjacent(contiguousTile.getLocation(), tile.getLocation()))
                return true;
        }
        return false;
    }

    void fillFromHexTile(HexTile hexTile) {
        ArrayList<HexTile> adjacentHexTiles = getAdjacentHexTiles(hexTile);
        for (HexTile adjacentHexTile : adjacentHexTiles) {
            if (!contiguousTiles.contains(adjacentHexTile) && isContiguous(adjacentHexTile)) {
                contiguousTiles.add(adjacentHexTile);
                fillFromHexTile(adjacentHexTile);
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

    List<HexTile> getTiles() {
        return contiguousTiles;
    }
}
