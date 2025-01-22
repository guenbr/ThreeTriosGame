package cs3500.threetrios.model;

/**
 * This class inherits from the Cell interface and overrides the
 * methods based on the CardCell.
 */
public class CardCell implements Cell {
  private Card card;
  private Player owner;

  public CardCell() {
    this.card = null;
    this.owner = Player.NULL_PLAYER;
  }

  @Override
  public boolean canPlaceCard() {
    return card == null;
  }

  @Override
  public boolean isOccupied() {
    return card != null;
  }

  @Override
  public void placeCard(Card card, Player owner) {
    if (this.card != null) {
      throw new IllegalStateException("Cell is already occupied");
    }
    if (card == null) {
      throw new IllegalArgumentException("Cannot place null card");
    }
    if (owner == null) {
      throw new IllegalArgumentException("Cannot place card with null owner");
    }
    this.card = card;
    this.owner = owner;
  }

  @Override
  public Card getCard() {
    return card;
  }

  @Override
  public Player getOwner() {
    return owner;
  }

  @Override
  public void setOwner(Player newOwner) {
    if (card == null) {
      throw new IllegalStateException("Cannot set owner of empty cell");
    }
    if (newOwner == null) {
      throw new IllegalArgumentException("New owner cannot be null");
    }
    this.owner = newOwner;
  }
}
