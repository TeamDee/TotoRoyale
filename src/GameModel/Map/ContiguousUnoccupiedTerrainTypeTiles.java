package GameModel.Map;

import GameModel.Map.Tile.HexTile;

/**
 * Created by conor on 3/21/2017.
 */
public class ContiguousUnoccupiedTerrainTypeTiles extends ContiguousTerrainTypeTiles{

    public ContiguousUnoccupiedTerrainTypeTiles(HexTile startTile) {
        super(startTile);
    }

    @Override
    public boolean isContiguous(HexTile hexTile) {
        return super.isContiguous(hexTile) && hexTile.getMeepleCount() == 0 && !hexTile.hasTotoro();
    }
}
