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
        //up-left
        if (this.col > col && this.row > row)
            for (int i = 1; i < Math.abs(this.col - col); i++)    //can be this.row - row either nothing changes
                if(board.getPiece(this.col - i, this.row - i) != null)
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
    }
}