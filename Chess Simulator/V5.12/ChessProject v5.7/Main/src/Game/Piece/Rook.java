package Game.Piece;

import Game.GameEngine.ChessEngine;
import resources.Variables;

import java.awt.image.BufferedImage;

public class Rook extends Piece implements movePieceCollision {
    public Rook(ChessEngine engine, int col, int row, boolean isWhite) {
        super(engine);
        this.col = col;
        this.row = row;
        this.xPos = col * Variables.tileSize;
        this.yPos = row * Variables.tileSize;

        this.isWhite = isWhite;
        this.name = "Rook";
        this.pieceChar = "R";

        this.sprite = sheet.getSubimage(4 * sheetScale, (isWhite ? 0 : sheetScale), sheetScale, sheetScale)
                           .getScaledInstance(Variables.tileSize, Variables.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    @Override
    public boolean pieceMoves(int col, int row) {
        return this.col == col || this.row == row;  //moving horizontally or vertically!
    }

    @Override
    public boolean moveCollidesWithPiece(int col, int row) {
        //left
        if(this.col > col)
            for(int c = this.col - 1; c > col; c--)
                if(engine.getPiece(c, this.row) != null)
                    return true;        //collides!

        //right
        if (this.col < col)
            for (int c = this.col + 1; c < col; c++)
                if(engine.getPiece(c, this.row) != null)
                    return true;

        //up
        if(this.row > row)
            for(int r = this.row - 1; r > row; r--)
                if (engine.getPiece(this.col, r) != null)
                    return true;

        //down
        if(this.row < row)
            for (int r = this.row + 1; r < row; r++)
                if ( engine.getPiece(this.col, r) != null)
                    return true;

        return false;
    }
}
