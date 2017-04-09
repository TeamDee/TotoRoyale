Feature: Tiger
  Scenario: checking level placement
    Given it is my turn to make a legal move
    When I place a tiger
    Then it must be on level 5 or greater
