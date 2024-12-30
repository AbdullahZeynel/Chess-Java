package Game.Piece;

import Game.GameEngine.ChessEngine;
import resources.Variables;

import java.awt.image.BufferedImage;

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

    protected boolean pawnOnePush(int col, int row){
        boolean isSameColumn = (this.col == col);
        boolean isValidRow = (row == this.row - this.colorIndex);
        boolean isTileEmpty = (engine.getPiece(col, row) == null);
        return isSameColumn && isValidRow && isTileEmpty;
    }

    protected boolean pawnTwoPush(int col, int row){
        boolean isSameColumn = (this.col == col);
        boolean isValidRow = (row == this.row - this.colorIndex * 2);
        boolean isTileEmpty = (engine.getPiece(col, row) == null);
        boolean isTileInTheWayEmpty = (engine.getPiece(col, row + this.colorIndex) == null);
        boolean canPawnTwoPush = (this.row == (isWhite ? 6 : 1));
        return isSameColumn && isValidRow && isTileEmpty && isTileInTheWayEmpty && canPawnTwoPush;
    }

    protected boolean pawnCapture(int col, int row){
        boolean leftCapture      = (this.col - 1 == col);
        boolean rightCapture     = (this.col + 1== col);
        boolean isValidRow       = (row == this.row - this.colorIndex);
        boolean isTileEmpty      = (engine.getPiece(col, row) != null);
        return (leftCapture || rightCapture) && isValidRow && isTileEmpty;
    }

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
        //int colorIndex = isWhite ? 1 : -1;
        //push pawn 1
        if(pawnOnePush(col, row))
            return true;
        //push pawn two
        if (pawnTwoPush(col, row))
            return true;
        //capture to the left or right
        if (pawnCapture(col, row))
            return true;
        //en passant left and right
        if(pawnEnPassant(col, row))
            return true;
        return false;
    }
}