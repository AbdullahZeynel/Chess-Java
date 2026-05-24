package Game.GameEngine;

import GUI.Arrow;
import GUI.Tile;
import Game.Piece.*;
import resources.Variables;

import java.util.ArrayList;

/**
 * Detects checks, checkmates, and stalemates by evaluating all possible
 * attack vectors against the king's position.
 *
 * The approach: for each attack type (rook, bishop, knight, pawn, king),
 * we hypothetically place the corresponding piece on the king's square
 * and check if it could "see" an enemy piece of that type.
 */
public class CheckScanner {

    private ArrayList<Piece> pieceList;

    ChessEngine engine;

    public CheckScanner(ChessEngine engine) {
        this.engine = engine;
        getPieceList();
    }

    /// Load the piece list using the engine's access control mechanism
    private void getPieceList() {
        pieceList = engine.invokeIfAllowed(this.getClass(), "pieceList");
    }


    /**
     * Determines whether the king is in check after the given move.
     * Evaluates all possible attack vectors: rook/queen lines, bishop/queen diagonals,
     * knight jumps, pawn captures, and adjacent king threats.
     */
    public boolean isKingChecked(Move move) {
        /// Locate the king matching the moving piece's color
        Piece king = engine.findKing((move.piece.isWhite));

        int kingCol = king.col;
        int kingRow = king.row;

        /// If the moving piece IS the king, use the target position for evaluation
        if (engine.selectedPiece != null && engine.selectedPiece instanceof King) {
            kingCol = move.newCol;
            kingRow = move.newRow;
        }

        return  hitByRook   (move, king, kingCol, kingRow) ||
                hitByBishop (move, king, kingCol, kingRow) ||
                hitByKnight (king, kingCol, kingRow)       ||
                hitByPawn   (king, kingCol, kingRow)       ||
                hitByKing   (move,  king, kingCol, kingRow);
    }

    /**
     * Checks all four straight-line directions (up, down, left, right)
     * for enemy Rooks or Queens that could attack the king.
     */
    private boolean hitByRook(Move move, Piece king, int kingCol, int kingRow) {
        int[][] rookDirections = {
                {0, 1}, {0, -1}, {1, 0}, {-1, 0}};

        for (int[] direction : rookDirections) {
            if (checkRook(move, king, kingCol, kingRow, direction[0], direction[1]))
                return true;    /// Threat found along this line
        }
        return false;
    }

    /**
     * Scans along a single straight-line direction from the king's position.
     * Stops at: board edge, the move's target tile (blocking piece), or any piece.
     * Returns true if an enemy Rook or Queen is found before being blocked.
     */
    private boolean checkRook(Move move, Piece king, int kingCol, int kingRow, int colValue, int rowValue) {
        for (int i = 1; i < 8; i++) {
            int targetCol = kingCol + (i * colValue);
            int targetRow = kingRow + (i * rowValue);

            if (!withinBoardLimits(targetCol, targetRow)) {
                break; /// Out of bounds
            }

            if (targetCol == move.newCol && targetRow == move.newRow) {
                break; /// The moving piece blocks this line — even if there's an attacker
                       /// behind it, the path is obstructed
            }

            Piece piece = engine.getPiece(targetCol, targetRow);
            boolean isNotNull = piece != null;
            boolean isNotSelectedPiece = piece != engine.selectedPiece;
            boolean isTheSameTeam = engine.sameTeam(piece, king);
            boolean instanceOfRookOrQueen = (piece instanceof Rook || piece instanceof Queen);

            if (isNotNull && isNotSelectedPiece) {
                if (!isTheSameTeam && instanceOfRookOrQueen) {
                    return true; /// Enemy Rook or Queen found — king is under attack!
                }
                break; /// Any other piece blocks the line, no threat from this direction
            }
        }
        return false;
    }

    /**
     * Checks all four diagonal directions for enemy Bishops or Queens.
     * Same logic as hitByRook but along diagonals.
     */
    private boolean hitByBishop(Move move, Piece king, int kingCol, int kingRow) {
        int[] preFixes = {-1, 1};

        for (int colPrefix : preFixes)
            for (int rowPrefix : preFixes)
                if (checkBishop(move, king, kingCol, kingRow, colPrefix, rowPrefix))
                    return true;
        return false;
    }

    /// Scans a single diagonal direction for enemy Bishop or Queen threats
    private boolean checkBishop(Move move, Piece king, int kingCol, int kingRow, int colPrefix, int rowPrefix) {
        for (int i = 1; i < 8; i++) {
            int targetCol = kingCol + (colPrefix * i);
            int targetRow = kingRow + (rowPrefix * i);

            if (!withinBoardLimits(targetCol, targetRow)) {
                break;
            }

            if (targetCol == move.newCol && targetRow == move.newRow) {
                break;
            }

            Piece piece = engine.getPiece(targetCol, targetRow);
            if (piece != null && piece != engine.selectedPiece) {
                if (!engine.sameTeam(piece, king) && (piece instanceof Bishop || piece instanceof Queen)) {
                    return true;    /// Enemy Bishop or Queen found on diagonal!
                }
                break; /// Path blocked by another piece
            }
        }
        return false;
    }

