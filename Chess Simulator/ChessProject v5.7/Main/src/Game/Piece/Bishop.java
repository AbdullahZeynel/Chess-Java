package Game.Piece;

import Game.GameEngine.ChessEngine;
import resources.Variables;

import java.awt.image.BufferedImage;

public class Bishop extends Piece implements movePieceCollision {
    public Bishop(ChessEngine engine, int col, int row, boolean isWhite) {
        super(engine);
        this.col = col;
        this.row = row;
        this.xPos = col * Variables.tileSize;   //placement coordinates in pixel I guess?
        this.yPos = row * Variables.tileSize;   // No maybe starting coordinates location on the baord

        this.isWhite = isWhite;
        this.name = "Bishop";
        this.pieceChar = "B";

        //2 is the order of bishop in the pieces.png, isWhite info provides the row info so that it is white or black
        //sheetScales here are for the widh and length of the image we are taking a square
        //so basiclly it is like this: (starting xpos, starting ypos, width, high)
        this.sprite = sheet.getSubimage(2 * sheetScale, (isWhite ? 0 : sheetScale), sheetScale, sheetScale)
                .getScaledInstance(Variables.tileSize, Variables.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    @Override
    public boolean pieceMoves(int col, int row) {   //Override the Interface
        return Math.abs(this.col - col) == Math.abs(this.row - row);
        //the diognal tiles according to the bishop's current position's x and y must be equal
        //this is how we find the moves for the bishop.
    }


    @Override
    public boolean moveCollidesWithPiece(int col, int row) {
        ///true: collision detected, false: no collision
        ///The loop does not check the target position itself
        ///because it only checks tiles before reaching the target.

        //up-left
        if (this.col > col && this.row > row)                                                       // if the tile is on the left and on the up to the bishop
            for (int i = 1; i < Math.abs(this.col - col); i++)                                                //can be this.row - row either nothing changes | calculate the left possible moves in the diognal
                if (engine.getPiece(this.col - i, this.row - i) != null)                         //checks wether there is a piece in the way starting from the bishop to the up left here
                    return true;
        //up-right
        if (this.col < col && this.row > row)
            for (int i = 1; i < Math.abs(this.col - col); i++)                                          //can be this.row - row either nothing changes
                if (engine.getPiece(this.col + i, this.row - i) != null)
                    return true;
        //down-left
        if (this.col > col && this.row < row)
            for (int i = 1; i < Math.abs(this.col - col); i++)                                                          //can be this.row - row either nothing changes
                if (engine.getPiece(this.col - i, this.row + i) != null)
                    return true;
        //down-right
        if (this.col < col && this.row < row)
            for (int i = 1; i < Math.abs(this.col - col); i++)                                                   //can be this.row - row either nothing changes
                if (engine.getPiece(this.col + i, this.row + i) != null)
                    return true;
        return false;

        /*
        NOTE: we send a desired move or a move and in this move it includes the col and row info
        the sent col and row are the desired location's so we are checking the squares in between but not the desired location yet!
        Example: For a move from (3,3) to (1,1), it will check (2,2) and stop before reaching (1,1).
        If all tiles along the path are empty (no pieces), the loop completes without returning true. In this case, the function returns false.
         */
    }
}
