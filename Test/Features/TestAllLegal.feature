package GameModel.Map;

import GameModel.Map.Coordinates.AxialCoordinate;
import GameModel.Map.Tile.Grass;
import GameModel.Map.Tile.Rock;
import GameModel.Map.Tile.VolcanoTile;
import org.junit.*;

private <ArrayList> placements

@GIVEN("^a list of placements$")
  map = new GameMap();
  location = new AxialCoordinate(0, 0);
  BoardSpace boardSpace = new BoardSpace(location,map);
  Assert.assertFalse(boardSpace.hasTile());
  Assert.assertTrue(boardSpace.getLocation().compare(location));
}

  @WHEN("^all placements are legal$")
  for (placement p


  @THEN("^no placements are illegal$")