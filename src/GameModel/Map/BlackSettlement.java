package GameModel.Map;

import GameControl.Player.BlackPlayer;
import GameModel.Map.Tile.HexTile;

/**
 * Created by conor on 3/20/2017.
 */
public class BlackSettlement extends Settlement {

    public BlackSettlement(HexTile startTile) {
        super(startTile);
    }

    @Override
    public boolean isContiguous(HexTile hexTile) {
        return super.isContiguous(hexTile) && hexTile.getOwner() instanceof BlackPlayer;
    }
}
