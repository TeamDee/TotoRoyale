package GameModel.Map;

import GameControl.Placement;
import GameControl.Player.Player;
import GameModel.Map.Coordinates.AxialCoordinate;
import GameModel.Map.Tile.Grass;
import GameModel.Map.Tile.Rock;
import GameModel.Map.Tile.VolcanoTile;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by eb on 3/24/2017.
 */
public class TriHexTileTest {
    private TriHexTile tht;
    private AxialCoordinate location;
    private GameMap map;
    private Player player;
    private ArrayList<Placement> placements;


    @Before
    public void create_tile() {
        location = new AxialCoordinate(0, 0);

    }

    @Test
        public void create_levels() {
        map = new GameMap();
        player = new Player();
        placements = map.getLegalMapPlacements(tht);
    }

    @After
        public void check_tile_placement() {
            for (Placement p: placements)
                Assert.assertTrue(p.volcanoMatch());

        }



}
