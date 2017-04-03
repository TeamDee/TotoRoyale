package GameModel.Map;

import GameModel.Map.Coordinates.AxialCoordinate;
import GameModel.Map.Tile.Grass;
import GameModel.Map.Tile.Rock;
import GameModel.Map.Tile.VolcanoTile;
import org.junit.*;

/**
 * Created by Z_K on 3/24/2017.
 */
public class BoardSpaceTest {
    private static GameMap map;
    private static AxialCoordinate location;

    @BeforeClass
    public static void initializeTest(){
        map = new GameMap();
        location = new AxialCoordinate(0, 0);
    }

    @Test
    public void createABoardSpaceTest(){
        BoardSpace boardSpace = new BoardSpace(location,map);
        Assert.assertFalse(boardSpace.hasTile());
        Assert.assertTrue(boardSpace.getLocation().compare(location));
    }

    @Test
    public void activateAdjacentBoardSpaceTest(){
        TriHexTile tht = new TriHexTile(new Grass(), new Rock(), new VolcanoTile());
        map.getAllLegalPlacements(tht);
        BoardSpace center = map.getVisibleAtAxialCoordinate(new AxialCoordinate(0,0)).getBoardSpace();
        Assert.assertTrue(center.getNorth().isActive());
        Assert.assertTrue(center.getNorthEast().isActive());
        Assert.assertTrue(center.getNorthWest().isActive());
        Assert.assertTrue(center.getSouth().isActive());
        Assert.assertTrue(center.getSouthEast().isActive());
        Assert.assertTrue(center.getSouthWest().isActive());
    }

    @After
    public void cleanUp(){

    }

}
