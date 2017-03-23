Feature: Deck
  Deck contains 48 TriHexTiles at the beginning of the game.

  Scenario:
    Given The game is initialized.
    When The deck is created.
    Then I get a shuffled deck.

