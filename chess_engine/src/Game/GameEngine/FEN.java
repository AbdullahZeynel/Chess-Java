package Game.GameEngine;

import Game.Piece.*;
import resources.Variables;

import java.util.ArrayList;

/**
 * FEN (Forsyth–Edwards Notation) handler.
 * Responsible for reading FEN strings into board state and
 * generating FEN strings from the current board state.
 *
 * FEN Format: "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
 *   - Piece placement (from rank 8 to 1)
 *   - Active color (w/b)
 *   - Castling availability (KQkq or -)
 *   - En passant target square (or -)
 *   - Half-move clock
 *   - Full-move number
 */
public class FEN {

    private static ArrayList<Piece> pieceList;

    /// Loads the piece list from the engine using the access control mechanism
    protected static void getPieceList(ChessEngine engine) {
        pieceList = engine.invokeIfAllowed("FEN", "pieceList");
    }

    /// Generates a complete FEN string from the current board state
    public static String createFEN(ChessEngine engine){
        int emptyTileCount = 0;

        String pieceChar          = "";
        String FENpieces          = "";
        String playerToMove       = "";
        String enPassantTile      = "";
        String castlingCondition  = "";
        String halfMovesFullMoves = "";

        String FENString          = "";

        // Iterate through the board and build the piece placement section
        for (int col = 0; col < 8; col++){
            for (int row = 0; row < 8; row++){
                Piece pieceAtTile = engine.getPiece(row, col);

                if(pieceAtTile == null){
                    emptyTileCount++;
                    continue;

                } else {
                    if (emptyTileCount != 0){
                        FENpieces = FENpieces.concat(String.valueOf(emptyTileCount));
                        emptyTileCount = 0;
                    }
                    pieceChar = pieceAtTile.pieceChar;
                    if(!pieceAtTile.isWhite)
                        pieceChar = pieceChar.toLowerCase();

                    FENpieces = FENpieces.concat(pieceChar);
                }
            }
            if (emptyTileCount != 0){
                FENpieces = FENpieces.concat(String.valueOf(emptyTileCount));
                emptyTileCount = 0;
            }

            if(col != 7)
                FENpieces = FENpieces.concat("/");
            else
                FENpieces = FENpieces.concat(" ");
        }

        // Active color
        if(engine.isWhiteToMove)
            playerToMove = playerToMove.concat("w ");
        else
            playerToMove = playerToMove.concat("b ");

        // Castling rights, en passant, and move counters
        castlingCondition = castlingCondition.concat(rightsToCastle(engine));
        enPassantTile = enPassantTile.concat(getEnPassantTile(engine));

        halfMovesFullMoves = halfMovesFullMoves.concat(getHalfMove(engine));
        halfMovesFullMoves = halfMovesFullMoves.concat(getFullMove(engine));

        FENString = (FENpieces + playerToMove + castlingCondition + enPassantTile + halfMovesFullMoves);

        return FENString;
    }

    /// Determines castling rights for both sides (KQkq format)
    private static String rightsToCastle(ChessEngine engine){
        String rightsToCastle = "";

        Piece bk = engine.getPiece(4, 0, false, "King");
        Piece wk = engine.getPiece(4, 7, true , "King");

        Piece bkr = engine.getPiece(7, 0, false, "Rook");
        Piece bqr = engine.getPiece(0, 0, false, "Rook");
        Piece wkr = engine.getPiece(7, 7, true , "Rook");
        Piece wqr = engine.getPiece(0, 7, true , "Rook");

        if(canCastle(wk, wkr))
            rightsToCastle = rightsToCastle.concat("K");
        else
            rightsToCastle = rightsToCastle.concat("-");

        if(canCastle(wk, wqr))
            rightsToCastle = rightsToCastle.concat("Q");
        else
            rightsToCastle = rightsToCastle.concat("-");

        if(canCastle(bk, bkr))
            rightsToCastle = rightsToCastle.concat("k");
        else
            rightsToCastle = rightsToCastle.concat("-");

        if(canCastle(bk, bqr))
            rightsToCastle = rightsToCastle.concat("q");
        else
            rightsToCastle = rightsToCastle.concat("-");

        return rightsToCastle.concat(" ");
    }

    /// Returns true if both king and rook haven't moved (eligible for castling)
    private static boolean canCastle(Piece king, Piece rook){
        if(king != null && rook != null)
            return (king.isFirstMove && rook.isFirstMove);
        return false;
    }

    /// Returns the en passant target square in algebraic notation, or "-"
    private static String getEnPassantTile(ChessEngine engine){
        if(engine.enPassantTile == -1)
            return "- ";
        else
            return convertTileToNotation(engine);
    }

