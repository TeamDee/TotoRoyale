package GameView.Map;

/**
 * Created by jowens on 3/8/17.
 */
public class Constants {
    public static int TILE_HEIGHT = 64, TILE_WIDTH = 64; //default
    public static int MEEPLES_PER_PLAYER = 20;
    public static int TOTORO_PER_PLAYER = 3;
    public static int TIGER_PER_PLAYER = 2;
    public static boolean SHOW_COORDINATES = true;

    public static boolean printGameInfo = false;

    public void setTileConstants(int newHeight, int newWidth){
        TILE_HEIGHT = newHeight;
        TILE_WIDTH = newWidth;
    }
}
