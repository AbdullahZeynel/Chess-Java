package Game.Exceptions;

/**
 * Thrown when a FEN string contains an invalid move notation.
 */
public class FenMoveErrorException extends RuntimeException {
    public FenMoveErrorException(String message) {
      super(message);
    }
}
