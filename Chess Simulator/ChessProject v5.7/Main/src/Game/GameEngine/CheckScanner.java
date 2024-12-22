package Game.GameEngine;

import GUI.Arrow;
import GUI.Tile;
import Game.Piece.*;
import resources.Variables;

import java.util.ArrayList;

public class CheckScanner {

    private ArrayList<Piece> pieceList; ///Importing the pieceList from the ChessEngine

    ChessEngine engine;

    public CheckScanner(ChessEngine engine) {
        this.engine = engine;
        getPieceList();         ///Load the pieceList (Encapsulation!)
    }

    private void getPieceList() {
        /// Because the CheckScanner class is included inside the engine.invokeIfAllowed() function
        /// We can load the private ArrayList pieceList successfully!
        pieceList = engine.invokeIfAllowed(this.getClass(), "pieceList");
    }


    /*
     *  Here we're trying to determine whether the king is in check or not by evaluating every possible attack type
     *  including (rook, bishop, knight, queen, king and pawn) for the king's current position.
     *  Also considering the new move made on the board.
     *
     */
    public boolean isKingChecked(Move move) {
        /// Locate the King relative to the move.piece object's color(isWhite)
        Piece king = engine.findKing((move.piece.isWhite));

        int kingCol = king.col;
        int kingRow = king.row;

        /// If the moving piece is the King itself then update the hypothetical position of the king
        /// to the requested move to check for any impossible moves
        if (engine.selectedPiece != null && engine.selectedPiece instanceof King) {
            kingCol = move.newCol;
            kingRow = move.newRow;
        }


        /*
        *   Now we're gonna check for attacks on the King or on the King's possible move tiles', using
        *   helper methods such as hitByRook, hitByKnight, hitByBishop, hitByKing and hitByPawn.
        *
        *   In every single method we're hypothetically putting the relative piece in the King's tile
        *   and evaluating every possible attack through checking the relative piece's moving algorithm
        */
        return  hitByRook   (move, king, kingCol, kingRow) ||
                hitByBishop (move, king, kingCol, kingRow) ||
                hitByKnight (king, kingCol, kingRow)       ||
                hitByPawn   (king, kingCol, kingRow)       ||
                hitByKing   (move,  king, kingCol, kingRow);
    }

    /*
    *   Taking the King's current position as our starting point through passing the kingCol and kingRow values as integers
    *   we're calling the checkRook method with explicit values such as colValue and rowValue.
    *
    *   The logic behind these values is them being the representations of Rook's moving algorithm.
    *   Because the rook is able to move either horizontally or vertically, in either way one of it's col or row values
    *   must always remain the same.
    *
    *   The same can be applied for the Queen so we don't have another seperate method for checking the Queen.
    *   We're simply checking the Queen while checking the Rook and Bishop
    */
    private boolean hitByRook(Move move, Piece king, int kingCol, int kingRow) {
        int[][] rookDirections = {
                {0, 1}, {0, -1}, {1, 0}, {-1, 0}};

        for (int[] direction : rookDirections) {
            if (checkRook(move, king, kingCol, kingRow, direction[0], direction[1]))
                return true;    /// If an enemy Rook or Queen is spotted in any one of the given direction then the King
                /// is under attack! Returning true here indicates
                /// that the given move tile is being hit either vertically or horizontally
        }
        return false;       /// No threat!
    }


    private boolean checkRook(Move move, Piece king, int kingCol, int kingRow, int colValue, int rowValue) {
        for (int i = 1; i < 8; i++) {   /// Scan all the tiles in the given direction
            int targetCol = kingCol + (i * colValue);
            int targetRow = kingRow + (i * rowValue);

            if (!withinBoardLimits(targetCol, targetRow)) {
                break; /// Stop if out of bounds
            }

            if (targetCol == move.newCol && targetRow == move.newRow) {
                break; /// Break the loop if encountered with the very same move being checked
                /// Here is why: Even if there is an enemy Rook or Queen at the same line
                /// as long as there is a piece blocking their way, it must not be considered as a Check threat.
                /// In fact the main aim in these methods is not to check the entire line but either to check the line
                /// until being encountered with the very same given move or another piece
            }

            Piece piece = engine.getPiece(targetCol, targetRow);
            boolean isNotNull = piece != null;
            boolean isNotSelectedPiece = piece != engine.selectedPiece;
            boolean isTheSameTeam = engine.sameTeam(piece, king);
            boolean instanceOfRookOrQueen = (piece instanceof Rook || piece instanceof Queen);

            if (isNotNull && isNotSelectedPiece) {
                if (!isTheSameTeam && instanceOfRookOrQueen) {
                    return true; /// If there is in fact a piece blocking the way and the very same piece is an
                    /// enemy Rook or Queen then return true. The tile is being hit, raise a threat!
                }
                break; /// It doesn't matter whether the piece is an enemy piece or not as long as
                /// it can't attack horizontally or vertically
            }
        }
        return false;   /// If no threat found then return false.
    }

