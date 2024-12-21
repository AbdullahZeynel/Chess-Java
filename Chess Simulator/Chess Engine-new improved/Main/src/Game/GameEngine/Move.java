package Game.GameEngine;

import Game.Piece.Piece;

public class Move {

    int oldCol;
    int oldRow;
    int newCol;
    int newRow;

    Piece piece;
    Piece capturedPiece;

    public Move(ChessEngine engine, Piece piece, int newCol, int newRow){
        this.oldCol= piece.col;
        this.oldRow= piece.row;
        this.newCol= newCol;
        this.newRow= newRow;

        this.piece = piece;

        this.capturedPiece = engine.getPiece(newCol, newRow);      ///either returns null or a piece
        ///the aim is to get the piece in the desired location (if exists!)
    }
}
