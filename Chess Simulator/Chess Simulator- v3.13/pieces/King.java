package pieces;

import main.Board;
import main.Move;

import java.awt.image.BufferedImage;

public class King extends Piece {


    public King(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * board.tile_size;
        this.yPos = row * board.tile_size;

        this.isWhite = isWhite;
        this.name = "King";


        this.sprite = sheet.getSubimage(0 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(board.tile_size, board.tile_size, BufferedImage.SCALE_SMOOTH);
    }

    @Override
    public boolean isValidMovement(int col, int row) {
        //System.out.println("King: col: " + col + ", row: " + row);

        return (Math.abs(col - this.col) <= 1 && Math.abs(row - this.row) <= 1) ||
                canCastle(col, row);

    }

    //Castle
    private boolean canCastle(int col, int row) {

        int pieceRow = isWhite ? 7 : 0;     //for the castle move to be applied with white pieces it must be on the 7th row and with black pieces 0th row
        if (pieceRow == row) {  //if the desired move is on the same row with the king
            if (col == 6) {     // 6 out of 7 with 0 included this is the king side caslte move
                Piece rook = board.getPiece(7, pieceRow);    //get the piece at the rook position
                if (rook != null && rook.name.equals("Rook") && rook.isWhite == this.isWhite)      //validate the piece's identity (piece type and color) and it's existance
                    if (rook.isFirstMove == isFirstMove) {              //check wether the king and the rook had moved or not
                        return board.getPiece(5, row) == null &&        //is there a piece in between?
                                board.getPiece(6, row) == null &&       //is there a piece in between?
                                !board.checkScanner.isKingChecked(new Move(board, this, 5, row));   //is the king checked?
                    }
            } else if (col == 2) {  //this is the queen side castle move

                Piece rook = board.getPiece(0, pieceRow);
                if (rook != null && rook.name.equals("Rook") && rook.isWhite == this.isWhite)
                    if (rook.isFirstMove && isFirstMove) {
                        return board.getPiece(3, row) == null &&
                                board.getPiece(2, row) == null &&
                                board.getPiece(1, row) == null &&
                                !board.checkScanner.isKingChecked(new Move(board, this, 3, row));
                    }

            }
        }
        return false;
    }
}
