package GameModel.Map;

import GameControl.Placement;
import GameModel.Map.Coordinates.AxialCoordinate;
import GameModel.Map.Tile.Grass;
import GameModel.Map.Tile.Rock;
import GameModel.Map.Tile.VolcanoTile;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by Z_K on 3/26/2017.
 */
public class GameMapTest {
    private static GameMap map;
    private static AxialCoordinate location;

    @BeforeClass
    public static void initializeTest(){
        map = new GameMap();
        location = new AxialCoordinate(0, 0);
    }

    @Test
    public void testAddAdjacentBoardSpace(){
        int numberOfBoardSpacesBefore = map.getNumberOfBoardSpaces();
        location = new AxialCoordinate(2,2);
        map.addAdjacentBoardSpaces(location);
        Assert.assertFalse(map.getNumberOfBoardSpaces()==numberOfBoardSpacesBefore);
    }

    @Test
    public void testGetLegalPlacement(){
        TriHexTile tht = new TriHexTile(new Grass(), new Rock(), new VolcanoTile());
        ArrayList<Placement> list = map.getAllLegalPlacements(tht);
        Assert.assertFalse(list.size()==0);
    }

    @Test
    public void testPlaceTile(){
        int numberOfBoardSpacesBefore = map.getNumberOfBoardSpaces();
        TriHexTile tht = new TriHexTile(new Grass(), new Rock(), new VolcanoTile());
        ArrayList<Placement> list = map.getAllLegalPlacements(tht);
        map.implementPlacement(list.get(0));
        Assert.assertFalse(map.getNumberOfBoardSpaces()==numberOfBoardSpacesBefore);
    }
}
