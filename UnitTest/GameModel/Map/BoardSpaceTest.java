package GameModel.Map;

import GameModel.Map.Coordinates.AxialCoordinate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Z_K on 3/24/2017.
 */
public class BoardSpaceTest {
    private AxialCoordinate location;

    @Before
    public void initialize(){
        location = new AxialCoordinate(0, 0);
    }

    @Test
    public void createBoardSpaceTest(){
        BoardSpace boardSpace = new BoardSpace(location);
        Assert.assertFalse(boardSpace.hasTile());
    }

    @After
    public void cleanUp(){

    }

}
