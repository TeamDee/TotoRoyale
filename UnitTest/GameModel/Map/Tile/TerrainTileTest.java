package GameModel.Map.Tile;

import GameControl.Player.BlackPlayer;
import GameControl.Player.Player;
import GameControl.Player.WhitePlayer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by conor on 4/9/2017.
 */
public class TerrainTileTest {

    Player whitePlayer;
    Player blackPlayer;

    @Before
    public void setUp() {
        whitePlayer = new WhitePlayer("White Player", null, null);
        blackPlayer = new BlackPlayer("Black Player", null, null);
    }

    @Test
    public void testGetNumberOfFriendlyNeighborsGivenFriendlyAndEnemyNeighbors() {
        TerrainTile center = new Grass();
        TerrainTile north = new Grass();
        TerrainTile northeast = new Grass();
        TerrainTile southeast = new Grass();
        center.setLevel(1);
        north.setLevel(1);
        northeast.setLevel(1);
        southeast.setLevel(1);
        center.placeMeeple(whitePlayer);
        north.placeMeeple(whitePlayer);
        northeast.placeMeeple(blackPlayer);
        southeast.placeMeeple(blackPlayer);
        center.setNorth(north);
        center.setNorthEast(northeast);
        center.setSouthEast(southeast);
        int numberOfFriendlyNeighbors = center.getNumberOfFriendlyNeighbors();
        Assert.assertEquals(1, numberOfFriendlyNeighbors);
    }

    @Test
    public void testGetNumberOfFriendlyNeighborsGivenOnlyFriendlyNeighbors() {
        TerrainTile center = new Grass();
        TerrainTile north = new Grass();
        TerrainTile northeast = new Grass();
        TerrainTile southeast = new Grass();
        center.setLevel(1);
        north.setLevel(1);
        northeast.setLevel(1);
        southeast.setLevel(1);
        center.placeMeeple(whitePlayer);
        north.placeMeeple(whitePlayer);
        northeast.placeMeeple(whitePlayer);
        southeast.placeMeeple(whitePlayer);
        center.setNorth(north);
        center.setNorthEast(northeast);
        center.setSouthEast(southeast);
        int numberOfFriendlyNeighbors = center.getNumberOfFriendlyNeighbors();
        Assert.assertEquals(3, numberOfFriendlyNeighbors);
    }

    @Test
    public void testGetNumberOfFriendlyNeighborsGivenOnlyEnemyNeighbors() {
        TerrainTile center = new Grass();
        TerrainTile north = new Grass();
        TerrainTile northeast = new Grass();
        TerrainTile southeast = new Grass();
        center.setLevel(1);
        north.setLevel(1);
        northeast.setLevel(1);
        southeast.setLevel(1);
        center.placeMeeple(whitePlayer);
        north.placeMeeple(blackPlayer);
        northeast.placeMeeple(blackPlayer);
        southeast.placeMeeple(blackPlayer);
        center.setNorth(north);
        center.setNorthEast(northeast);
        center.setSouthEast(southeast);
        int numberOfFriendlyNeighbors = center.getNumberOfFriendlyNeighbors();
        Assert.assertEquals(0, numberOfFriendlyNeighbors);
    }

    @Test
    public void testGetNumberOfFriendlyNeighborsGivenNoOccupiedNeighbors() {
        TerrainTile center = new Grass();
        TerrainTile north = new Grass();
        TerrainTile northeast = new Grass();
        TerrainTile southeast = new Grass();
        center.setLevel(1);
        north.setLevel(1);
        northeast.setLevel(1);
        southeast.setLevel(1);
        center.placeMeeple(whitePlayer);
        center.setNorth(north);
        center.setNorthEast(northeast);
        center.setSouthEast(southeast);
        int numberOfFriendlyNeighbors = center.getNumberOfFriendlyNeighbors();
        Assert.assertEquals(0, numberOfFriendlyNeighbors);
    }
}
