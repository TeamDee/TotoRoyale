package GameControl.Player;

import GameModel.Map.Contiguous.Settlement;
import GameModel.Map.GameMap;
import GameModel.Map.Tile.Grass;
import GameModel.Map.Tile.Rock;
import GameModel.Map.Tile.TerrainTile;
import GameModel.Map.Tile.VolcanoTile;
import GameModel.Map.TriHexTile;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by Z_K on 3/26/2017.
 */
public class PlayerTest {
    private static GameMap map;
    private static Player player1;

    @BeforeClass
    public static void setUp(){
        map = new GameMap();
        player1 = new WhitePlayer("WhitePlayer", map, null);
    }

    @Test
    public void playerTakeTurnTest(){
        int numberOfBoardSpacesBefore = map.getNumberOfBoardSpaces();
        TriHexTile tht = new TriHexTile(new Grass(), new Rock(), new VolcanoTile());
        player1.takeTurn(map, tht);
        Assert.assertFalse(map.getNumberOfBoardSpaces()==numberOfBoardSpacesBefore);
    }

    @Test
    public void testNukeSettlementsGivenSplitSettlements() {
        TerrainTile north = new Grass();
        TerrainTile center = new Grass();
        TerrainTile south = new Grass();
        Settlement settlement = new Settlement();
        north.setLevel(1);
        center.setLevel(1);
        south.setLevel(1);
        center.setNorth(north);
        center.setSouth(south);
        north.setSouth(center);
        south.setNorth(center);
        north.placeMeeple(player1);
        center.placeMeeple(player1);
        south.placeMeeple(player1);
        settlement.addToSettlement(north);
        settlement.addToSettlement(center);
        settlement.addToSettlement(south);
        player1.clearSettlements();
        player1.addSettlement(settlement);
        Assert.assertEquals(3, settlement.getSettlementSize());
        player1.nukeSettlements(center);
        ArrayList<Settlement> playerSettlements = player1.getSettlements();
        Assert.assertEquals(2, playerSettlements.size());
        Assert.assertEquals(1, playerSettlements.get(0).getSettlementSize());
        Assert.assertEquals(1, playerSettlements.get(1).getSettlementSize());
    }

    @Test
    public void testNukeSettlementsGivenNoSplitSettlements() {
        TerrainTile north = new Grass();
        TerrainTile center = new Grass();
        TerrainTile south = new Grass();
        Settlement settlement = new Settlement();
        north.setLevel(1);
        center.setLevel(1);
        south.setLevel(1);
        center.setNorth(north);
        center.setSouth(south);
        north.setSouth(center);
        south.setNorth(center);
        north.placeMeeple(player1);
        center.placeMeeple(player1);
        south.placeMeeple(player1);
        settlement.addToSettlement(north);
        settlement.addToSettlement(center);
        settlement.addToSettlement(south);
        player1.clearSettlements();
        player1.addSettlement(settlement);
        Assert.assertEquals(3, settlement.getSettlementSize());
        player1.nukeSettlements(south);
        ArrayList<Settlement> playerSettlements = player1.getSettlements();
        Assert.assertEquals(1, playerSettlements.size());
        Assert.assertEquals(2, playerSettlements.get(0).getSettlementSize());
    }
}
