# ThreeTriosGame
Three Trios Game Model

Overview
This codebase provides a model for the Three Trios game, a two-player card game
on a customizable grid from a configuration file. Players take turns placing cards
on a grid and the card battles based on directional attack values.
The goal is to own the most cards by the end of the game. This implementation supports
custom board and deck configurations, combo attacks using recursion/memoization, and game
state rendering.

Play the ThreeTrios Game:
/ThreeTrios/artifacts/ThreeTrios_jar path
Player types: human, strategy1, strategy2, strategy3
Run: java -jar ThreeTrios.jar (player types) (player types)

Quick Start
1. Set up the model: Initialize and start the game with configuration
files for both the board and the deck.
    ThreeTriosGridModel model = new ThreeTriosGridModel();
    model.startGame(“directory, "deck.config", “directory”, "board.config");
2.	Game actions: Players can place cards on the grid and initiate battles
based on attack values.
    model.playCardToCardCell(redHand.get(0), 0, 0);
    model.playCardToCardCell(blueHand.get(0), 1,0);


Key Components

1. Main Game Model (ThreeTriosGridModel)
Purpose: Core game logic, managing player turns, card placement, battles, and the game's progression.
Responsibilities:
Reads board and deck configurations.
Manages player turns and enforces turn-based rules.
Manages card placement and ensures valid moves.
Handles battle logic for adjacent cards and flips ownership based on attack values.
Tracks game-over conditions and determines the winner.

2. Card Representation (Card)
Purpose: Represents a game card, including attack values and owner.
Attributes:
name: Unique identifier for each card.
attack values (North, South, East, West): Directional attack power.
owner: Tracks the card's current owner (RED, BLUE, or NULL_PLAYER).

3. Cell Types (Cell, CardCell, HoleCell)
Purpose: Represents cells within the grid.
Cell: Interface for general cell functionality.
CardCell: Player can play to a cardCell if it is not occupied.
HoleCell: Cells that are permanently empty, representing impassable grid areas.
Responsibility: Distinguish between playable cells (CardCell) and non-playable cells
(HoleCell), facilitating flexible grid designs.

4. Player Enum (Player)
Purpose: Enum representing players in the game (RED and BLUE)
and a NULL_PLAYER option for unassigned cards.
Responsibility: Tracks player ownership of each card and defines which player is active.

5. Direction Enum (Direction)
Purpose: Enum for attack directions (NORTH, SOUTH, EAST, WEST) and includes functionality
for finding the opposite direction.
Responsibility: Provides directional context to battles, allowing the model to
compare attack values correctly.

6. Helper Classes and Enums
Position: Represents a coordinate on the grid (row and column).
AttackValues: integer-based attack values, values from 1 to 10. Game ensures that
no other values can be played since enums!
CellType: Enum distinguishing between CARD_CELL and HOLE,
used in board configuration parsing.

7. View (ThreeTriosTextView)
Purpose: Provides a text-based view of the game, rendering the grid and the
current player's hand in a readable format.
Responsibility: Reads the current state from the model and outputs it in a
structured format. Used in testing.
Source Organization
src/cs3500/threeTrios/model/: Contains all model-related classes, interfaces, and enums.
src/cs3500/threeTrios/view/: Contains the text view implementation for rendering the game state.
docs/: Stores sample configuration files for decks and boards, allowing different game setups.
tests/: Contains JUnit tests for validating the model's functionality, covering various edge
cases and game plays.

8. Testing
Game Start: Validates that game initialization works with configuration files.
Player Actions: Tests for card placement, hand updates, and invalid move attempts.
Battle Phase: Ensures adjacent cards are correctly flipped based on attack comparisons.
Win Conditions: Verifies the game-over state and winner determination.
Error Handling: Covers exceptions for invalid inputs and configurations.

