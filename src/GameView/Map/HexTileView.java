package GameView.Map;
import java.awt.Image.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import GameModel.Map.Tile.HexTile;
import GameView.Tileables.MeepleView;

/**
 * Created by jowens on 3/8/17.
 */
public abstract class HexTileView extends TileView{
    BufferedImage myImage;
    public void visit(HexTile hexTile){
        if(hexTile.getMeepleCount()>0){
            TileableView meepleView = new MeepleView(hexTile.getMeepleCount());
            super.addTileableView(meepleView);
        }
    }
}
