package GameModel.Map;

import GameControl.Player.Player;
import GameControl.Player.WhitePlayer;
import GameModel.Map.Contiguous.Settlement;
import GameModel.Map.Tile.Grass;
import GameModel.Map.Tile.Rock;
import GameModel.Map.Tile.TerrainTile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by conor on 4/9/2017.
 */
public class SettlementTest {
    public static TerrainTile north;
    public static TerrainTile center;
    public static TerrainTile south;
    public static TerrainTile northeast;
    public static TerrainTile farNorth;
    public static Settlement settlement;
    public static Player player;

    @BeforeClass
    public static void setUp() {
        center = new Grass();
        north = new Grass();
        northeast = new Rock();
        south = new Grass();
        farNorth = new Grass();
        center.setLevel(1);
        north.setLevel(1);
        northeast.setLevel(1);
        south.setLevel(1);
        farNorth.setLevel(1);
        center.setNorth(north);
        center.setNorthEast(northeast);
        center.setSouth(south);
        north.setNorth(farNorth);
        north.setSouth(center);
        north.setSouthEast(northeast);
        northeast.setNorthWest(north);
        northeast.setSouthWest(center);
        south.setNorth(center);
        farNorth.setSouth(north);
    }

    @Before
    public void setUpTest() {
        center.nuke();
        north.nuke();
        northeast.nuke();
        south.nuke();
        farNorth.nuke();
        settlement = new Settlement();
        player = new WhitePlayer("White Player", null, null);
    }

    @Test
    public void testGetSplitSettlementsAfterNukeGivenNoSplitSettlements() {
        center.placeMeeple(player);
        north.placeMeeple(player);
        northeast.placeMeeple(player);
        south.placeMeeple(player);
        settlement.addToSettlement(center);
        settlement.addToSettlement(north);
        settlement.addToSettlement(northeast);
        settlement.addToSettlement(south);
        ArrayList<Settlement> splitSettlementsAfterNuke = settlement.getSplitSettlementsAfterNuke(south);
        Assert.assertEquals(1, splitSettlementsAfterNuke.size());
        Assert.assertEquals(3, splitSettlementsAfterNuke.get(0).getSettlementSize());
    }

    @Test
    public void testGetSplitSettlementsAfterNukeGiven2SplitSettlements() {
        center.placeMeeple(player);
        north.placeMeeple(player);
        northeast.placeMeeple(player);
        south.placeMeeple(player);
        settlement.addToSettlement(center);
        settlement.addToSettlement(north);
        settlement.addToSettlement(northeast);
        settlement.addToSettlement(south);
        ArrayList<Settlement> splitSettlementsAfterNuke = settlement.getSplitSettlementsAfterNuke(center);
        Assert.assertEquals(2, splitSettlementsAfterNuke.size());
        int largerSettlementSize = -1;
        int smallerSettlementSize = 1000;
        for (Settlement s : splitSettlementsAfterNuke) {
            int settlementSize = s.getSettlementSize();
            if (settlementSize > largerSettlementSize)
                largerSettlementSize = settlementSize;
            if (settlementSize < smallerSettlementSize)
                smallerSettlementSize = settlementSize;
        }
        Assert.assertEquals(2, largerSettlementSize);
        Assert.assertEquals(1, smallerSettlementSize);
    }
}
