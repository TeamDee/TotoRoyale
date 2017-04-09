package GameModel.Map;

import GameControl.Placement;
import GameControl.Player.BlackPlayer;
import GameControl.Player.WhitePlayer;
import GameModel.Map.Coordinates.OffsetCoordinate;
import GameModel.Map.Tile.Grass;
import GameModel.Map.Tile.Rock;
import GameModel.Map.Tile.TerrainTile;
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
    private static OffsetCoordinate location;

    @BeforeClass
    public static void initializeTest(){
        map = new GameMap();
        location = new OffsetCoordinate(0, 0);
    }

    @Test
    public void testAddAdjacentBoardSpace(){
        int numberOfBoardSpacesBefore = map.getNumberOfBoardSpaces();
        location = new OffsetCoordinate(2,2);
        map.addAdjacentBoardSpaces(location);


        TriHexTile tht = new TriHexTile(new Grass(), new Rock(), new VolcanoTile());
        map.getAllLegalPlacements(tht);

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

    @Test
    public void testContainEntireSettlementsGivenSize1Settlement() {
        WhitePlayer whitePlayer = new WhitePlayer("White Player", map, null);
        TerrainTile tt1 = new Grass();
        TerrainTile tt2 = new Grass();
        TerrainTile tt1North = new Grass();
        TerrainTile tt1Northeast = new Grass();
        TerrainTile tt1Southeast = new Grass();
        TerrainTile tt1South = tt2;
        TerrainTile tt1Southwest = new Grass();
        TerrainTile tt1Northwest = new Grass();
        TerrainTile tt2North = tt1;
        TerrainTile tt2Northeast = tt1Southeast;
        TerrainTile tt2Southeast = new Grass();
        TerrainTile tt2South = new Grass();
        TerrainTile tt2Southwest = new Grass();
        TerrainTile tt2NorthWest = tt1Southwest;
        tt1.setLevel(1);
        tt2.setLevel(1);
        tt1North.setLevel(1);
        tt1Northeast.setLevel(1);
        tt1Southeast.setLevel(1);
        tt1Southwest.setLevel(1);
        tt1Northwest.setLevel(1);
        tt2Southeast.setLevel(1);
        tt2South.setLevel(1);
        tt2Southwest.setLevel(1);
        tt1.setNorth(tt1North);
        tt1.setNorthEast(tt1Northeast);
        tt1.setSouthEast(tt1Southeast);
        tt1.setSouth(tt1South);
        tt1.setSouthWest(tt1Southwest);
        tt1.setNorthWest(tt1Northwest);
        tt2.setNorth(tt2North);
        tt2.setNorthEast(tt2Northeast);
        tt2.setSouthEast(tt2Southeast);
        tt2.setSouth(tt2South);
        tt2.setSouthWest(tt2Southwest);
        tt2.setNorthWest(tt2NorthWest);
        tt1.placeMeeple(whitePlayer);
        boolean containEntireSettlements = map.containEntireSettlements(tt1, tt2);
        Assert.assertTrue(containEntireSettlements);
    }

    @Test
    public void testContainEntireSettlementsGivenSize2Settlement() {
        WhitePlayer whitePlayer = new WhitePlayer("White Player", map, null);
        TerrainTile tt1 = new Grass();
        TerrainTile tt2 = new Grass();
        TerrainTile tt1North = new Grass();
        TerrainTile tt1Northeast = new Grass();
        TerrainTile tt1Southeast = new Grass();
        TerrainTile tt1South = tt2;
        TerrainTile tt1Southwest = new Grass();
        TerrainTile tt1Northwest = new Grass();
        TerrainTile tt2North = tt1;
        TerrainTile tt2Northeast = tt1Southeast;
        TerrainTile tt2Southeast = new Grass();
        TerrainTile tt2South = new Grass();
        TerrainTile tt2Southwest = new Grass();
        TerrainTile tt2NorthWest = tt1Southwest;
        tt1.setLevel(1);
        tt2.setLevel(1);
        tt1North.setLevel(1);
        tt1Northeast.setLevel(1);
        tt1Southeast.setLevel(1);
        tt1Southwest.setLevel(1);
        tt1Northwest.setLevel(1);
        tt2Southeast.setLevel(1);
        tt2South.setLevel(1);
        tt2Southwest.setLevel(1);
        tt1.setNorth(tt1North);
        tt1.setNorthEast(tt1Northeast);
        tt1.setSouthEast(tt1Southeast);
        tt1.setSouth(tt1South);
        tt1.setSouthWest(tt1Southwest);
        tt1.setNorthWest(tt1Northwest);
        tt2.setNorth(tt2North);
        tt2.setNorthEast(tt2Northeast);
        tt2.setSouthEast(tt2Southeast);
        tt2.setSouth(tt2South);
        tt2.setSouthWest(tt2Southwest);
        tt2.setNorthWest(tt2NorthWest);
        tt1.placeMeeple(whitePlayer);
        tt2.placeMeeple(whitePlayer);
        boolean containEntireSettlements = map.containEntireSettlements(tt1, tt2);
        Assert.assertTrue(containEntireSettlements);
    }

    @Test
    public void testContainEntireSettlementsGivenNoEntireSettlementsAnd1TileOccupied() {
        WhitePlayer whitePlayer = new WhitePlayer("White Player", map, null);
        TerrainTile tt1 = new Grass();
        TerrainTile tt2 = new Grass();
        TerrainTile tt1North = new Grass();
        TerrainTile tt1Northeast = new Grass();
        TerrainTile tt1Southeast = new Grass();
        TerrainTile tt1South = tt2;
        TerrainTile tt1Southwest = new Grass();
        TerrainTile tt1Northwest = new Grass();
        TerrainTile tt2North = tt1;
        TerrainTile tt2Northeast = tt1Southeast;
        TerrainTile tt2Southeast = new Grass();
        TerrainTile tt2South = new Grass();
        TerrainTile tt2Southwest = new Grass();
        TerrainTile tt2NorthWest = tt1Southwest;
        tt1.setLevel(1);
        tt2.setLevel(1);
        tt1North.setLevel(1);
        tt1Northeast.setLevel(1);
        tt1Southeast.setLevel(1);
        tt1Southwest.setLevel(1);
        tt1Northwest.setLevel(1);
        tt2Southeast.setLevel(1);
        tt2South.setLevel(1);
        tt2Southwest.setLevel(1);
        tt1.setNorth(tt1North);
        tt1.setNorthEast(tt1Northeast);
        tt1.setSouthEast(tt1Southeast);
        tt1.setSouth(tt1South);
        tt1.setSouthWest(tt1Southwest);
        tt1.setNorthWest(tt1Northwest);
        tt2.setNorth(tt2North);
        tt2.setNorthEast(tt2Northeast);
        tt2.setSouthEast(tt2Southeast);
        tt2.setSouth(tt2South);
        tt2.setSouthWest(tt2Southwest);
        tt2.setNorthWest(tt2NorthWest);
        tt1.placeMeeple(whitePlayer);
        tt1North.placeMeeple(whitePlayer);
        boolean containEntireSettlements = map.containEntireSettlements(tt1, tt2);
        Assert.assertFalse(containEntireSettlements);
    }

    @Test
    public void testContainEntireSettlementsGivenNoEntireSettlementsAndBothTilesOccupied() {
        WhitePlayer whitePlayer = new WhitePlayer("White Player", map, null);
        TerrainTile tt1 = new Grass();
        TerrainTile tt2 = new Grass();
        TerrainTile tt1North = new Grass();
        TerrainTile tt1Northeast = new Grass();
        TerrainTile tt1Southeast = new Grass();
        TerrainTile tt1South = tt2;
        TerrainTile tt1Southwest = new Grass();
        TerrainTile tt1Northwest = new Grass();
        TerrainTile tt2North = tt1;
        TerrainTile tt2Northeast = tt1Southeast;
        TerrainTile tt2Southeast = new Grass();
        TerrainTile tt2South = new Grass();
        TerrainTile tt2Southwest = new Grass();
        TerrainTile tt2NorthWest = tt1Southwest;
        tt1.setLevel(1);
        tt2.setLevel(1);
        tt1North.setLevel(1);
        tt1Northeast.setLevel(1);
        tt1Southeast.setLevel(1);
        tt1Southwest.setLevel(1);
        tt1Northwest.setLevel(1);
        tt2Southeast.setLevel(1);
        tt2South.setLevel(1);
        tt2Southwest.setLevel(1);
        tt1.setNorth(tt1North);
        tt1.setNorthEast(tt1Northeast);
        tt1.setSouthEast(tt1Southeast);
        tt1.setSouth(tt1South);
        tt1.setSouthWest(tt1Southwest);
        tt1.setNorthWest(tt1Northwest);
        tt2.setNorth(tt2North);
        tt2.setNorthEast(tt2Northeast);
        tt2.setSouthEast(tt2Southeast);
        tt2.setSouth(tt2South);
        tt2.setSouthWest(tt2Southwest);
        tt2.setNorthWest(tt2NorthWest);
        tt1.placeMeeple(whitePlayer);
        tt1North.placeMeeple(whitePlayer);
        tt2.placeMeeple(whitePlayer);
        boolean containEntireSettlements = map.containEntireSettlements(tt1, tt2);
        Assert.assertFalse(containEntireSettlements);
    }

    @Test
    public void testContainEntireSettlementsGivenSize1SettlementAndAdjacentEnemySettlement() {
        WhitePlayer whitePlayer = new WhitePlayer("White Player", map, null);
        BlackPlayer blackPlayer = new BlackPlayer("Black Player", map, null);
        TerrainTile tt1 = new Grass();
        TerrainTile tt2 = new Grass();
        TerrainTile tt1North = new Grass();
        TerrainTile tt1Northeast = new Grass();
        TerrainTile tt1Southeast = new Grass();
        TerrainTile tt1South = tt2;
        TerrainTile tt1Southwest = new Grass();
        TerrainTile tt1Northwest = new Grass();
        TerrainTile tt2North = tt1;
        TerrainTile tt2Northeast = tt1Southeast;
        TerrainTile tt2Southeast = new Grass();
        TerrainTile tt2South = new Grass();
        TerrainTile tt2Southwest = new Grass();
        TerrainTile tt2NorthWest = tt1Southwest;
        tt1.setLevel(1);
        tt2.setLevel(1);
        tt1North.setLevel(1);
        tt1Northeast.setLevel(1);
        tt1Southeast.setLevel(1);
        tt1Southwest.setLevel(1);
        tt1Northwest.setLevel(1);
        tt2Southeast.setLevel(1);
        tt2South.setLevel(1);
        tt2Southwest.setLevel(1);
        tt1.setNorth(tt1North);
        tt1.setNorthEast(tt1Northeast);
        tt1.setSouthEast(tt1Southeast);
        tt1.setSouth(tt1South);
        tt1.setSouthWest(tt1Southwest);
        tt1.setNorthWest(tt1Northwest);
        tt2.setNorth(tt2North);
        tt2.setNorthEast(tt2Northeast);
        tt2.setSouthEast(tt2Southeast);
        tt2.setSouth(tt2South);
        tt2.setSouthWest(tt2Southwest);
        tt2.setNorthWest(tt2NorthWest);
        tt1.placeMeeple(whitePlayer);
        tt1North.placeMeeple(blackPlayer);
        boolean containEntireSettlements = map.containEntireSettlements(tt1, tt2);
        Assert.assertTrue(containEntireSettlements);
    }
}
