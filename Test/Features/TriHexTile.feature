Feature: TriHexTile
  Description:
    A TriHexTile groups three Tile objects.
  Scenario: create a TriHexTile
    Given The game is up and initializing
    When A TriHexTile get created
    Then The TriHexTile is initialized with three Tiles and one has to be VocanoTile

  Scenario: triHexTile rotation
    Given The game is up and running
    When A player rotates one TriHexTile
    Then The three Tiles rotate their positions clock-wisely for one

  Scenario: