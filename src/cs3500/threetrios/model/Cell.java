package cs3500.threetrios.model;

/**
 * Interface for representing a cell on the game grid. This interface defines methods
 * to manage card placement, ownership, and cell status in the Three Trios game.
 * Each cell can either be empty, contain a card with an owner, or be a hole that
 * cannot accept cards.
 */
public interface Cell {

  /**
   * Determines whether a card can be placed in this cell.
   * @return true if the cell is empty and is of type CardCell, false otherwise.
   */
  boolean canPlaceCard();

  /**
   * Determines whether this cell currently contains a card.
   * @return true if there is a card in this cell, false otherwise.
   */
  boolean isOccupied();

  /**
   * Places a card in this cell with the specified owner.
   * @param card the card to place in this cell
   * @param owner the owner of the card being placed
   * @throws IllegalStateException if the cell is already occupied
   * @throws IllegalArgumentException if the card or owner is null
   */
  void placeCard(Card card, Player owner);

  /**
   * Retrieves the card currently placed in this cell.
   * @return the card in this cell, or null if the cell is empty.
   */
  Card getCard();

  /**
   * Retrieves the current owner of the card in this cell.
   * @return the owner of the card, or null if the cell is empty.
   */
  Player getOwner();

  /**
   * Updates the owner of the card in this cell.
   * @param owner the new owner to set for the card
   * @throws IllegalStateException if the cell is empty
   * @throws IllegalArgumentException if the owner is null
   */
  void setOwner(Player owner);
}