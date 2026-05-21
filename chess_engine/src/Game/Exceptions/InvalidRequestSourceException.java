package Game.Exceptions;

/**
 * Thrown when a move request comes from an unauthorized source.
 * Only approved callers (GUI.Input, registered Users) can request moves.
 */
public class InvalidRequestSourceException extends RuntimeException {
    public InvalidRequestSourceException(String message) {
        super(message);
    }
}
