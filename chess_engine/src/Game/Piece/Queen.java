package Game.Piece;

import Game.GameEngine.ChessEngine;
import resources.Variables;

import java.awt.image.BufferedImage;

/**
 * Queen piece — combines the movement of both Rook and Bishop.
 * Can move horizontally, vertically, or diagonally any number of squares.
 * Implements collision detection for both straight-line and diagonal paths.
 */
public class Queen extends Piece implements movePieceCollision {
    public Queen(ChessEngine engine, int col, int row, boolean isWhite){
        super(engine);
        this.col = col;
        this.row = row;
        this.xPos = col * Variables.tileSize;
        this.yPos = row * Variables.tileSize;

        this.isWhite = isWhite;
        this.name = "Queen";
        this.pieceChar = "Q";

        this.sprite = sheet.getSubimage(1 * sheetScale, (isWhite ? 0 : sheetScale), sheetScale, sheetScale)
                           .getScaledInstance(Variables.tileSize, Variables.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    @Override
    public boolean pieceMoves(int col, int row) {
        boolean diagonalMoves = (Math.abs(col - this.col) == Math.abs(row - this.row));
        boolean straightMoves = ((col == this.col) || (row == this.row));

        return diagonalMoves || straightMoves;
    }

    @Override
    public boolean moveCollidesWithPiece(int col, int row) {
        // Straight-line movement (like a Rook)
        if(this.col == col || this.row == row){
            // Left
            if (this.col > col)
                for (int c = this.col - 1; c > col; c--)
                    if(engine.getPiece(c, this.row) != null)
                        return true;
            // Right
            if (this.col < col)
                for (int c = this.col + 1; c < col; c++)
                    if(engine.getPiece(c, this.row) != null)
                        return true;
            // Up
            if (this.row > row)
                for (int r = this.row - 1; r > row; r--)
                    if(engine.getPiece(this.col, r) != null)
                        return true;
            // Down
            if (this.row < row)
                for (int r = this.row + 1; r < row; r++)
                    if(engine.getPiece(this.col, r) != null)
                        return true;
        } else {
            // Diagonal movement (like a Bishop)
            // Up-left
            if (this.col > col && this.row > row)
                for (int i = 1; i < Math.abs(this.col - col); i++)
                    if(engine.getPiece(this.col - i, this.row - i) != null)
                        return true;
            // Up-right
            if (this.col < col && this.row > row)
                for (int i = 1; i < Math.abs(this.col - col); i++)
                    if(engine.getPiece(this.col + i, this.row - i) != null)
                        return true;
            // Down-left
            if (this.col > col && this.row < row)
                for (int i = 1; i < Math.abs(this.col - col); i++)
                    if(engine.getPiece(this.col - i, this.row + i) != null)
                        return true;
            // Down-right
            if (this.col < col && this.row < row)
                for (int i = 1; i < Math.abs(this.col - col); i++)
                    if(engine.getPiece(this.col + i, this.row + i) != null)
                        return true;
        }
        return false;
    }
}
