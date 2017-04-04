package GameControl.Player;

import GameModel.Map.GameMap;
import GameModel.Map.Tile.Grass;
import GameModel.Map.Tile.Rock;
import GameModel.Map.Tile.VolcanoTile;
import GameModel.Map.TriHexTile;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by Z_K on 3/26/2017.
 */
public class PlayerTest {
    private static GameMap map;
    private static Player player1;

    @BeforeClass
    public static void setUp(){
        map = new GameMap();
        player1 = new WhitePlayer("WhitePlayer", map);
    }

    @Test
    public void playerTakeTurnTest(){
        int numberOfBoardSpacesBefore = map.getNumberOfBoardSpaces();
        TriHexTile tht = new TriHexTile(new Grass(), new Rock(), new VolcanoTile());
        player1.takeTurn(map, tht);
        Assert.assertFalse(map.getNumberOfBoardSpaces()==numberOfBoardSpacesBefore);
    }
}
