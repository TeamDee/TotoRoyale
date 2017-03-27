package GameView.Map;

import GameModel.Map.Tile.HexTile;
import GameModel.Map.Tile.TerrainTile;
import GameView.Tileables.MeepleView;

/**
 * Created by jowens on 3/8/17.
 */
public abstract class TerrainView extends HexTileView {

    public void visit(TerrainTile tt){
        if(tt.getMeepleCount()>0){
            TileableView meepleView = new MeepleView(tt.getMeepleCount(), tt.getOwner());
            super.addTileableView(meepleView);
            super.makeNewImage();
        }
    }

    public int getPriority() {
        return 1;
    }
}