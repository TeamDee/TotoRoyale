Feature: Settlement
  Description: Settlements are created by meeple placements and are used in score counting.
  Scenario: Founding a settlement
    Given I have a game map initalized
    When I am on level 1
    Then a tile must be placed on a non-volcano terrain

    Scenario: Founding a settlement (continued)
      Given a game map is initalized
      When I have exsisting settlements
      Then I cannot found a settlement on an exsisting settlement

