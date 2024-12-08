package pieces;

import main.Board;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

public class Pawn extends Piece {
    public Pawn(Board board, int col, int row, boolean isWhite){
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * board.tile_size;
        this.yPos = row * board.tile_size;

        this.isWhite = isWhite;
        this.name = "Pawn";

        this.sprite = sheet.getSubimage(5 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(board.tile_size, board.tile_size, BufferedImage.SCALE_SMOOTH);
    }

    @Override
    public boolean isValidMovement(int col, int row) {
        int colorIndex = isWhite ? 1 : -1;

        //push pawn 1
        if (this.col == col && row == this.row - colorIndex && board.getPiece(col,row) == null)
            return true;

        //push pawn two

        if (!hasMoved && this.col == col && row == this.row - colorIndex * 2&& board.getPiece(col,row) == null && board.getPiece(col,row + colorIndex) == null)
            return true;

        //capture to the left
        if ( col == this.col - 1 && row == this.row - colorIndex && board.getPiece(col,row) != null)
            return true;

        //capture to the right
        if ( col == this.col + 1 && row == this.row - colorIndex && board.getPiece(col, row) != null)
            return true;

        //en passant left
        if (board.getTileNum(col, row) == board.enPassantTile && col == this.col - 1 && row == this.row - colorIndex && board.getPiece(col, row + colorIndex) != null){
            return true;
        }

        //en passant right
        if (board.getTileNum(col, row) == board.enPassantTile && col == this.col + 1 && row == this.row - colorIndex && board.getPiece(col, row + colorIndex) != null){
            return true;
        }

        return false;
    }
}