    /*
    *   The very same logic is being applied to the Bishop
    *   with the mere exception being that this time we're checking the diagonal tiles.
    */
    private boolean hitByBishop(Move move, Piece king, int kingCol, int kingRow) {
        int[] preFixes = {-1, 1};

        for (int colPrefix : preFixes)
            for (int rowPrefix : preFixes)
                if (checkBishop(move, king, kingCol, kingRow, colPrefix, rowPrefix))
                    return true;       /// Threat found!
        return false;   /// No threat reported!
    }

    private boolean checkBishop(Move move, Piece king, int kingCol, int kingRow, int colPrefix, int rowPrefix) {
        for (int i = 1; i < 8; i++) {
            int targetCol = kingCol + (colPrefix * i);
            int targetRow = kingRow + (rowPrefix * i);

            if (!withinBoardLimits(targetCol, targetRow)) {
                break;      /// Break the loop when out of bonds
            }

            if (targetCol == move.newCol && targetRow == move.newRow) {
                break;      /// Also break the loop when encountered with the tile of the given move
            }

            Piece piece = engine.getPiece(targetCol, targetRow);
            if (piece != null && piece != engine.selectedPiece) {
                if (!engine.sameTeam(piece, king) && (piece instanceof Bishop || piece instanceof Queen)) {
                    return true;    /// Either and enemy Bishop or Queen is found. Threat found!
                }
                break; /// Stop further checks in this direction if any other piece is found.
            }
        }
        return false;   /// No threat!
    }

    /*
    *   Instead of using some complicated looping algorithm here to evalute the possible Knight moves
    *   we're using explicit move casting to check all the possible Knight moves that can be applied from
    *   the given king position.
     */
    private boolean hitByKnight (Piece king,int kingCol, int kingRow){
        int[][] knightMoves = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}};
        for (int[] move : knightMoves) {
            int targetCol = kingCol + move[0];
            int targetRow = kingRow + move[1];

            if (!withinBoardLimits(targetCol, targetRow))
                continue;   /// If out of bonds then continue and DON'T BREAK
                /// Knight's moves are not the same as the Rook or the Bishop, we must scan the other moves!
                /// Also keep in mind that this time we're not checking the moves with a loop but instead with explicit casting.

            Piece piece = engine.getPiece(targetCol, targetRow);
            if (piece != null)
                if (piece instanceof Knight && !engine.sameTeam(piece, king))
                    return true;    /// Enemy Knight found, raise threat!
        }
        return false;   /// No threats were found!
    }

    /*
    *   Scan all the possible moves the King has and determine wether the King is encountering
    *   the other king or not!
    *
    *   In any given position, if not out of bounds and not blocked neither under attack a King can move
    *   like this:
    *   (-1,-1), (-1,0), (-1,1), (0,-1), (0,1), (1,-1), (1,0), (1,1),  with  (0,0) being the King's current position.
    */
    private boolean hitByKing (Move move, Piece king, int kingCol, int kingRow){

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

    private boolean checkKing (Piece piece, Piece king){
        boolean isNotSameTeam = !engine.sameTeam(piece, king);
        boolean isAKing = piece instanceof King;

        return isNotSameTeam && isAKing;
    }

    private boolean hitByPawn (Piece king,int kingCol, int kingRow){
            int colorVal = king.isWhite ? -1 : 1;
            return checkPawn(engine.getPiece(kingCol + 1, kingRow + colorVal), king) ||
                    checkPawn(engine.getPiece(kingCol - 1, kingRow + colorVal), king);
        }

    private boolean checkPawn (Piece piece, Piece king){
        boolean isAPawn = (piece != null) && piece instanceof Pawn;
        boolean isNotSameTeam = !engine.sameTeam(piece, king);
        return isAPawn && isNotSameTeam;
    }

    /// Checks wether the given col and row values are within the bounds of the board or not
    private boolean withinBoardLimits ( int col, int row){
        boolean validCol = (0 <= col && col <= 7);
        boolean validRow = (0 <= row && row <= 7);
        return validCol && validRow;
    }

    /// The same method being overloaded. It's possible to call the method with only the col or row value.
    private boolean withinBoardLimits ( int colOrRow){
        return (0 <= colOrRow && colOrRow <= 7);
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



