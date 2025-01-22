package cs3500.threetrios.model;

/**
 * This is a enum that ensures that there is no other players.
 * It keeps the integrity of blue and red player.
 */
public enum Player {
  BLUE("BLUE"),
  RED("RED"),
  NULL_PLAYER("NiL");


  private final String color;

  /**
   * Color of the player is either the blue red, or the null(where there is no player).
   * @param color of the player on the card.
   */
  Player(String color) {
    this.color = color;
  }

  /**
   * Gets the player per request of card identity.
   * @return the color of that player.
   */
  public String getPlayer() {
    return color;
  }

}