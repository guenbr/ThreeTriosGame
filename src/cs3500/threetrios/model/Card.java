package cs3500.threetrios.model;

/**
 * This class is for the cards with the different directions and attack values aligned with those
 * directions. There is also an owner of the card, fielded by the Player class.
 */
public class Card {

  private final String name;

  private final AttackValues north;

  private final AttackValues south;

  private final AttackValues east;

  private final AttackValues west;

  /**
   * Constructor to ensure all the fields, and to make sure none of the values are null.
   * @param name name of the card.
   * @param north north attack (direction).
   * @param south south attack.
   * @param east east attack.
   * @param west west attack.
   */
  public Card(String name, AttackValues north, AttackValues south, AttackValues east,
              AttackValues west) {
    if (name == null || north == null || south == null ||
            east == null || west == null) {
      throw new IllegalArgumentException("Card arguments cannot be null");
    }
    this.name = name;
    this.north = north;
    this.south = south;
    this.east = east;
    this.west = west;
  }

  /**
   * To get the name of the card when called.
   * @return the name of the card.
   */
  public String getName() {
    return name;
  }

  /**
   * Get the attack direction when called.
   * @return will return the north direction.
   */
  public AttackValues getNorthAttack() {
    return north;
  }

  /**
   * Get the attack direction when called.
   * @return will return the south direction.
   */
  public AttackValues getSouthAttack() {
    return south;
  }

  /**
   * Get the attack direction when called.
   * @return will return the east direction.
   */
  public AttackValues getEastAttack() {
    return east;
  }

  /**
   * Get the attack direction when called.
   * @return will return the west direction.
   */
  public AttackValues getWestAttack() {
    return west;
  }

  /**
   * Get the attack direction when called(will get value of that direction).
   */
  public int getAttackValue(Direction direction) {
    switch (direction) {
      case NORTH:
        return north.getValue();
      case SOUTH:
        return south.getValue();
      case EAST:
        return east.getValue();
      case WEST:
        return west.getValue();
      default:
        throw new IllegalArgumentException("Invalid direction");
    }
  }

  @Override
  public String toString() {
    return name + " (" + north.getValue() + "," + south.getValue() + "," +
            east.getValue() + "," + west.getValue() + ")";
  }
}