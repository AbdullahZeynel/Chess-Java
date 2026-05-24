package Game.Piece;

import Game.GameEngine.ChessEngine;
import resources.Variables;



/**
 * Bishop piece — moves diagonally any number of squares.
 * Implements collision detection for path blocking.
 *
 * The diagonal movement rule: the absolute difference between
 * column and row offsets must be equal (|dx| == |dy|).
 */
public class Bishop extends Piece implements movePieceCollision {
    public Bishop(ChessEngine engine, int col, int row, boolean isWhite) {
        super(engine);
        this.col = col;
        this.row = row;
        this.xPos = col * Variables.tileSize;
        this.yPos = row * Variables.tileSize;

        this.isWhite = isWhite;
        this.name = "Bishop";
        this.pieceChar = "B";

        this.sprite = getCachedSprite(2, isWhite);
    }

    @Override
    public boolean pieceMoves(int col, int row) {
        return Math.abs(this.col - col) == Math.abs(this.row - row);
    }

    /**
     * Checks for pieces blocking the diagonal path between the bishop's
     * current position and the target tile.
     *
     * Note: only checks tiles BETWEEN start and target — not the target itself
     * (capture validation is handled elsewhere).
     *
     * Example: moving from (3,3) to (1,1) checks (2,2) only.
     */
    @Override
    public boolean moveCollidesWithPiece(int col, int row) {
        // Up-left diagonal
        if (this.col > col && this.row > row)
            for (int i = 1; i < Math.abs(this.col - col); i++)
                if (engine.getPiece(this.col - i, this.row - i) != null)
                    return true;
        // Up-right diagonal
        if (this.col < col && this.row > row)
            for (int i = 1; i < Math.abs(this.col - col); i++)
                if (engine.getPiece(this.col + i, this.row - i) != null)
                    return true;
        // Down-left diagonal
        if (this.col > col && this.row < row)
            for (int i = 1; i < Math.abs(this.col - col); i++)
                if (engine.getPiece(this.col - i, this.row + i) != null)
                    return true;
        // Down-right diagonal
        if (this.col < col && this.row < row)
            for (int i = 1; i < Math.abs(this.col - col); i++)
                if (engine.getPiece(this.col + i, this.row + i) != null)
                    return true;
        return false;
    }
}