Directory Structure Map:
ThreeTrios/
	docs/
		board.config        # Board configuration file with holes and connections.
		boardNotReachCC.config      # Board with holes, making some cells unreachable.
		boardWithHoleAllCCCanReach.config # Board with holes, ensuring all card cells are reachable.
		boardWithNoHoles.config     # Board with no holes, for straightforward gameplay.
		cardsWorksWithAll.config        # Deck configuration compatible with all board types.
		cardsWorksWithOne.config        # Deck configuration compatible with specific board types.
		README      # Documentation file describing the codebase and its setup.
	src/
		cs3500.threeTrios/
		model/hw05/     # Contains model classes and enums for game logic.
		AttackValues.java       # Enum for attack values (1-10) used on cards.
		Card.java       # Card class defining card attributes and ownership.
		CardCell.java       # Class representing a cell that can hold a card.
		Cell.java       # Interface for cell functionality.
		CellType.java       # Enum distinguishing cell types (CARD_CELL, HOLE).
		Direction.java      # Enum for card attack directions (NORTH, SOUTH, etc.).
		GameGridModel.java      # Interface for the game model.
		HoleCell.java       # Class representing a non-playable cell.
		Player.java     # Enum representing players (RED, BLUE, NULL_PLAYER).
		Position.java       # Class representing coordinates on the game grid.
		ReadOnlyTTModel.java        # Read-only model interface for safe view access.
		ThreeTriosGridModel.java        # Main game model implementing core logic and rules.
	view/hw05/      # Contains view classes for rendering the game state.
		ThreeTriosTextView.java     # Text-based view class for console rendering.
		ThreeTriosView.java     # Interface for the game view.
	test/
		cs3500.threeTrios.model.hw05/    # Contains test classes for validating model functionality.
		TestGameStateToString.java      # Tests for the game's string representation.
		TTModelReadCardBoardTest.java       # Tests for reading card and board configurations.

Changes for part 2:

Class Invariant: The total number of cards in the deck must be at least N+1, where N is the
number of card cells in the grid. This ensures both players can be dealt equal hands of size
((N+1)/2 cards at game start). This ensures all card cells can be filled.
This invariant is established in the startGame method and maintained throughout the game by:
1. The readCardConfig method verifies there are enough cards when reading the deck
2. The readGridConfig method counts the number of card cells and verifies it's odd
3. The ensureEnoughCards helper enforces the N+1 minimum card requirement
4. The dealCards method distributes (N+1)/2 cards to each player's hand
5. The playCardToCardCell method maintains this by only allowing legal card placements

Split functionality between ReadOnlyTTModel and GameGridModel interfaces
ReadOnlyTTModel now contains all observation methods
GameGridModel extends ReadOnlyTTModel and adds mutation methods
We improved this idea because it ensures views can only observe but not modify game state

Deleted Card Ownership because the card should not worry about who owns the card.
The card should only be represented by the name and the attack values.
Updated getOwner() to where it is the cell not based on the card.


------

The codebase implements the advanced extra credit strategies in the following files:

HardToFlip.java - This implements strategy #3 from the assignment.
This is found in cs3500/threetrios/strategy/HardToFlip.java
Our implementation looks at each card and position combination
to minimize exposure to opponent flips.
Breaks ties using uppermost-leftmost position.

New Classes:

GridPanel:
- This class represents the graphical view of the game grid. It listens for mouse clicks and
    visually updates based on the current game state.
- It implements the GridPanelGUI interface and handles the logic for rendering the grid cells,
    highlighting cells, and interacting with grid positions.
         paintComponent(Graphics g) paints the grid and the cards in the grid
         handleGridClick(int row, int col) handles clicks on grid cells and sends info to listener.

GridPanelGUI: (interface)
- This interface defines the methods for handling grid interactions in the game view.
    handleGridClick(int row, int col) handles a grid cell click
    refresh() refreshes the grid panel to reflect any changes

PlayerHandPanel:
- Represents the hand of a player and displays the cards in the player’s hand.
- It listens for mouse clicks and allows players to select a card from their hand
    paintComponent(Graphics g) draws the cards in the player’s hand
    handleCardClick(int clickedIndex) handles card selection and deselection from the player's hand
    refresh() refreshes the hand panel to update the displayed cards

PlayerHandPanelGUI: (interface)
- This interface defines methods for handling player hand interactions in the game view.
    handleCardClick(int index) handles clicking on a card in the player’s hand
    refresh() refreshes the player’s hand display
    getSelectedCardIndex() returns the index of the currently selected card

ThreeTriosGUI:
- Main GUI class for the ThreeTrios game. It sets the window and layout(grid an hand panels).
- It also manages the game state and updates the display based on interactions.
    refresh() refreshes the entire GUI, including the current player's turn, grid, and player hands
    cardClickedListener(Player player, int cardIndex) handles card selection/deselection in hand
    gridCellClickedListener(int row, int col): Handles grid cell clicks

ViewListener: (interface)
- This interface listens for interaction events such as card clicks and grid cell clicks.
- the classes that implement this use it to respond to user actions
    cardClickedListener(Player player, int cardIndex) called when a player clicks on card in hand
    gridCellClickedListener(int row, int col) called when a player clicks on a grid cell

