package Game.GameEngine;

import GUI.Arrow;
import GUI.Tile;
import Game.Piece.*;
import resources.Variables;

import java.util.ArrayList;


public class FuckedUpCheckScanner {

    private ArrayList<Tile> tileList;
    private ArrayList<Piece>  pieceList;
    private ArrayList<Arrow>  arrowList;

    ChessEngine engine;

    int kingCol;
    int kingRow;

    public FuckedUpCheckScanner(ChessEngine engine){
        this.engine = engine;
        getLists();
    }

    public boolean isKingChecked (Move move) {

        Piece king = engine.findKing((move.piece.isWhite));     ///Determine the color of the king relative to the selected piece and find it on the board
        //assert king != null;        ///AT this point the king object cannot be null

        this.kingCol = king.col;
        this.kingRow = king.row;

        if(engine.selectedPiece != null && engine.selectedPiece instanceof King){
            this.kingCol = move.newCol;
            this.kingRow = move.newRow;
        }
/*
,0 , 1
,1 , 0
,0 ,-1
,-1, 0
 */

        return   hitByRook  (move, king, kingCol, kingRow)||   //up
                hitByRook  (move, king, kingCol, kingRow)||   //right
                hitByRook  (move, king, kingCol, kingRow)||   //down
                hitByRook  (move, king, kingCol, kingRow)||  //left
                hitByKing(king)   ||
                hitByBishop(king) ||
                hitByKnight(king) ||
                hitByPawn(king)   ;
    }
    /*
    hitByKing(king)
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

    /*

            return hitByRook   (move.newCol, move.newRow, king, kingCol, kingRow, 0, 1)||   //up
                    hitByRook  (move.newCol, move.newRow, king, kingCol, kingRow, 1, 0)||   //right
                    hitByRook  (move.newCol, move.newRow, king, kingCol, kingRow,0, -1)||   //down
                    hitByRook  (move.newCol, move.newRow, king, kingCol, kingRow, -1, 0)||  //left




     */

    private boolean hitByRook (Move move, Piece king, int kingCol, int kingRow, int colValue, int rowValue) {
        int col = move.newCol;
        int row = move.newRow;

        for (int i = 1; i < 8; i++) {
            if (kingCol + (i * colValue) == col && kingRow + (i * rowValue) == row) {
                break;
            }

            Piece piece = engine.getPiece(kingCol + (i * colValue), kingRow + (i * rowValue));
            if (piece != null && piece != engine.selectedPiece) {
                if (!engine.sameTeam(piece, king) && (piece.name.equals("Rook") || piece.name.equals("Queen"))) {
                    return true;
                }
                break;
            }
        }
        return false;
    }


