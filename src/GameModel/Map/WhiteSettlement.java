package GameModel.Map;

import GameControl.Player.WhitePlayer;
import GameModel.Map.Tile.HexTile;

/**
 * Created by conor on 3/20/2017.
 */
public class WhiteSettlement extends Settlement{

    public WhiteSettlement(HexTile startTile) {
        super(startTile);
    }

    @Override
    public boolean isContiguous(HexTile hexTile) {
        return super.isContiguous(hexTile) && hexTile.getOwner() instanceof WhitePlayer;
    }
}
