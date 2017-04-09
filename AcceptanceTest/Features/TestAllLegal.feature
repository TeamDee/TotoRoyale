Feature: Placement
  Scenario: Check to see legal placements
    Given a list of placements
    When all placements are legal
    Then no placements are illegal
