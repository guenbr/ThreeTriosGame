package cs3500.threetrios.model;

/**
 * This class inherits from cell and overrides for the certain state.
 */
public class HoleCell implements Cell {
  @Override
  public boolean canPlaceCard() {
    return false; // Can't place a card here
  }

  @Override
  public boolean isOccupied() {
    return false; // Hole cells are never occupied
  }

  @Override
  public void placeCard(Card card, Player owner) {
    throw new UnsupportedOperationException("Cannot place a card on a hole");
  }

  @Override
  public Card getCard() {
    return null; // No card in a hole cell
  }

  @Override
  public Player getOwner() {
    return Player.NULL_PLAYER;
  }

  @Override
  public void setOwner(Player owner) {
    throw new UnsupportedOperationException("Cannot set owner of a hole");
  }
}

