Feature: Placement
  Scenario: Check to see legal placements
    Given When I try to place something
    When I make the move
    Then I get a list of legal placemetns
