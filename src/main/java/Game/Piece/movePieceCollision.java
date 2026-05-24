package Game.Piece;

/**
 * Interface for sliding pieces (Bishop, Rook, Queen) that need
 * to check if their movement path is blocked by other pieces.
 */
public interface movePieceCollision {
    boolean moveCollidesWithPiece(int col, int row);
}
