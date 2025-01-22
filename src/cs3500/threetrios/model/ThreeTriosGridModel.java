package cs3500.threetrios.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.List;
import java.util.Set;

import cs3500.threetrios.controller.GameStatusListener;

/**
 * Implementation of the GameGridModel interface for the Three Trios game.
 * This model represents a grid-based card game where two players take
 * turns placing and battling cards.
 */
public class ThreeTriosGridModel implements GameGridModel {

  private int numRows;

  private int numCols;

  private Player currentPlayer;

  private Player gameWinner; // Winner of the game.

  private List<Card> redPlayerHand; // Hand (of cards) of the red player.

  private List<Card> bluePlayerHand; // Hand (of cards) of the blue player.

  private boolean isGameStarted; // Is the game started?. Either yes or no.

  private List<GameStatusListener> modelListeners;

  /**
   * Origin (0,0), top-left corner of the grid.
   * Row numbers increase downward (0 to numRows-1).
   * Column numbers increase rightward (0 to numCols-1).
   */
  private List<List<CellType>> grid; // Grid for the cells.

  private boolean isGameWon; // is game won.

  private Random random = new Random(); // Random object. 

  public boolean gameOver; // Is game is over or not. 

  private List<Card> deck; // List for the deck. 

  private List<List<Cell>> cellGrid; // Grid of cells. 

  private int numberOfCardCells; // Number of card cells available. 

  private int numberOfCards; //  Num of cards.

  /**
   * Constructor for the default settings (must be taken in or else game cannot be played).
   * Initializes all instance variables to their default states.
   */
  public ThreeTriosGridModel() {
    this.gameOver = false;
    this.isGameStarted = false;
    this.redPlayerHand = new ArrayList<>();
    this.bluePlayerHand = new ArrayList<>();
    this.grid = new ArrayList<>();
    this.cellGrid = new ArrayList<>();
    this.currentPlayer = Player.RED;
    // Whether the game is won or not.
    this.isGameWon = false;
    this.deck = new ArrayList<>();
    this.numberOfCardCells = 0;
    this.numberOfCards = 0;
    this.modelListeners = new ArrayList<>();
  }

  /**
   * This constructor takes in a random for the game model before the game actual starts.
   *
   * @param random Random object used, must not be null.
   */
  public ThreeTriosGridModel(Random random) {
    if (random == null) {
      throw new IllegalArgumentException("Random cannot be null");
    }
    this.random = random;
  }

  /**
   * This method is to make sure the listener is being added.
   * @param listener to the listener add.
   */
  public void addModelStatusListener(GameStatusListener listener) {
    if (listener == null) {
      throw new IllegalArgumentException("listener cannot be null");
    }
    modelListeners.add(listener);
  }

