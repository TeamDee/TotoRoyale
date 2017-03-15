package GameView.Map;

/**
 * Created by jowens on 3/8/17.
 */
public class Constants {
    public static int TILE_HEIGHT, TILE_WIDTH = 64; //default
    public static int MEEPLES_PER_PLAYER = 20;
    public static int TOTORO_PER_PLAYER = 3;

    public void setTileConstants(int newHeight, int newWidth){
        TILE_HEIGHT = newHeight;
        TILE_WIDTH = newWidth;
    }
}