    private boolean hitByRook(Move move, Piece king, int kingCol, int kingRow) {
        int[] preFixes = {-1, 1};

        // Checking along the rows (left-right)
        for (int preFix : preFixes) {
            for (int tileIterator = 1; tileIterator < 8; tileIterator++) {
                int targetCol = kingCol + (tileIterator * preFix);

                if (!checkBoardLimits(targetCol)) {
                    break;
                }

                if (targetCol == move.newCol && kingRow == move.newRow) {
                    break;
                }

                Piece piece = engine.getPiece(targetCol, kingRow);
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
                int targetRow = kingRow + (tileIterator * preFix);

                if (!checkBoardLimits(kingCol, targetRow)) {
                    break;
                }

                if (kingCol == move.newCol && targetRow == move.newRow) {
                    break;
                }

                Piece piece = engine.getPiece(kingCol, targetRow);
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


/*
    private boolean hitByRook(Piece king, Move move) {
        int[] preFixes = {-1, 1};

        // Checking along the rows (left-right)
        for (int preFix : preFixes) {
            for (int tileIterator = 1; tileIterator < 8; tileIterator++) {
                if (kingCol + (tileIterator * preFix) == move.newCol && kingRow == move.newRow)
                    break;

                int targetCol = this.kingCol + (preFix * tileIterator);
                if (!checkBoardLimits(targetCol)) {
                    break;
                }
                Piece piece = engine.getPiece(targetCol, this.kingRow);
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
                if(kingCol == move.newCol && kingRow + (tileIterator * preFix) == move.newRow)
                    break;

                int targetRow = this.kingRow + (preFix * tileIterator);
                if (!checkBoardLimits(targetRow)) {
                    break;
                }
                Piece piece = engine.getPiece(this.kingCol, targetRow);
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
 */


    private boolean hitByBishop(Piece king){
        int [] preFixes = {-1, 1};

        for(int colPrefix: preFixes)
            for(int rowPrefix: preFixes)
                for(int tileIterator = 1; tileIterator < 8; tileIterator++){
                    int targetCol = this.kingCol + (colPrefix * tileIterator);
                    int targetRow = this.kingRow + (rowPrefix * tileIterator);

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
            int targetCol = this.kingCol + move[0];
            int targetRow = this.kingRow + move[1];

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
                Piece adjacentPiece = engine.getPiece(this.kingCol + colFromKing, this.kingRow + rowFromKing);
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
        return  checkPawn(engine.getPiece(this.kingCol + 1, this.kingRow + colorVal), king) ||
                checkPawn(engine.getPiece(this.kingCol - 1, this.kingRow + colorVal), king);

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



/*
    private boolean hitByKing(Piece king, int kingCol, int kingRow) {
        return  checkKing(engine.getPiece(kingCol - 1, kingRow - 1), king) ||
                checkKing(engine.getPiece(kingCol + 1, kingRow - 1), king) ||
                checkKing(engine.getPiece(    kingCol    , kingRow - 1), king) ||
                checkKing(engine.getPiece(kingCol - 1,      kingRow)    , king) ||
                checkKing(engine.getPiece(kingCol + 1,      kingRow)    , king) ||
                checkKing(engine.getPiece(kingCol - 1, kingRow + 1), king) ||
                checkKing(engine.getPiece(kingCol + 1, kingRow + 1), king) ||
                checkKing(engine.getPiece(    kingCol    , kingRow + 1), king);
    }

    private boolean checkKing(Piece p, Piece k) {
        return p != null && !engine.sameTeam(p, k) && p.name.equals("King");
    }
 */

/*
    private boolean hitByKnight (int col, int row, Piece king, int kingCol, int kingRow) {
        return  checkKnight(engine.getPiece(kingCol - 1, kingRow - 2), king, col, row) ||
                checkKnight(engine.getPiece(kingCol + 1, kingRow - 2), king, col, row) ||
                checkKnight(engine.getPiece(kingCol + 2, kingRow - 1), king, col, row) ||
                checkKnight(engine.getPiece(kingCol + 2, kingRow + 1), king, col, row) ||
                checkKnight(engine.getPiece(kingCol + 1, kingRow + 2), king, col, row) ||
                checkKnight(engine.getPiece(kingCol - 1, kingRow + 2), king, col, row) ||
                checkKnight(engine.getPiece(kingCol - 2, kingRow + 1), king, col, row) ||
                checkKnight(engine.getPiece(kingCol - 2, kingRow - 1), king, col, row);
    }

    private boolean checkKnight(Piece piece, Piece king, int col, int row) {
        return piece != null && !engine.sameTeam(piece, king) && piece.name.equals("Knight") ;  //&& !(p.col == col && p.row == row)
        ///I've commented the last part and didn't added it to the new cleaned up project so just keep it in mind
    }
 */

    /*


    //col and row for newCol and newRow
    //colValue and rowValue -1 -1, -1 1, 1 -1, 1 1
    private boolean hitByBishop (int col, int row, Piece king, int kingCol, int kingRow, int colValue, int rowValue) {
        for (int i = 1; i < 8; i++) {
            if (kingCol - (i * colValue) == col && kingRow - (i * rowValue) == row) {
                break;
            }

            Piece piece = engine.getPiece(kingCol - (i * colValue), kingRow - (i * rowValue));
            if (piece != null && piece != engine.selectedPiece) {
                if (!engine.sameTeam(piece, king) && (piece.name.equals("Bishop") || piece.name.equals("Queen"))) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

     */



    /*
    hitByRook  (move, king, kingCol, kingRow, 0, 1)||   //up
                hitByRook  (move, king, kingCol, kingRow, 1, 0)||   //right
                hitByRook  (move, king, kingCol, kingRow,0, -1)||   //down
                hitByRook  (move, king, kingCol, kingRow, -1, 0)||  //left
                0, 1
                0,-1
                1,0
                -1,0

                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, -1, -1)|| //up left
                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, 1, -1)||  //up right
                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, 1, 1)||   //down right
                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, -1, 1)||  //down left

  private boolean hitByRook (Move move, Piece king, int kingCol, int kingRow, int colValue, int rowValue) {
        for (int i = 1; i < 8; i++) {
            if (kingCol + (i * colValue) == move.newCol && kingRow + (i * rowValue) == move.newRow) {
                break;
            }

            Piece piece = engine.getPiece(kingCol + (i * colValue), kingRow + (i * rowValue));
            if (piece != null && piece != engine.selectedPiece) {
                if (!engine.sameTeam(piece, king) && (piece.name.equals("Rook") || piece.name.equals("Queen"))) {
                    return true;
                }
                break;
            }
        }
        return false;
    }
     */






/*

private boolean hitByKing (Move move, Piece king,int kingCol, int kingRow){

    for (int colFromKing = -1; colFromKing <= 1; colFromKing++)
        for (int rowFromKing = -1; rowFromKing <= 1; rowFromKing++) {
            if (colFromKing == 0 && rowFromKing == 0) {   ///exclude the king's current position
                continue;
            }

            int targetCol = kingCol + colFromKing;
            int targetRow = kingRow + rowFromKing;

            if(!withinBoardLimits(targetCol, targetRow))
                continue;

            if (targetCol == move.newCol && targetRow == move.newRow) {
                break;
            }

            /// Check the piece in the adjacent position
            Piece adjacentPiece = engine.getPiece(targetCol, targetRow);
            if (adjacentPiece != null)
                if (checkKing(adjacentPiece, king)) { // Correct method name
                    return true;
                }
        }
    ///If no piece satisfies the condition then return false
    return false;
}

private boolean checkking (Piece piece, Piece king){
    boolean isNotSameTeam = !engine.sameTeam(piece, king);
    boolean isAKing = piece instanceof King;

    return isNotSameTeam && isAKing;
}


 */


/*
        private boolean hitByKing (Piece king,int kingCol, int kingRow){

            for (int colFromKing = -1; colFromKing <= 1; colFromKing++)
                for (int rowFromKing = -1; rowFromKing <= 1; rowFromKing++) {
                    if (colFromKing == 0 && rowFromKing == 0)   ///exclude the king's current position
                        continue;

                    /// Check the piece in the adjacent position
                    Piece adjacentPiece = engine.getPiece(kingCol + colFromKing, kingRow + rowFromKing);
                    if (adjacentPiece != null)
                        return checkking(adjacentPiece, king);
                }
            ///If no piece satisfies the condition then return false
            return false;
        }

        private boolean checkking (Piece piece, Piece king){
            boolean isNotSameTeam = !engine.sameTeam(piece, king);
            boolean isAKing = piece instanceof King;

            return isNotSameTeam && isAKing;
        }
 */