  /**
   * Reads and parses the grid configuration file to initialize the game grid.
   * Specifies the rows and columns in the grid.
   *
   * @param directory the directory must not be null.
   * @param filename  the name of the grid configuration file cannot be null.
   * @throws IllegalArgumentException if directory or file is null or format is invalid format.
   * @throws IOException              IO error when the reading in the file.
   */
  private void readGridConfig(String directory, String filename) throws IOException {
    if (directory == null || filename == null) {
      throw new IllegalArgumentException("readGridConfig args cannot be null");
    }
    if (isGameStarted) {
      throw new IllegalStateException("Cannot read grid config when game started");
    }

    InputStream inputStream = ThreeTriosGridModel.class.getClassLoader()
            .getResourceAsStream(directory + "/" + filename);

    if (inputStream == null) {
      String filePath = "." + File.separator + directory + File.separator + filename;
      inputStream = new FileInputStream(filePath);
    }

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
      String line;
      if ((line = reader.readLine()) != null) {
        String[] rowCol = line.trim().split("\\s+");
        if (rowCol.length != 2) {
          throw new IllegalArgumentException("Invalid grid dimensions line: " + line);
        }
        try {
          numRows = Integer.parseInt(rowCol[0]);
          numCols = Integer.parseInt(rowCol[1]);
        } catch (NumberFormatException e) {
          throw new IllegalArgumentException("Invalid grid dimensions: " + line);
        }

        grid = new ArrayList<>(numRows);

        for (int row = 0; row < numRows; row++) {
          if ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.length() != numCols) {
              throw new IllegalArgumentException("Invalid number of columns in row " + (row + 1));
            }
            List<CellType> gridRow = new ArrayList<>(numCols);
            for (int col = 0; col < numCols; col++) {
              char c = line.charAt(col);
              if (c == 'C' || c == 'c') {
                numberOfCardCells++;
                gridRow.add(CellType.CARD_CELL);
              } else if (c == 'X' || c == 'x') {
                gridRow.add(CellType.HOLE);
              } else {
                throw new IllegalArgumentException("Invalid character in grid");
              }
            }
            grid.add(gridRow);
          } else {
            throw new IllegalArgumentException("Not enough rows in grid configuration file");
          }
        }
      } else {
        throw new IllegalArgumentException("Empty grid configuration file");
      }
    }
  }

  /**
   * Initializes the cellGrid based on the amount needed to be accounted for.
   * Each cell in the logical grid is converted to an actual cell object.
   */
  private void initializeCellGrid() {
    cellGrid = new ArrayList<>();
    for (int row = 0; row < numRows; row++) {
      List<Cell> cellRow = new ArrayList<>();
      for (int col = 0; col < numCols; col++) {
        if (grid.get(row).get(col) == CellType.CARD_CELL) {
          cellRow.add(new CardCell()); // Empty card cell
        } else {
          cellRow.add(new HoleCell());
        }
      }
      cellGrid.add(cellRow);
    }
  }

  /**
   * Reads card configuration file to initialize the deck of cards.
   * CardName NorthAttack SouthAttack EastAttack WestAttack (of the cards).
   *
   * @param directory the directory where the card config file is located; not be null
   * @param filename  the name of the card configuration file; must not be {@code null}
   * @throws IllegalArgumentException if {@code directory} or f is {@code null},
   *                                  if the game has already started,
   *                                  if there are duplicate card names,
   *                                  or if the file format is invalid
   * @throws IOException              if an IO error occurs while reading the file.
   */
  private void readCardConfig(String directory, String filename) throws IOException {
    if (directory == null || filename == null) {
      throw new IllegalArgumentException("readCardConfig args cannot be null");
    }
    if (isGameStarted) {
      throw new IllegalStateException("Cannot read card config when game started");
    }

    deck = new ArrayList<>();
    Set<String> cardNames = new HashSet<>();

    InputStream inputStream = ThreeTriosGridModel.class.getClassLoader()
            .getResourceAsStream(directory + "/" + filename);

    if (inputStream == null) {
      String filePath = "." + File.separator + directory + File.separator + filename;
      inputStream = new FileInputStream(filePath);
    }

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] breakUpLine = line.split("\\s+");
        if (breakUpLine.length == 5) {
          try {
            String name = breakUpLine[0];
            if (cardNames.contains(name)) {
              throw new IllegalArgumentException("Duplicate card name found: " + name);
            }
            cardNames.add(name);

            AttackValues north = setAttack(breakUpLine[1]);
            AttackValues south = setAttack(breakUpLine[2]);
            AttackValues east = setAttack(breakUpLine[3]);
            AttackValues west = setAttack(breakUpLine[4]);

            Card currentCard = new Card(name, north, south, east, west);
            numberOfCards++;
            deck.add(currentCard);
          } catch (IllegalArgumentException ex) {
            System.err.println("Invalid card line: " + line);
            throw new IllegalArgumentException("Invalid file format", ex);
          }
        } else {
          throw new IllegalArgumentException("Invalid card line format: " + line);
        }
      }
    }
  }

  /**
   * This helper method helps to set the attack.
   *
   * @param value is the specific attack.
   * @return returns the attack value.
   */
  private AttackValues setAttack(String value) {
    if (value == null) {
      throw new IllegalArgumentException("string attack value cannot be null");
    }
    value = value.toUpperCase();
    switch (value) {
      case "A":
        return AttackValues.A;
      case "1":
        return AttackValues.ONE;
      case "2":
        return AttackValues.TWO;
      case "3":
        return AttackValues.THREE;
      case "4":
        return AttackValues.FOUR;
      case "5":
        return AttackValues.FIVE;
      case "6":
        return AttackValues.SIX;
      case "7":
        return AttackValues.SEVEN;
      case "8":
        return AttackValues.EIGHT;
      case "9":
        return AttackValues.NINE;
      default:
        throw new IllegalArgumentException("Invalid value: " + value);
    }
  }

  /**
   * This helper method ensures that there is enough cards in the file.
   * Must be number of card cells + 1.
   *
   * @param numberOfCards     is the number of cards found in file.
   * @param numberOfCardCells is the number of card cells in the file
   */
  private void ensureEnoughCards(int numberOfCards, int numberOfCardCells) {
    if (numberOfCards < numberOfCardCells + 1) {
      throw new IllegalArgumentException("Must be numberOfCard Cells at least: "
              + numberOfCardCells + " + 1 to start game");
    }
  }

  @Override
  public void createGrid(List<String> gridLines, List<Card> cards) {
    // Might not need this because when reading in the grid file we already make one
  }

  @Override
  public void playCardToCardCell(Card card, int row, int col) {
    if (!isGameStarted) {
      throw new IllegalStateException("Game has not started");
    }
    if (row < 0 || row >= numRows || col < 0 || col >= numCols) {
      throw new IllegalArgumentException("Invalid cell coordinates");
    }
    Cell cell = cellGrid.get(row).get(col);
    if (!cell.canPlaceCard()) {
      throw new IllegalArgumentException("Cannot place a card on this cell");
    }
    List<Card> playerHand = getPlayerHand(currentPlayer);
    int cardIndex = playerHand.indexOf(card);
    if (cardIndex == -1) {
      throw new IllegalArgumentException("Card not in current player's hand");
    }
    cell.placeCard(card, currentPlayer);
    playerHand.remove(cardIndex);
    Position position = new Position(row, col);
    Set<Position> visited = new HashSet<>();
    battlePhase(position, visited);
    currentPlayer = (currentPlayer == Player.RED) ? Player.BLUE : Player.RED;
    for (GameStatusListener listener : modelListeners) {
      listener.notifyTurnChange(currentPlayer);
    }
    if (checkGameOver()) {
      gameOver = true;
      determineWinner();
    }
  }

  private void determineWinner() {
    int redCards = 0;
    int blueCards = 0;
    for (int row = 0; row < numRows; row++) {
      for (int col = 0; col < numCols; col++) {
        Cell cell = cellGrid.get(row).get(col);
        if (cell instanceof CardCell && cell.isOccupied()) {
          Player owner = cell.getOwner();
          if (owner == Player.RED) {
            redCards++;
          } else if (owner == Player.BLUE) {
            blueCards++;
          }
        }
      }
    }
    redCards += redPlayerHand.size();
    blueCards += bluePlayerHand.size();
    if (redCards > blueCards) {
      gameWinner = Player.RED;
    } else if (blueCards > redCards) {
      gameWinner = Player.BLUE;
    } else {
      gameWinner = Player.NULL_PLAYER;
    }
    isGameWon = true;
    int finalScore = (gameWinner == Player.NULL_PLAYER) ? redCards :
            (gameWinner == Player.RED ? redCards : blueCards);
    for (GameStatusListener listener : modelListeners) {
      listener.notifyGameOver(gameWinner, finalScore);
    }
  }

  /**
   * This checks if the game is over or not. If the game is not
   * started it throws an
   *
   * @return if the game is over (true or false).
   * @throws IllegalArgumentException if the game has not started yet.
   */
  private boolean checkGameOver() {
    if (!isGameStarted) {
      throw new IllegalStateException("Game has not started yet");
    }

    for (int row = 0; row < numRows; row++) {
      for (int col = 0; col < numCols; col++) {
        Cell cell = cellGrid.get(row).get(col);
        if (cell instanceof CardCell && !cell.isOccupied()) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Retrieves the adjacent position (attack direction) based on a given direction.
   * This method calculates the new position by moving one unit in the specified direction
   * from the current position.
   *
   * @param pos the current position, cannot be null.
   * @param dir the current direction, cannot be null.
   * @return the adjacent direction position.
   * @throws IllegalArgumentException if direction and position are invalid.
   */
  public Position getAdjacentPosition(Position pos, Direction dir) {
    int row = pos.getRow();
    int col = pos.getCol();
    switch (dir) {
      case NORTH:
        return new Position(row - 1, col);
      case SOUTH:
        return new Position(row + 1, col);
      case EAST:
        return new Position(row, col + 1);
      case WEST:
        return new Position(row, col - 1);
      default:
        throw new IllegalArgumentException("Invalid direction");
    }
  }

  /**
   * The battle phase of the game state, will comply with the game path, where one side attacks
   * the adjacent side at a given time on the grid.
   *
   * @param position is the specific position the card is in (cardCell).
   * @param visited  is the memoization of the cards in the game state.
   */
  private void battlePhase(Position position, Set<Position> visited) {
    if (visited.contains(position)) {
      return;
    }
    visited.add(position);

    Cell currentCell = cellGrid.get(position.getRow()).get(position.getCol());
    Card currentCard = currentCell.getCard();
    Player currentOwner = currentCell.getOwner();  // Get owner from cell

    for (Direction direction : Direction.values()) {
      Position adjPosition = getAdjacentPosition(position, direction);
      int adjRow = adjPosition.getRow();
      int adjCol = adjPosition.getCol();

      // Check if the adjacent position is within bounds
      if (adjRow >= 0 && adjRow < numRows && adjCol >= 0 && adjCol < numCols) {
        Cell adjCell = cellGrid.get(adjRow).get(adjCol);

        // Proceed only if the adjacent cell is occupied
        if (adjCell.isOccupied()) {
          Card adjCard = adjCell.getCard();
          Player adjOwner = adjCell.getOwner();  // Get owner from cell

          // Check if the adjacent card belongs to the opponent
          if (adjOwner != currentOwner) {
            int currentAttack = currentCard.getAttackValue(direction);
            int adjAttack = adjCard.getAttackValue(direction.opposite());

            // Compare attack values
            if (currentAttack < adjAttack) {
              // Updated to set owner on cell instead of card
              adjCell.setOwner(currentOwner);

              // Continue the battle phase from the newly flipped card
              battlePhase(adjPosition, visited);
            }
          }
        }
      }
    }
  }

  // Do not have to throw any exceptions because we know already ensured there is
  // enough cards.

  /**
   * This deals deck to redPlayerHand and bluePlayerHand until each
   * have a size of (numberOfCardCells + 1) / 2.
   *
   * @param deck is the deck where players can refer to (one being played with).
   */
  private void dealCards(List<Card> deck) {
    Collections.shuffle(deck, random);
    int fillHandSize = (numberOfCardCells + 1) / 2;
    for (int card = 0; card < fillHandSize; card++) {
      redPlayerHand.add(deck.remove(0));
      bluePlayerHand.add(deck.remove(0));
    }
  }

  // Change it to (Board, List<Card>, int rows, int cols)
  @Override
  public void startGame(String directoryCards, String filenameCards,
                        String directoryBoard, String filenameBoard) {
    if (directoryCards == null || filenameCards == null || directoryBoard == null ||
            filenameBoard == null) {
      throw new IllegalArgumentException("startGame args cannot be null");
    }
    try {
      readGridConfig(directoryBoard, filenameBoard);
      initializeCellGrid();
    } catch (Exception e) {
      throw new IllegalArgumentException("Cannot read Grid.config file", e);
    }
    try {
      readCardConfig(directoryCards, filenameCards);
    } catch (Exception e) {
      throw new IllegalArgumentException("Cannot read Card.config file", e);
    }
    dealCards(deck);
    isGameStarted = true;
  }

  /**
   * This must be accessed by multiple files to account in the tests.
   *
   * @return GameStarted.
   */
  @Override
  public boolean isGameStarted() {
    return isGameStarted;
  }

  @Override
  public int getCols() {
    return numCols;
  }

  @Override
  public int getRows() {
    return numRows;
  }

  @Override
  public Cell getCell(int row, int col) {
    if (row < 0 || row >= numRows || col < 0 || col >= numCols) {
      throw new IllegalArgumentException("Invalid cell coordinates");
    }
    CellType cellType = grid.get(row).get(col);
    if (cellType == CellType.CARD_CELL) {
      return cellGrid.get(row).get(col);
    } else {
      return new HoleCell();
    }
  }

  @Override
  public Player getCurrentPlayer() {
    gameNotStarted();
    return currentPlayer;
  }

  @Override
  public List<Card> getPlayerHand(Player player) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }
    if (!isGameStarted) {
      throw new IllegalStateException("Game has not been started");
    }
    if (player == Player.RED) {
      return redPlayerHand;
    } else if (player == Player.BLUE) {
      return bluePlayerHand;
    } else {
      throw new IllegalArgumentException("Invalid player");
    }
  }

  /**
   * This gets the CellType of the specific cell.
   *
   * @param row of the certain cell.
   * @param col of the certain cell.
   * @return the cell type of the cell.
   */
  @Override
  public CellType getCellType(int row, int col) {
    if (row < 0 || row >= numRows || col < 0 || col >= numCols) {
      throw new IllegalArgumentException("Invalid cell coordinates");
    }
    return grid.get(row).get(col);
  }

  /**
   * Gets the name of the card.
   *
   * @param name is the name of the card.
   * @return the card by the name.
   */
  @Override
  public Card getCardByName(String name) {
    for (Card card : deck) {
      if (card.getName().equals(name)) {
        return card;
      }
    }
    throw new IllegalArgumentException("Is not contained int he deck");
  }

  /**
   * Helper that is used in different methods. Will tell if the game is not started.
   */
  private void gameNotStarted() {
    if (!isGameStarted) {
      throw new IllegalStateException("Game has not started, testing helper");
    }
  }

  /**
   * Gets the deck size at a given point of the game.
   *
   * @return the deck size (amount of cards).
   */
  @Override
  public int getDeckSize() {
    return deck.size();
  }

  /**
   * Gets the decks which contain cards.
   *
   * @return a new deck.
   */
  @Override
  public List<Card> getDeck() {
    return new ArrayList<>(deck);
  }

  @Override
  public boolean isGameOver() {
    gameNotStarted();
    return gameOver;
  }

  @Override
  public Player getWinner() {
    gameNotStarted();
    if (!isGameOver()) {
      throw new IllegalStateException("Game is not over");
    }
    determineWinner();
    return gameWinner;
  }

  /**
   * Calculates the score for a player based on the number of cards in their hand and the
   * # of occupied cells owned by the player on the grid. The score is incremented by the
   * # of cards in the player's hand, and for each occupied cell owned by the player, the score
   * is increased by one.
   *
   * @param player the player whose score is being calculated for the score
   * @return the player's score,(sum of cards in hand and the amount of occupied cells)
   * @throws IllegalArgumentException if the player is null
   */
  public int getPlayerScore(Player player) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }

    int score = 0;
    List<Card> hand = getPlayerHand(player);
    score += hand.size();

    for (int row = 0; row < numRows; row++) {
      for (int col = 0; col < numCols; col++) {
        Cell cell = cellGrid.get(row).get(col);
        if (cell instanceof CardCell && cell.isOccupied()
                && cell.getOwner() == player) {
          score++;
        }
      }
    }

    return score;
  }

  /**
   * This checks if a given move is legal based on the game state. Essentially a legal move is when
   * the game has started (row and col is within bounds), and the cell is a card cell(occupied).
   *
   * @param row the row of the cell to check
   * @param col the column of the cell to check
   * @return true if the move is legal, and false otherwise
   * @throws IllegalStateException    if the game has not started
   * @throws IllegalArgumentException if the row or column is out of bounds
   */
  public boolean isLegalMove(int row, int col) {
    if (!isGameStarted) {
      throw new IllegalStateException("Game has not started");
    }
    if (row < 0 || row >= numRows || col < 0 || col >= numCols) {
      return false;
    }
    Cell cell = cellGrid.get(row).get(col);
    return cell.canPlaceCard();
  }

  /**
   * Calculates how many opponent cards would be flipped if the given card was played at the
   * specified position.
   * @param card the card to simulate playing
   * @param row  the row to simulate playing at
   * @param col  the column to simulate playing at
   * @return the number of opponent cards that would be flipped by this move
   * @throws IllegalArgumentException if card is null or coordinates are invalid
   */
  public int getPotentialFlips(Card card, int row, int col) {
    if (card == null) {
      throw new IllegalArgumentException("Card cannot be null");
    }
    if (row < 0 || row >= numRows || col < 0 || col >= numCols) {
      throw new IllegalArgumentException("Invalid cell coordinates");
    }
    if (!isGameStarted) {
      throw new IllegalStateException("Game has not started");
    }

    Cell cell = cellGrid.get(row).get(col);
    if (!cell.canPlaceCard()) {
      throw new IllegalArgumentException("Invalid coordinate.");
    }

    List<List<Cell>> tempGrid = new ArrayList<>();
    for (int currentRow = 0; currentRow < numRows; currentRow++) {
      List<Cell> curRow = new ArrayList<>();
      for (int currentCol = 0; currentCol < numCols; currentCol++) {
        Cell originalCell = cellGrid.get(currentRow).get(currentCol);
        if (originalCell instanceof CardCell) {
          CardCell newCell = new CardCell();
          if (originalCell.isOccupied()) {
            newCell.placeCard(copyCard(originalCell.getCard()), originalCell.getOwner());
          }
          curRow.add(newCell);
        } else {
          curRow.add(new HoleCell());
        }
      }
      tempGrid.add(curRow);
    }

    Cell tempCell = tempGrid.get(row).get(col);
    tempCell.placeCard(copyCard(card), currentPlayer);
    Position position = new Position(row, col);
    Set<Position> flippedCards = new HashSet<>();
    calculatePotentialFlips(position, tempGrid, flippedCards);

    return flippedCards.size();
  }

  /**
   * Gets all legal moves (positions on the grid) that are available
   * to the current player.
   *
   * @return List of Position objects representing valid moves
   * @throws IllegalStateException if game has not started
   */
  public List<Position> getLegalMoves() {
    if (!isGameStarted) {
      throw new IllegalStateException("Game has not started");
    }

    List<Position> legalMoves = new ArrayList<>();

    for (int row = 0; row < numRows; row++) {
      for (int col = 0; col < numCols; col++) {
        Cell cell = cellGrid.get(row).get(col);
        if (cell instanceof CardCell && !cell.isOccupied()) {
          legalMoves.add(new Position(row, col));
        }
      }
    }

    return legalMoves;
  }

  private void calculatePotentialFlips(Position position, List<List<Cell>> tempGrid,
                                       Set<Position> flippedCards) {
    if (!isGameStarted) {
      throw new IllegalStateException("Game has not started");
    }
    if (position == null || tempGrid == null || flippedCards == null) {
      throw new IllegalArgumentException("Args cannot be null");
    }

    for (Direction direction : Direction.values()) {
      Position adjPosition = getAdjacentPosition(position, direction);
      int adjRow = adjPosition.getRow();
      int adjCol = adjPosition.getCol();

      if (adjRow >= 0 && adjRow < numRows && adjCol >= 0 && adjCol < numCols) {
        Cell currentCell = tempGrid.get(position.getRow()).get(position.getCol());
        Cell adjCell = tempGrid.get(adjRow).get(adjCol);

        if (adjCell.isOccupied()) {
          Card currentCard = currentCell.getCard();
          Card adjCard = adjCell.getCard();
          Player currentOwner = currentCell.getOwner();
          Player adjOwner = adjCell.getOwner();

          if (adjOwner != currentOwner) {
            int attackValue = currentCard.getAttackValue(direction);
            int defenseValue = adjCard.getAttackValue(direction.opposite());

            if (attackValue > defenseValue) {
              Position adjPos = new Position(adjRow, adjCol);
              if (!flippedCards.contains(adjPos)) {
                flippedCards.add(adjPos);
                adjCell.setOwner(currentOwner);
                calculatePotentialFlips(adjPos, tempGrid, flippedCards);
              }
            }
          }
        }
      }
    }
  }

  /**
   * Creates a complete deep copy of the current game state.
   *
   * @return A new ReadOnlyTTModel containing a copy of the current game state
   */
  @Override
  public ReadOnlyTTModel getCopy() {
    ThreeTriosGridModel copy = new ThreeTriosGridModel(this.random);
    copy.numRows = this.numRows;
    copy.numCols = this.numCols;
    copy.currentPlayer = this.currentPlayer;
    copy.gameWinner = this.gameWinner;
    copy.isGameStarted = this.isGameStarted;
    copy.isGameWon = this.isGameWon;
    copy.gameOver = this.gameOver;
    copy.numberOfCardCells = this.numberOfCardCells;
    copy.numberOfCards = this.numberOfCards;
    copy.deck = new ArrayList<>();

    for (Card card : this.deck) {
      copy.deck.add(copyCard(card));
    }

    copy.redPlayerHand = new ArrayList<>();
    for (Card card : this.redPlayerHand) {
      copy.redPlayerHand.add(copyCard(card));
    }

    copy.bluePlayerHand = new ArrayList<>();
    for (Card card : this.bluePlayerHand) {
      copy.bluePlayerHand.add(copyCard(card));
    }

    copy.grid = new ArrayList<>();
    for (List<CellType> row : this.grid) {
      copy.grid.add(new ArrayList<>(row));
    }

    copy.cellGrid = new ArrayList<>();
    for (int row = 0; row < numRows; row++) {
      List<Cell> newRow = new ArrayList<>();
      for (int col = 0; col < numCols; col++) {
        Cell originalCell = this.cellGrid.get(row).get(col);
        if (originalCell instanceof CardCell) {
          CardCell newCell = new CardCell();
          if (originalCell.isOccupied()) {
            Card newCard = copyCard(originalCell.getCard());
            newCell.placeCard(newCard, originalCell.getOwner());
          }
          newRow.add(newCell);
        } else {
          newRow.add(new HoleCell());
        }
      }
      copy.cellGrid.add(newRow);
    }

    return copy;
  }

  /**
   * Helper method to create a deep copy of a card.
   * @param original the card to copy
   * @return a new Card with the same properties as the original
   */
  private Card copyCard(Card original) {
    if (original == null) {
      return null;
    }

    return new Card(
            original.getName(),
            original.getNorthAttack(),
            original.getSouthAttack(),
            original.getEastAttack(),
            original.getWestAttack()
    );
  }
}