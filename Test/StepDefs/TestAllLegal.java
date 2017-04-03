package StepDefs;

import GameModel.Map.GameMap;

/**
 * Created by eb on 3/29/2017.
 */
public class TestAllLegal {


private <ArrayList> placements

@GIVEN("^a list of placements$")
  map = new GameMap();
          location = new AxialCoordinate(0, 0);
          BoardSpace boardSpace = new BoardSpace(location,map);
          Assert.assertFalse(boardSpace.hasTile());
          Assert.assertTrue(boardSpace.getLocation().compare(location));
          }

@WHEN("^all placements are legal$")
  for (Placement p : placements) {
        Assert.assertTrue(p.isLegal)



@THEN("^no placements are illegal$")


}