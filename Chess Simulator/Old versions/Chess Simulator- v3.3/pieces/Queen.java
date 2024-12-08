package pieces;

import main.Board;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Queen extends Piece {
    public Queen(Board board, int col, int row, boolean isWhite){
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * board.tile_size;
        this.yPos = row * board.tile_size;

        this.isWhite = isWhite;
        this.name = "Queen";

        this.sprite = sheet.getSubimage(1 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(board.tile_size, board.tile_size, BufferedImage.SCALE_SMOOTH);


    }

    @Override
    public boolean isValidMovement(int col, int row) {
        boolean diognalMoves = (Math.abs(col - this.col) == Math.abs(row - this.row));
        boolean horizontalMoves = ((col == this.col) || (row == this.row));

        return horizontalMoves || diognalMoves;
    }

    @Override
    public boolean moveCollidesWithPiece(int col, int row) {
        //like a rook
        if(this.col == col || this.row == row){
            //left
            if (this.col > col)
                for (int c = this.col - 1; c > col; c--)
                    if(board.getPiece(c, this.row) != null)
                        return true;

            //right
            if (this.col < col)
                for (int c = this.col + 1; c < col; c++)
                    if(board.getPiece(c, this.row) != null)
                        return true;

            //up
            if (this.row > row)
                for (int r = this.row - 1; r > row; r--)
                    if(board.getPiece(this.col, r) != null)
                        return true;

            //down
            if (this.row < row)
                for (int r = this.row + 1; r < row; r++)
                    if(board.getPiece(this.col, r) != null)
                        return true;
        } else {
            //like a bishop
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
        }
        return false;
    }
}
