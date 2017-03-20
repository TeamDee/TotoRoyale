package GameModel.Map;

import GameModel.Map.Tile.HexTile;

/**
 * Created by conor on 3/20/2017.
 */
public class ContiguousOccupiedTiles extends ContiguousTiles{

    public ContiguousOccupiedTiles(HexTile startTile) {
        super(startTile);
    }

    @Override
    public boolean isContiguous(HexTile tile) {
        return super.isContiguous(tile) && tile.getMeepleCount() > 0 || tile.hasTotoro();
    }
}
