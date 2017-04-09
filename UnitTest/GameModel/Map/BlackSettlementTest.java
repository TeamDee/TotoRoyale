package GameModel.Map;

import GameControl.Player.BlackPlayer;
import GameModel.Map.Coordinates.OffsetCoordinate;
import GameModel.Map.Tile.HexTile;
import GameModel.Map.Tile.TerrainTile;
import GameModel.Map.Tile.TerrainType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class BlackSettlementTest {
    private GameMap map;
    private BoardSpace boardspace;
    private BlackPlayer blackPlayer;
    private static OffsetCoordinate location;

        //WHITE MEEPLE SETTLEMENT
        @Before
        public void Game_map_is_init_black() {
                map = new GameMap();
                location = new OffsetCoordinate(0, 0);
                BoardSpace boardspace = new BoardSpace(location, map);
                boardspace.getLevel();

                }

        @Test
        public void check_terrain_black(){
            map=new GameMap();
            blackPlayer=new BlackPlayer("Black Player", map, null);
            ArrayList<HexTile>tiles=map.getVisible();
            for(HexTile ht:tiles){
                if(ht.terrainType() !=TerrainType.VOLCANO)
                // new WhitePlayer().buildSettlement((TerrainTile)ht);
                blackPlayer.buildSettlement((TerrainTile)ht);
                break;
            }
            }

    @After
    public void cleanUp () {

    }

}

