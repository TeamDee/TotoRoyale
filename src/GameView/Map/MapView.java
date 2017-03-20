package GameView.Map;

import GameModel.Map.GameMap;
import GameModel.Map.Tile.HexTile;

import java.awt.Graphics;
import java.util.List;
import GameView.*;
/**
 * Created by jowens on 3/8/17.
 */
public class MapView extends View{

    private GameMap myMap;
    public MapView(GameMap m){
        myMap = m;
    }

    @Override
    public void paint(Graphics g) {
        List<HexTile> tiles = myMap.getVisible();

        drawMap(g, tiles);
        //draw meeples left
    }
    public void drawMap(Graphics g, List<HexTile> tiles){

    }
}
