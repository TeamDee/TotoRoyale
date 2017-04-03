package GameView.Map;
import java.awt.Image.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import GameModel.Map.Tile.HexTile;
import GameView.Tileables.MeepleView;
import GameModel.Map.Tile.*;

/**
 * Created by jowens on 3/8/17.
 */
public abstract class HexTileView extends TileView{
    public HexTileView(HexTile mine){
        super(mine);
    }
}
