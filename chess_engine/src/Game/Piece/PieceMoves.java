package Game.Piece;

/**
 * Interface for defining piece-specific movement rules.
 * Each piece type must implement this to specify which tiles it can move to.
 */
public interface PieceMoves {
    boolean pieceMoves(int col, int row);
}
