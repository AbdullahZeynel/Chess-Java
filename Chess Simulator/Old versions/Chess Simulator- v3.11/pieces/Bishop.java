package pieces;

import main.Board;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Bishop extends Piece{
    public Bishop(Board board, int col, int row, boolean isWhite){
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * board.tile_size;
        this.yPos = row * board.tile_size;

        this.isWhite = isWhite;
        this.name = "Bishop";

        this.sprite = sheet.getSubimage(2 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(board.tile_size, board.tile_size, BufferedImage.SCALE_SMOOTH);
    }

    @Override
    public boolean isValidMovement(int col, int row) {
        return Math.abs(this.col - col) == Math.abs(this.row - row);
    }

    @Override
    public boolean moveCollidesWithPiece(int col, int row) {
        //true: collision detected, false: no collision
        //The loop does not check the target position itself because it only checks tiles before reaching the target.

        //up-left
        if (this.col > col && this.row > row)       // if the tile is on the left and on the up to the bishop
            for (int i = 1; i < Math.abs(this.col - col); i++)    //can be this.row - row either nothing changes | calculate the left possible moves in the diognal
                if(board.getPiece(this.col - i, this.row - i) != null)  //checks wether there is a piece in the way starting from the bishop to the up left here
                    return true;

        //up-right
        if (this.col < col && this.row > row)
            for (int i = 1; i < Math.abs(this.col - col); i++)    //can be this.row - row either nothing changes
                if(board.getPiece(this.col + i, this.row - i) != null)
                    return true;

        //down-left
        if (this.col > col && this.row < row)
            for (int i = 1; i < Math.abs(this.col - col); i++)    //can be this.row - row either nothing changes
                if(board.getPiece(this.col - i, this.row + i) != null)
                    return true;
        //down-right
        if (this.col < col && this.row < row)
            for (int i = 1; i < Math.abs(this.col - col); i++)    //can be this.row - row either nothing changes
                if(board.getPiece(this.col + i, this.row + i) != null)
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