Corner, HardToFlip*, MaxCardFlip classes in "strategy"
- These classes are for the strategies. HardToFlip is our extra credit class
- These all implement StrategicThreeTrios (interface)
    - This interface allows user/player to apply various strategies in the game
    - For example, choosing the optimal card, deciding the best position on the grid.
- These strategies rely on the ReadOnlyTTModel to assess the current game state
    - for ex. (legal moves, the current player's turn, and the board layout)
-The MaxCardFlip class is a strategy that selects the best card and position for a player by
    maximizing the number of flips. It checks all possible positions for each card in the player's
    hand and selects the one that flips the most cards
        - The strategy looks at all positions on the board and all cards in the player's hand.
        - It calculates how many flips each card would make at each position
        - The card and position that result in the most flips are chosen
        - If there’s a tie, the uppermost-leftmost position is preferred
- The MaxCardFlip
    - The Corner strategy focuses on corner positions on the grid, where only two sides are exposed
        for attack.
    - This strategy selects cards based on their attack values for the specific corner
        position.
            - The strategy looks for corner positions on the grid, which have only two exposed
                attack directions.
            - It selects the best card from the player’s hand based on the exposed attack values
                for the optimal corner position.
            - If no corner position is available, the strategy chooses the uppermost-leftmost legal
                position and selects the first card in the hand.
- Extra credit HardToFlip
    - The HardToFlip strategy aims to select cards with the highest total attack values and places
        them in positions with the least exposure to enemy flips. The goal is to make the placed
        cards harder for the opponent to flip.
        - Card Selection: The strategy selects the card with the highest total attack value,
            ensuring that the card has the greatest offensive power.
        - Position Selection: It chooses the position with the least exposure to potential flips.
            The fewer sides exposed, the harder it will be for the opponent to flip the card.
        - Tie Breaker: In case of multiple cards or positions having the same attack value or
            exposure, the strategy breaks ties by choosing the card or position with the smallest
            index (uppermost-leftmost).
- So for each, the card selection and position selection can be different as each strategy requires
    user to play strategically differently. Position could matter depending on each specific
    strategy.

For mocks (CornerStrategy -> MaxCardFlip -> HardToFliP)
- These are essentially mocks to help us with testing. They provide controlled and simplified
  versions of the game.
    - Very good for when we test edge cases and behaviors of the strategies we have above without
      the need of running the whole game logic.

For testing mocks (strategies):

- testCornerStrategyPrefersCorners:
    - Ensures the strategy prefers corner positions (only two exposed sides for attack).
    - Expected Outcome: The selected position should be a corner.

- testCornerStrategySelectsHighValueCard:
    - Verifies the strategy selects the card with the highest attack values.
    - Expected Outcome: The strategy should choose the higher-value card (mockCard2).

- testCornerStrategyBreaksTiesWithPosition:
    - Checks if the strategy breaks ties by choosing the uppermost-leftmost position when multiple
      corner positions are tied.
    - Expected Outcome: The strategy should select the position (0, 1).

- testGameOverCheck:
    - Tests that the strategy handles the game-over state by throwing an exception if a move is
      attempted when the game is over.
    - Expected Outcome: An IllegalStateException should be thrown.

- testNoLegalMoves:
    - Simulates a scenario with no legal moves left and ensures the strategy returns null.
    - Expected Outcome: The strategy should return null when no legal moves are available.

- testMaxFlipFindsHighestFlip:
    - Ensures that the strategy selects the position with the highest number of potential flips.
    - Expected Outcome: The strategy should choose the position (1, 1) for mockCard3, as it results
      in
      the highest number of flips.

- testMaxFlipSelectsBestCard:
    - Verifies that the strategy selects the card with the highest attack values that results in
      the most flips.
    - Expected Outcome: The strategy should select mockCard3 as the best card.

- testMaxFlipBreaksTiesWithPosition:
    - Tests that if multiple positions have the same number of flips, the strategy picks the
      uppermost-leftmost position.
    - Expected Outcome: The strategy should choose the position (0, 0) if there is a tie in the
      number of flips.

- testEmptyHand:
    - Simulates a scenario where the player has an empty hand and verifies the behavior of the
      strategy.
    - Expected Outcome: The strategy should return null since no cards are available to play.

- testMultipleMaxFlipPositions:
    - Checks if the strategy correctly handles multiple positions that result in the same maximum
      flips.
    - Expected Outcome: The strategy should pick the position (0, 1) in case of a tie in flip count.

-testNullModel:
    - Tests if the strategy throws an exception when a null model is passed.
    - Expected Outcome: The strategy should throw a NullPointerException.

- testNullPlayer:
    - Verifies that the strategy handles a null player input properly.
    - Expected Outcome: A NullPointerException should be thrown when the player is null.

- testNullCard:
     - Verifies that the strategy handles a null card input properly.
     - Expected Outcome: A NullPointerException should be thrown when the card is null.

- testGameOverCheck:
    - Simulates a game-over condition and ensures that the strategy doesn't make any moves
      after the game has ended.
    - Expected Outcome: The strategy should throw an IllegalStateException.

Changes for part 3
Added GameStatusListener: “private List<GameStatusListener> modelListeners;”
MVC Integration: this allows the controller to be notified automatically of key game events
without explicitly polling the model for updates. This also helps good to change turns method
and game over and final score notifications. Reaction will all be immediate

Added “if (row < 0 || row >= numRows || col < 0 || col >= numCols) { return false; }”
Simplified Logic for AI and player action handling
Instead of just solely sending exceptions, because now the controller needs to be implemented,
we can just say that a legal move is portrayed by a boolean (true vs false). It's easier for
the methods to rely on the fact if the move is allowed or not at any given time for a player
or a computer. Also for strategic handling. This is good for strategy evaluation at a given time
to be handled with a boolean.This essentially makes it easier to go through all the dimensions
of legal moves in the methods and not make it so complex.

Added: “int cardIndex = playerHand.indexOf(card); if (cardIndex == -1) {
throw new IllegalArgumentException("Card not in current player's hand");}
 playerHand.remove(cardIndex);”
Good for card removal (controller can be more efficient in handling user interaction)
This makes sure a card is played, the GUI is updated correctly (good mirroring essentially)

Added “currentPlayer = (currentPlayer == Player.RED) ? Player.BLUE : Player.RED;
for (GameStatusListener listener : modelListeners) { listener.notifyTurnChange(currentPlayer); }}"
Controller needs to know when the turn is over so that GUI updates.
This is so that also the computer plays on its own and isn’t acting like the human one.

