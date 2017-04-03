Feature: BoardSpace
  Description:
  BoardSpace is an object holding the reference to the GameMap object and the container for the Deck object.
#  Scenario: The user is in placement phase and decides to build on another level
#    Given I have a <game map> game map initialized
#    When I place a <triHexTiles> TriHexTile
#    And it is ontop of two or more <triHexTiles> TriHexTiles of the same <level> level
#    And the <volcano> Volcano tile lies atop an exsisting <volcano>
#    Then the user's placement is legal.
#    And the <Boardspace> BoardSpace should account for the newly placed tile.
#
#
#  Scenario: The user is in placement phase and decides to build another level
#    Given I have <BoardSpace> BoardSpace
#    When I place a <triHexTiles> TriHexTile directly on top a <triHexTiles> TriHexTiles
#    And it is an illegal placement
#    Then return null;

  Scenario: The user is in placement phase and decides to place a tile
    Given  I have a game map game map initialized
    And  there are TriHexTiles already placed on the board
    When I place a  TriHexTile adjacent to an exisiting TriHexTile
    Then the user's placement is legal

#  Scenario: The user is in placement phase and decides to place a tile illegaly
#    Given  I have a game map initialized
#    And  TriHexTiles are already placed on the board
#    When I do not place a  TriHexTile adjacent to an exisiting TriHexTile
#    Then the user's placement is illegal
