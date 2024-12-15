package main;

import pieces.King;
import pieces.Piece;

public class Move {

    int oldCol;
    int oldRow;
    int newCol;
    int newRow;

    Piece piece;
    Piece capture;

    boolean isKingChecked;      //I wanna add this to the King class

    public Move(Board board, Piece piece, int newCol, int newRow){
        this.oldCol= piece.col;
        this.oldRow= piece.row;
        this.newCol= newCol;
        this.newRow= newRow;

        this.piece = piece;

        if (piece instanceof King) {
            this.isKingChecked = false;     //default value
        }

        this.capture = board.getPiece(newCol, newRow);      //returns either null or Piece piece
    }
}