Added “int finalScore = (gameWinner == Player.NULL_PLAYER) ? redCards :
(gameWinner == Player.RED ? redCards : blueCards); for(GameStatusListener
listener : modelListeners) { listener.notifyGameOver(gameWinner, finalScore); }”
This decision by us is so that the user is notified when the game ends
(like the winner and final score also being registered).
Also so that the controller can handle end of game state so that no other movies
can be played (follows the game logic).

Controller (hw7)
Interfaces
1. GameStatusListener
    - Interface for listening to game status events like turn changes and game completion.
    - notifyTurnChange(Player currentPlayer): called when player turns change
    - notifyGameOver(Player winner, int score): called when game ends
    - notifyInvalidMove(String message): called when an invalid move is attempted

2. PlayerActionListener
    - Interface for listening to player actions during the game.:
    - notifyCardSelected(Player player, int cardIndex): called when a player selects a card
    - notifyPositionSelected(int row, int col): called when a player selects a grid position

3. ThreeTriosPlayer
    - interface used for both the human and machine players
    - addPlayerActionListener(ViewListener listener): registers listener for player actions
    - performModelAction(ReadOnlyTTModel model): executes player move based on type
    - getTurn(): returns which player red/blue
    - isHuman(): differentiates between human and computer players

Classes
1. ThreeTriosController
    - Main controller class managing game flow and player interactions.
    - what it does essentially
        - Mediates between model, view, and player
        - Enforces game rules and turn order
        - Handles player move validation
        - Updates view state
        - game over conditions
        - Coordinates between human and machine players
    - Maintains separate views for each player
    - Validates moves before execution
    - Shows error messages for invalid moves
    - Updates view titles based on game state
    - Handles game over notifications

2. HumanPlayer
    - Represents a human player in the game.
    - Implements ThreeTriosPlayer interface
    - Allows user input through GUI
    - Does not automatically make moves
    - Validates player identity
    - Manages action listeners

3. MachinePlayer
    - Represents an AI player using a strategy.
    - Implements ThreeTriosPlayer interface
    - Uses StrategicThreeTrios for move decisions
    - Automatically executes moves on its turn
    - Validates moves before execution
    - Notifies listeners of actions taken

For design patterns we used for the controller impl etc...:

Observer Pattern:
    - GameStatusListener for game state changes
    - PlayerActionListener for player actions
    - ViewListener for GUI interactions






