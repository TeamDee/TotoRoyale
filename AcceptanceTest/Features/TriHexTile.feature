Feature: TriHexTile
  Description:
    A TriHexTile groups three Tile objects.
 Scenario: create a TriHexTile
   Given The game is up and initializing
    When A TriHexTile get created
    Then The TriHexTile is initialized with three Tiles and one has to be VolcanoTile



  Scenario:TriHexTile level Placement
    Given The game is initialized
    And TriHexTiles exist on the board
    When a user places a TriHexTile with intentions to build a new level
    Then one volcano must be atop another volcano
