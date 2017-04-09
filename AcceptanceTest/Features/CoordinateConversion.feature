Feature: Coordinate Conversion
  Scenario Outline: We need to convert our coordinate system to cubical coordinate system, or from cubical coordinate system
    Given I need to convert one of our coordinate objects with coordinates: <row> <col>
    When I convert it into cubical coordinate object
    Then I have cubical coordinates: <x> <y> <z>

    Examples:
      | row   | col   | x   | y   | z   |
      | 1     | -3    | 1   | 1   | -2 |
      | 2     | -4    | 2   | 1   | -3 |
      | 0     | 6     | 0   | -3 |  3|
      | -1    | 5     | -1  | -2 | 3  |

  Scenario Outline: We need to convert cubical coordinate system to suit our Offset Coordinate system
    Given I need to convert cubic object with: <x> <y> <z>
    When I convert it into offset coordinate object
    Then I have offset coordinates: <row> <col>

    Examples:
    |   x   |   y   |   z   |   row   |   col   |
    |   1   |   1   |   -2  |   1     |   -3    |
    |   2   |   1   |   -3   |  2     |   -4    |
    |   0   |   -3   |  3    |  0     |   6     |
    |   -1   |  -2    | 3    |   -1   |   5     |


