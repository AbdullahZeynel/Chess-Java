package Game.Exceptions;

/**
 * Thrown when a player attempts to make a move when it's not their turn.
 */
public class InvalidTurnException extends RuntimeException {
    public InvalidTurnException(String message) {
        super(message);
    }
}
