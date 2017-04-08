package GameView.Map;
import GameModel.Map.Tile.HexTile;
import javax.swing.JPanel;
/**
 * Created by jowens on 3/8/17.
 */
public abstract class HexTileView extends TileView{
    public HexTileView(HexTile mine){
        super(mine);
    }
}
