package GameView.Map;

import GameModel.Map.Tile.TerrainTile;
import GameView.Tileables.MeepleView;
import GameView.Tileables.TileableView;

import javax.swing.*;
import java.awt.Point;
/**
 * Created by jowens on 3/8/17.
 */
public abstract class TerrainView extends HexTileView {
    public TerrainView(TerrainTile tt){
        super(tt);
    }

    public void visit(TerrainTile tt){
        if(tt.getMeepleCount()>0){
            TileableView meepleView = new MeepleView(tt.getMeepleCount(), tt.getOwner());
            super.addTileableView(meepleView);
            super.makeNewImage(new Point(tt.getLocation().x,tt.getLocation().y));
        }
    }

    public int getPriority() {
        return 1;
    }
}