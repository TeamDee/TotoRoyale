Feature: Map
  Description:
    Testing the map
  Scenario Outline: placing first tile
    Given I have <game map> game map
    And I place <triHexTiles> TriHexTile
    When I go to place TriHexTile
    Then the gameMap should have <HexTiles_on_Map> hextiles

    Examples:
      | game map  | triHexTiles | HexTiles_on_Map|
      | 1         | 0           | 0              |
      | 1         | 1           | 3              |
      | 1         | 2           | 6              |
      | 0         | 0           | 0              |


