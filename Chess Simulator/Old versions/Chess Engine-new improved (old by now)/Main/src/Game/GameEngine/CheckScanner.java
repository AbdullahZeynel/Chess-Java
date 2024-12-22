package Game.GameEngine;


import GUI.Arrow;
import GUI.Tile;
import Game.Piece.*;
import resources.Variables;

import java.util.ArrayList;

public class CheckScanner {

    private ArrayList<Tile> tileList;
    private ArrayList<Piece>  pieceList;
    private ArrayList<Arrow>  arrowList;

    ChessEngine engine;

    public CheckScanner(ChessEngine engine){
        this.engine = engine;
        getLists();
    }


    public boolean isKingChecked (Move move) {

        Piece king = engine.findKing((move.piece.isWhite));     ///Determine the color of the king relative to the selected piece and find it on the board
        //assert king != null;        ///AT this point the king object cannot be null

        if(engine.selectedPiece != null && engine.selectedPiece instanceof King){
            king.col = move.newCol;
            king.row = move.newRow;
        }

        return  hitByKing(king);
    }
    /*
                hitByRook(king)   ||
                hitByBishop(king) ||
                hitByKnight(king) ||
                hitByPawn(king)   ||
     */


    private void getLists(){
        pieceList = engine.invokeIfAllowed(this.getClass(),"pieceList");

        arrowList = engine.invokeIfAllowed(this.getClass(),"arrowList");

        tileList = engine.invokeIfAllowed(this.getClass(),"tileList");

    }

    private boolean checkBoardLimits(int col, int row){
        boolean validCol = (0 <= col && col <= 7);
        boolean validRow = (0 <= row && row <= 7);
        return validCol && validRow;
    }

    private boolean checkBoardLimits(int colOrRow){
        return (0 <= colOrRow && colOrRow <= 7);
    }


    private boolean hitByRook(Piece king) {
        int[] preFixes = {-1, 1};

        // Checking along the rows (left-right)
        for (int preFix : preFixes) {
            for (int tileIterator = 1; tileIterator < 8; tileIterator++) {
                int targetCol = king.col + (preFix * tileIterator);
                if (!checkBoardLimits(targetCol)) {
                    break;
                }
                Piece piece = engine.getPiece(targetCol, king.row);
                if (piece instanceof Rook || piece instanceof Queen) {
                    return !engine.sameTeam(piece, king);
                }
                if (piece != null) {
                    break;
                }
            }
        }

        // Checking along the columns (up-down)
        for (int preFix : preFixes) {
            for (int tileIterator = 1; tileIterator < 8; tileIterator++) {
                int targetRow = king.row + (preFix * tileIterator);
                if (!checkBoardLimits(targetRow)) {
                    break;
                }
                Piece piece = engine.getPiece(king.col, targetRow);
                if (piece instanceof Rook || piece instanceof Queen) {
                    return !engine.sameTeam(piece, king);
                }
                if (piece != null) {
                    break;
                }
            }
        }

        return false;
    }


    private boolean hitByBishop(Piece king){
        int [] preFixes = {-1, 1};

        for(int colPrefix: preFixes)
            for(int rowPrefix: preFixes)
                for(int tileIterator = 1; tileIterator < 8; tileIterator++){
                    int targetCol = king.col + (colPrefix * tileIterator);
                    int targetRow = king.row + (rowPrefix * tileIterator);

                    if (!checkBoardLimits(targetCol, targetRow))
                        continue;

                    Piece piece = engine.getPiece(targetCol, targetRow);
                    if(piece instanceof Bishop || piece instanceof Queen)
                        return !engine.sameTeam(piece, king);

                    if (piece != null)
                        break;
            }
        return false;
    }





    private boolean hitByKnight(Piece king){
            int[][] knightMoves = {
                    {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                    {1, 2}, {1, -2}, {-1, 2}, {-1, -2} };
            for(int[] move: knightMoves){
                int targetCol = king.col + move[0];
                int targetRow = king.row + move[1];

                if(!checkBoardLimits(targetCol, targetRow))
                    return false;

                Piece piece = engine.getPiece(targetCol, targetRow);
                if(piece instanceof Knight && !engine.sameTeam(piece, king))
                    return true;
            }
            return false;
    }


    private boolean hitByKing(Piece king){

        for(int colFromKing = -1; colFromKing <= 1; colFromKing++)
            for(int rowFromKing = -1; rowFromKing <= 1; rowFromKing++){
                if (colFromKing == 0 && rowFromKing == 0)   ///exclude the king's current position
                    continue;

                /// Check the piece in the adjacent position
                Piece adjacentPiece = engine.getPiece(king.col + colFromKing, king.row + rowFromKing);
                if (adjacentPiece != null)
                    return checkking(adjacentPiece, king);
            }
        ///If no piece satisfies the condition then return false
        return false;
    }

    private boolean checkking(Piece piece, Piece king){
        boolean isNotSameTeam = !engine.sameTeam(piece, king);
        boolean isAKing = piece instanceof King;

        return isNotSameTeam && isAKing;
    }

    private boolean hitByPawn(Piece king){
        int colorVal = king.isWhite ? -1 : 1;
        return  checkPawn(engine.getPiece(king.col + 1, king.row + colorVal), king) ||
                checkPawn(engine.getPiece(king.col - 1, king.row + colorVal), king);

    }

    private boolean checkPawn(Piece piece, Piece king){
        boolean isAPawn = piece instanceof Pawn;
        boolean isNotSameTeam = !engine.sameTeam(piece, king);
        boolean isNotNull = piece != null;

        return isNotNull && isNotSameTeam && isAPawn;
    }


    public boolean isGameOver(Piece king) {
        for (Piece piece: pieceList)
            if(engine.sameTeam(piece, king)) {
                engine.selectedPiece = (piece == king) ? king : null;

                for(int row = 0; row < Variables.rows; row++)
                    for(int col = 0; col < Variables.cols; col++) {
                        Move move = new Move(engine, piece, col, row);
                        if(engine.isValidMove(move))    ///Valid move found, game is not over yet
                            return false;
                    }
            }
        return true;    ///No move found so game over
    }
}