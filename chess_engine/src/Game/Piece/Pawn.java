package Game.Piece;

import Game.GameEngine.ChessEngine;
import resources.Variables;

import java.awt.image.BufferedImage;

/**
 * Pawn piece — moves forward one square, optionally two on first move.
 * Captures diagonally. Supports en passant and promotion.
 */
public class Pawn extends Piece {
    int colorIndex;

    public Pawn(ChessEngine engine, int col, int row, boolean isWhite) {
        super(engine);
        this.col     = col;
        this.row     = row;
        this.xPos    = col * Variables.tileSize;
        this.yPos    = row * Variables.tileSize;

        this.isWhite = isWhite;
        this.name    = "Pawn";
        this.pieceChar = "P";

        this.sprite  = sheet.getSubimage(5 * sheetScale, (isWhite ? 0 : sheetScale), sheetScale, sheetScale)
                .getScaledInstance(Variables.tileSize, Variables.tileSize, BufferedImage.SCALE_SMOOTH);

        this.colorIndex = isWhite ? 1 : -1;
    }

    /// Single push — move one square forward if the target is empty
    protected boolean pawnOnePush(int col, int row){
        boolean isSameColumn = (this.col == col);
        boolean isValidRow = (row == this.row - this.colorIndex);
        boolean isTileEmpty = (engine.getPiece(col, row) == null);
        return isSameColumn && isValidRow && isTileEmpty;
    }

    /// Double push — move two squares forward from the starting rank
    protected boolean pawnTwoPush(int col, int row){
        boolean isSameColumn = (this.col == col);
        boolean isValidRow = (row == this.row - this.colorIndex * 2);
        boolean isTileEmpty = (engine.getPiece(col, row) == null);
        boolean isTileInTheWayEmpty = (engine.getPiece(col, row + this.colorIndex) == null);
        boolean canPawnTwoPush = (this.row == (isWhite ? 6 : 1));
        return isSameColumn && isValidRow && isTileEmpty && isTileInTheWayEmpty && canPawnTwoPush;
    }

    /// Diagonal capture — move one square diagonally to capture an enemy piece
    protected boolean pawnCapture(int col, int row){
        boolean leftCapture      = (this.col - 1 == col);
        boolean rightCapture     = (this.col + 1 == col);
        boolean isValidRow       = (row == this.row - this.colorIndex);
        boolean isTileOccupied   = (engine.getPiece(col, row) != null);
        return (leftCapture || rightCapture) && isValidRow && isTileOccupied;
    }

    /// En passant — special diagonal capture of a pawn that just double-pushed
    protected boolean pawnEnPassant(int col, int row){
        boolean leftEnPassant   = (this.col - 1 == col);
        boolean rightEnPassant  = (this.col + 1 == col);
        boolean isEnPassantTile = (engine.getTileNum(col, row) == engine.enPassantTile);
        boolean isValidRow      = (row == this.row - this.colorIndex);
        boolean isTileEmpty     = (engine.getPiece(col, row) == null);

        return (leftEnPassant || rightEnPassant) && isEnPassantTile && isValidRow && isTileEmpty;
    }

    @Override
    public boolean pieceMoves(int col, int row) {
        if(pawnOnePush(col, row))
            return true;
        if (pawnTwoPush(col, row))
            return true;
        if (pawnCapture(col, row))
            return true;
        if(pawnEnPassant(col, row))
            return true;
        return false;
    }
}