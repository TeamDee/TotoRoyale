package GameModel.Map;
package GameControl.Player;

import GameControl.Player.WhitePlayer;
import GameModel.Map.BoardSpace;
import GameControl.Player.WhitePlayer;
import GameModel.Map.Coordinates.AxialCoordinate;
import GameModel.Map.Tile.HexTile;
import GameModel.Map.Tile.TerrainTile;
import GameModel.Map.Tile.TerrainType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by eb on 3/27/2017.
 */
public class WhiteSettlementTest {
            private GameMap map;
            private BoardSpace boardspace;
            private WhitePlayer whitePlayer;
            private static AxialCoordinate location;

            //WHITE MEEPLE SETTLEMENT
            @Before
            public void Game_map_is_init() {
                map = new GameMap();
                location = new AxialCoordinate(0, 0);
                BoardSpace boardspace = new BoardSpace(location, map);
                boardspace.getLevel();

            }

            @Test
            public void check_terrain() {
                map = new GameMap();
                whitePlayer = new WhitePlayer();
                ArrayList<HexTile> tiles = map.getVisible();
                for (HexTile ht : tiles) {
                    if (ht.terrainType() == TerrainType.VOLCANO)
                        // new WhitePlayer().buildSettlement((TerrainTile)ht);
                        whitePlayer.buildSettlement((TerrainTile) ht);
                    break;
                }

                @After
                public void cleanUp () {

                }


            }
        }