    /**
     * Checks all eight possible knight-jump positions for an enemy Knight.
     * Unlike sliding pieces, knights don't need path checking — they jump.
     * We use explicit offset arrays instead of a scanning loop.
     */
    private boolean hitByKnight (Piece king,int kingCol, int kingRow){
        int[][] knightMoves = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}};
        for (int[] move : knightMoves) {
            int targetCol = kingCol + move[0];
            int targetRow = kingRow + move[1];

            if (!withinBoardLimits(targetCol, targetRow))
                continue;   /// Out of bounds — but keep checking other positions!
                            /// Knights have discontinuous moves, unlike sliding pieces.

            Piece piece = engine.getPiece(targetCol, targetRow);
            if (piece != null)
                if (piece instanceof Knight && !engine.sameTeam(piece, king))
                    return true;    /// Enemy Knight found!
        }
        return false;
    }

    /**
     * Checks all eight adjacent squares for the opposing king.
     * This prevents two kings from occupying adjacent squares.
     *
     * King movement offsets: (-1,-1), (-1,0), (-1,1), (0,-1), (0,1), (1,-1), (1,0), (1,1)
     * with (0,0) being the king's current position (excluded).
     */
    private boolean hitByKing (Move move, Piece king, int kingCol, int kingRow){

        for (int colFromKing = -1; colFromKing <= 1; colFromKing++)
            for (int rowFromKing = -1; rowFromKing <= 1; rowFromKing++) {
                if (colFromKing == 0 && rowFromKing == 0) {
                    continue; /// Skip the king's own position
                }

                int targetCol = kingCol + colFromKing;
                int targetRow = kingRow + rowFromKing;

                if(!withinBoardLimits(targetCol, targetRow))
                    continue;

                if (targetCol == move.newCol && targetRow == move.newRow) {
                    break;
                }

                Piece adjacentPiece = engine.getPiece(targetCol, targetRow);
                if (adjacentPiece != null)
                    if (checkKing(adjacentPiece, king)) {
                        return true;
                    }
            }
        return false;
    }

    /// Returns true if the piece is an enemy king
    private boolean checkKing (Piece piece, Piece king){
        boolean isNotSameTeam = !engine.sameTeam(piece, king);
        boolean isAKing = piece instanceof King;

        return isNotSameTeam && isAKing;
    }

    /// Checks if either diagonal pawn-capture square contains an enemy pawn
    private boolean hitByPawn (Piece king,int kingCol, int kingRow){
            int colorVal = king.isWhite ? -1 : 1;
            return checkPawn(engine.getPiece(kingCol + 1, kingRow + colorVal), king) ||
                    checkPawn(engine.getPiece(kingCol - 1, kingRow + colorVal), king);
        }

    /// Returns true if the piece is an enemy pawn
    private boolean checkPawn (Piece piece, Piece king){
        boolean isAPawn = (piece != null) && piece instanceof Pawn;
        boolean isNotSameTeam = !engine.sameTeam(piece, king);
        return isAPawn && isNotSameTeam;
    }

    /// Validates that the given coordinates are within the 8x8 board
    private boolean withinBoardLimits ( int col, int row){
        boolean validCol = (0 <= col && col <= 7);
        boolean validRow = (0 <= row && row <= 7);
        return validCol && validRow;
    }

    /// Overloaded — validates a single coordinate
    private boolean withinBoardLimits ( int colOrRow){
        return (0 <= colOrRow && colOrRow <= 7);
    }


    /**
     * Determines if the game is over (checkmate or stalemate).
     * Iterates through all pieces of the king's team and checks if any legal move exists.
     * If no legal move is found, the game is over.
     */
    public boolean isGameOver(Piece king) {
        Piece savedSelection = engine.selectedPiece;
        for (Piece piece: pieceList)
            if(engine.sameTeam(piece, king)) {
                engine.selectedPiece = piece;

                for(int row = 0; row < Variables.rows; row++)
                    for(int col = 0; col < Variables.cols; col++) {
                        Move move = new Move(engine, piece, col, row);
                        if(engine.isValidMove(move)) {
                            engine.selectedPiece = savedSelection;
                            return false;   /// Valid move found — game is not over
                        }
                    }
            }
        engine.selectedPiece = savedSelection;
        return true;    /// No legal moves — game is over (checkmate or stalemate)
    }
}
