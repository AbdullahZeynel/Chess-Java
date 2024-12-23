package Game.Piece;


import GUI.Board.Board;
import Game.GameEngine.ChessEngine;
import Game.GameEngine.Move;
import resources.Variables;

import java.awt.image.BufferedImage;

public class King extends Piece{

//    public boolean isKingChecked;

    public King (ChessEngine engine, int col, int row, boolean isWhite){
        super(engine);
        this.col = col;
        this.row = row;
        this.xPos = col * Variables.tileSize;
        this.yPos = row * Variables.tileSize;

        this.isWhite = isWhite;
        this.name = "King";

        //this.isKingChecked = false;     //default is false

        this.sprite = sheet.getSubimage(0 * sheetScale, (isWhite ? 0 : sheetScale), sheetScale, sheetScale)
                           .getScaledInstance(Variables.tileSize, Variables.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    @Override
    public boolean pieceMoves(int col, int row) {
        return (Math.abs(col - this.col) <= 1 && Math.abs(row - this.row) <= 1) ||
                canCastle(col, row);      //King can only move one move!
    }

    //Castle
    private boolean canCastle(int col, int row) {
        int pieceRow = isWhite ? 7 : 0;     //for the castle move to be applied with white pieces it must be on the 7th row and with black pieces 0th row

        if (pieceRow == row) {  //if the desired move is on the same row with the king
            if (col == 6) {     // 6 out of 7 with 0 included this is the king side caslte move
                Piece rook = engine.getPiece(7, row);    //get the piece at the rook position
                if (rook != null && rook.name.equals("Rook") && rook.isWhite == isWhite)      //validate the piece's identity (piece type and color) and it's existance
                    if (rook.isFirstMove == isFirstMove) {              //check wether the king and the rook had moved or not
                        return  engine.getPiece(5, row) == null &&        //is there a piece in between?
                                engine.getPiece(6, row) == null &&       //is there a piece in between?
                               !engine.checkScanner.isKingChecked(new Move(engine, this, 5, row));   //is the king checked?
                }
            } else if (col == 2) {  //this is the queen side castle move
                Piece rook = engine.getPiece(0, row);
                if ( rook != null && rook.name.equals("Rook")&& rook.isWhite == isWhite)
                    if (rook.isFirstMove == isFirstMove) {
                        return engine.getPiece(3, row) == null &&
                               engine.getPiece(2, row) == null &&
                               engine.getPiece(1, row) == null &&
                              !engine.checkScanner.isKingChecked(new Move(engine, this, 3, row));
                    }
            }
        }
        return false;
    }
}