    /// Converts the internal tile number to algebraic notation (e.g., "e3")
    private static String convertTileToNotation(ChessEngine engine){
        int enPassantTile = engine.enPassantTile;
        String enPassantTileChar = null;
        int enPassantRow = 0;

        // Extract row and column from the tile number
        while(true){
            if(enPassantTile >= 8){
                enPassantTile -= 8;
                enPassantRow++;
                continue;
            } else {
                enPassantRow = Math.abs(8 - enPassantRow);
                break;
            }
        }

        // Map column number to file letter
        switch (enPassantTile){
            case 0: enPassantTileChar = "a"; break;
            case 1: enPassantTileChar = "b"; break;
            case 2: enPassantTileChar = "c"; break;
            case 3: enPassantTileChar = "d"; break;
            case 4: enPassantTileChar = "e"; break;
            case 5: enPassantTileChar = "f"; break;
            case 6: enPassantTileChar = "g"; break;
            case 7: enPassantTileChar = "h"; break;
        }
        return (enPassantTileChar + enPassantRow + " ");
    }

    private static String getHalfMove(ChessEngine engine){
        String halfMove = String.valueOf(engine.halfMoveCounter);
        return halfMove.concat(" ");
    }

    private static String getFullMove(ChessEngine engine){
        String fullMove = String.valueOf(engine.fullMoveCounter);
        return fullMove.concat(" ");
    }


    /// Parses a complete FEN string and applies it to the engine state.
    /// FEN: "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
    protected static void readFenString(String fenString, ChessEngine engine){
        pieceList.clear();
        String[] parts = fenString.split(" ");

        String fenPosition   = parts[0];
        String colorToMove   = parts[1];
        String rightToCastle = parts[2];
        String enPassant     = parts[3];

        loadPositionFromFen(fenPosition, engine);

        /// Active color
        engine.isWhiteToMove = colorToMove.equals("w");

        loadFenCastling(rightToCastle, engine);

        // En passant target square
        if (enPassant.equals("-")) {
            engine.enPassantTile = -1;
        } else {
            // Convert algebraic notation to tile number
            // e.g., e3 -> row = 7 - (3-1) = 5, col = 'e'-'a' = 4 -> tile = 5*8 + 4 = 44
            engine.enPassantTile = (7 - (parts[3].charAt(1) - '1')) * Variables.rows + (parts[3].charAt(0) - 'a');
        }
    }

    /// Applies castling rights from the FEN string to the corresponding pieces
    private static void loadFenCastling(String rightToCastle, ChessEngine engine){
        // bqr = black queen rook, bkr = black king rook
        // wqr = white queen rook, wkr = white king rook

        try{
            Piece bqr = engine.getPiece(0,0);
            Piece wqr = engine.getPiece(0,7);
            Piece bkr = engine.getPiece(7,0);
            Piece wkr = engine.getPiece(7,7);

            if (bqr instanceof Rook){
                bqr.isFirstMove = rightToCastle.contains("q");
            }

            if (bkr instanceof Rook){
                bkr.isFirstMove = rightToCastle.contains("k");
            }

            if (wqr instanceof Rook){
                wqr.isFirstMove = rightToCastle.contains("Q");
            }

            if (wkr instanceof Rook){
                wkr.isFirstMove = rightToCastle.contains("K");
            }

            if (bqr.isFirstMove || bkr.isFirstMove){
                Piece king = engine.findKing(false);
                king.isFirstMove = true;
            }

            if (wqr.isFirstMove || wkr.isFirstMove){
                Piece king = engine.findKing(true);
                king.isFirstMove = true;
            }

        }catch (Exception e){
            System.out.println("Castle pieces at FEN string are null");
        }
    }

    /// Parses the piece placement section of a FEN string and populates the piece list.
    /// Uppercase = white, lowercase = black. Digits = empty squares. '/' = next rank.
    protected static void loadPositionFromFen(String fenPosition, ChessEngine engine){
        int row = 0;
        int col = 0;

        for (int i = 0; i < fenPosition.length(); i++){

            char fenChar = fenPosition.charAt(i);
            if (fenChar == '/') {
                row++;
                col = 0;

            } else if (Character.isDigit(fenChar)) {
                col += Character.getNumericValue(fenChar);

            } else {
                boolean isWhite = Character.isUpperCase(fenChar);
                char pieceChar = Character.toLowerCase(fenChar);

                switch (pieceChar) {
                    case 'r':
                        pieceList.add(new Rook(engine, col, row, isWhite));
                        break;
                    case 'n':
                        pieceList.add(new Knight(engine, col, row, isWhite));
                        break;
                    case 'b':
                        pieceList.add(new Bishop(engine, col, row, isWhite));
                        break;
                    case 'q':
                        pieceList.add(new Queen(engine, col, row, isWhite));
                        break;
                    case 'k':
                        if(engine instanceof ThreeChecksChess)
                            pieceList.add(new ThreeChecksKing(engine, col, row, isWhite));
                        else
                            pieceList.add(new King(engine, col, row, isWhite));
                        break;
                    case 'p':
                        pieceList.add(new Pawn(engine, col, row, isWhite));
                        break;
                }
                col ++;
            }
        }
    }
}
