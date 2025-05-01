package inf112.ppbros.model;

/**
 * Represents a 2D coordinate with integer {@code x} and {@code y} components.
 * 
 * <p>This record is used to represent grid-based or pixel-based positions in the game world.</p>
 *
 * @param x the X-coordinate
 * @param y the Y-coordinate
 */
public record Coordinate(int x, int y) {
  
}
