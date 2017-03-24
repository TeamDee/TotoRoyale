package GameView.Map;

/**
 * Created by jowens on 3/8/17.
 */
public abstract class TerrainView extends HexTileView {

    public int getPriority() {
        return 1;
    }
}