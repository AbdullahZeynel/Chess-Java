package Game.GameEngine;

import Game.Piece.King;
import Game.Piece.Piece;

/**
 * Represents a single chess move from one tile to another.
 * Captures the old and new positions, the moving piece,
 * any captured piece, and check status.
 */
public class Move {

    int oldCol;
    int oldRow;
    int newCol;
    int newRow;

    Piece piece;
    Piece capturedPiece;

    boolean isKingChecked;

    public Move(ChessEngine engine, Piece piece, int newCol, int newRow){
        this.oldCol= piece.col;
        this.oldRow= piece.row;
        this.newCol= newCol;
        this.newRow= newRow;

        this.piece = piece;

        /// Checks if there's already a piece at the target position (potential capture)
        this.capturedPiece = engine.getPiece(newCol, newRow);

        if(piece instanceof King){
            this.isKingChecked = false;
        }
    }

}
