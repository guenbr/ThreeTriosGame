package cs3500.threetrios.model;

/**
 * Enum for the attack values 1-A(10) on the cards.
 * This makes sure that the only valid numbers are what's in this enum or else game
 * cannot be played.
 */
public enum AttackValues {
  ONE(1),
  TWO(2),
  THREE(3),
  FOUR(4),
  FIVE(5),
  SIX(6),
  SEVEN(7),
  EIGHT(8),
  NINE(9),
  A(10);

  private final int value;

  AttackValues(int value) {
    this.value = value;
  }

  /**
   * Gets the value of the attackValue (1-10). 10 being parsed as A.
   * @return the value.
   */
  public int getValue() {
    return value;
  }

}
