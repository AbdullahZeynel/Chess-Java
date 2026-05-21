package Game.Piece;


import Game.GameEngine.ChessEngine;
import Game.GameEngine.Move;

import resources.Variables;

import java.awt.image.BufferedImage;

/**
 * King piece — moves one square in any direction.
 * Also handles castling (both king-side and queen-side).
 */
public class King extends Piece {

    public King (ChessEngine engine, int col, int row, boolean isWhite){
        super(engine);
        this.col = col;
        this.row = row;
        this.xPos = col * Variables.tileSize;
        this.yPos = row * Variables.tileSize;

        this.isWhite = isWhite;
        this.name = "King";
        this.pieceChar = "K";

        this.sprite = sheet.getSubimage(0 * sheetScale, (isWhite ? 0 : sheetScale), sheetScale, sheetScale)
                           .getScaledInstance(Variables.tileSize, Variables.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    @Override
    public boolean pieceMoves(int col, int row) {
        return (Math.abs(col - this.col) <= 1 && Math.abs(row - this.row) <= 1) ||
                canCastle(col, row);    /// King moves one square in any direction, or castles
    }

    /**
     * Validates castling conditions:
     * - King and rook must not have moved (isFirstMove)
     * - No pieces between king and rook
     * - King must not pass through check
     *
     * King-side castle: king to col 6, rook from col 7 to col 5
     * Queen-side castle: king to col 2, rook from col 0 to col 3
     */
    protected boolean canCastle(int col, int row) {
        int pieceRow = isWhite ? 7 : 0;

        if (pieceRow == row) {
            if (col == 6) {     /// King-side castle
                Piece rook = engine.getPiece(7, row);
                if (rook != null && rook.name.equals("Rook") && rook.isWhite == isWhite)
                    if (rook.isFirstMove == isFirstMove) {
                        return  engine.getPiece(5, row) == null &&
                                engine.getPiece(6, row) == null &&
                               !engine.checkScanner.isKingChecked(new Move(engine, this, 5, row));
                }
            } else if (col == 2) {  /// Queen-side castle
                Piece rook = engine.getPiece(0, row);
                if ( rook != null && rook.name.equals("Rook") && rook.isWhite == isWhite)
                    if (rook.isFirstMove == isFirstMove) {
                        return engine.getPiece(3, row) == null &&
                               engine.getPiece(2, row) == null &&
                               engine.getPiece(1, row) == null &&
                              !engine.checkScanner.isKingChecked(new Move(engine, this, 3, row));
                    }
            }
        }
        return false;
    }
}
