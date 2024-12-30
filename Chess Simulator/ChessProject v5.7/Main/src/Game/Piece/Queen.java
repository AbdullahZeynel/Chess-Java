package Game.Piece;

import Game.GameEngine.ChessEngine;
import resources.Variables;

import java.awt.image.BufferedImage;

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
        boolean diognalMoves = (Math.abs(col - this.col) == Math.abs(row - this.row));
        boolean horizontalMoves = ((col == this.col) || (row == this.row));

        return diognalMoves || horizontalMoves;
    }

    @Override
    public boolean moveCollidesWithPiece(int col, int row) {
        //like a rook
        if(this.col == col || this.row == row){                                                                         //
            //left                                                                          //
            if (this.col > col)                                                                         //
                for (int c = this.col - 1; c > col; c--)                                                                            //
                    if(engine.getPiece(c, this.row) != null)                                                                            //
                        return true;                                                                            //
            //right                                                                         //
            if (this.col < col)                                                                         //
                for (int c = this.col + 1; c < col; c++)                                                                            //
                    if(engine.getPiece(c, this.row) != null)                                                                            //
                        return true;                                                                            //
            //up                                                                            //
            if (this.row > row)                                                                         //
                for (int r = this.row - 1; r > row; r--)                                                                            //
                    if(engine.getPiece(this.col, r) != null)                                                                            //
                        return true;                                                                            //
            //down                                                                          //
            if (this.row < row)                                                                         //
                for (int r = this.row + 1; r < row; r++)                                                                            //
                    if(engine.getPiece(this.col, r) != null)                                                                            //
                        return true;                                                                            //
        } else {
            //like a bishop
            //up-left
            if (this.col > col && this.row > row)
                for (int i = 1; i < Math.abs(this.col - col); i++)                         //               //can be this.row - row either nothing changes
                    if(engine.getPiece(this.col - i, this.row - i) != null)
                        return true;
            //up-right
            if (this.col < col && this.row > row)
                for (int i = 1; i < Math.abs(this.col - col); i++)                         //                //can be this.row - row either nothing changes
                    if(engine.getPiece(this.col + i, this.row - i) != null)
                        return true;
            //down-left
            if (this.col > col && this.row < row)
                for (int i = 1; i < Math.abs(this.col - col); i++)                         //               //can be this.row - row either nothing changes
                    if(engine.getPiece(this.col - i, this.row + i) != null)
                        return true;
            //down-right
            if (this.col < col && this.row < row)
                for (int i = 1; i < Math.abs(this.col - col); i++)                         //              //can be this.row - row either nothing changes
                    if(engine.getPiece(this.col + i, this.row + i) != null)
                        return true;
        }
        return false;
    }
}
