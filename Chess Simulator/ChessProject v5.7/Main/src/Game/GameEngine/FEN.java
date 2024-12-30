package Game.GameEngine;

import Game.Piece.*;
import resources.Variables;

import java.util.ArrayList;

public class FEN {

    private static ArrayList<Piece> pieceList; ///Importing the pieceList from the ChessEngine

    protected static void getPieceList(ChessEngine engine) {
        /// Because the CheckScanner class is included inside the engine.invokeIfAllowed() function
        /// We can load the private ArrayList pieceList successfully!
        pieceList = engine.invokeIfAllowed("FEN", "pieceList");
    }

    public static String createFEN(ChessEngine engine){
        int emptyTileCount = 0;

        String pieceChar          = "";
        String FENpieces          = "";
        String playerToMove       = "";
        String enPassantTile      = "";
        String castlingCondition  = "";
        String halfMovesFullMoves = "";

        String FENString          = "";


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

        if(engine.isWhiteToMove)
            playerToMove = playerToMove.concat("w ");
        else
            playerToMove = playerToMove.concat("b ");


        castlingCondition = castlingCondition.concat(rightsToCastle(engine));
        enPassantTile = enPassantTile.concat(getEnPassantTile(engine));

        halfMovesFullMoves = halfMovesFullMoves.concat(getHalfMove(engine));
        halfMovesFullMoves = halfMovesFullMoves.concat(getFullMove(engine));

        FENString = (FENpieces + playerToMove + castlingCondition + enPassantTile + halfMovesFullMoves);

        return FENString;
    }

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

    private static boolean canCastle(Piece king, Piece rook){
        if(king != null && rook != null)
            return (king.isFirstMove && rook.isFirstMove);
        return false;
    }

    private static String getEnPassantTile(ChessEngine engine){
        if(engine.enPassantTile == -1)
            return "- ";

        else
            return convertTileToNotation(engine);
    }

    private static String convertTileToNotation(ChessEngine engine){
        int enPassantTile = engine.enPassantTile;
        String enPassantTileChar = null;
        int enPassantRow = 0;

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

        switch (enPassantTile){
            case 0:
                enPassantTileChar = "a";
                break;
            case 1:
                enPassantTileChar = "b";
                break;
            case 2:
                enPassantTileChar = "c";
                break;
            case 3:
                enPassantTileChar = "d";
                break;
            case 4:
                enPassantTileChar = "e";
                break;
            case 5:
                enPassantTileChar = "f";
                break;
            case 6:
                enPassantTileChar = "g";
                break;
            case 7:
                enPassantTileChar = "h";
                break;
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


    //"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR" "w" "KQkq" "-" "0 1"
    protected static void readFenString(String fenString, ChessEngine engine){
        pieceList.clear();
        String[] parts = fenString.split(" ");

        String fenPosittion  = parts[0];
        String colorToMove   = parts[1];
        String rightToCastle = parts[2];
        String enPassant = parts[3];

        loadPositionFromFen(fenPosittion, engine);

        /// color to move
        engine.isWhiteToMove = colorToMove.equals("w");

        loadFenCastling(rightToCastle, engine);

        //en passant square
        if (enPassant.equals("-")) {
            engine.enPassantTile = -1;
        } else {
            //row * rows + col
            //for example e3 -> 3 - 1 (zero based) -> 7 - 2 (inverting) -> 8 * 5 -> 'e' - 'a' = 4 -> 44 (zero based 0-63)
            engine.enPassantTile = (7 - (parts[3].charAt(1) - '1')) * Variables.rows + (parts[3].charAt(0) - 'a');
        }
    }

    ///castling
    private static void loadFenCastling(String rightToCastle, ChessEngine engine){
        ///bqr -> black queen rook, bkr -> black king rook
        ///wqr -> white queen rook, wkr -> white king rook

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
        //throw new IllegalArgumentException("Invalid piece name: " + pieceCharacter);
    }

    //"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR"
    protected static void loadPositionFromFen(String fenPosition, ChessEngine engine){
        int row = 0;
        int col = 0;

        ///A for loop to loop through the FenString
        for (int i = 0; i < fenPosition.length(); i++){

            ///The fenChar is the iterating variable here. In case of '/' then
            ///move to the next row. set the row to row++ and col to col=0
            char fenChar = fenPosition.charAt(i);
            if (fenChar == '/') {
                row++;
                col = 0;

                ///The digits in FenStrings stand for the empty tiles. Iterate forward from the empty tiles with col+=Cahracter.getNumericValue(fenChar)
                ///or just simply by col += (the encountered digit)
            } else if (Character.isDigit(fenChar)) {
                col += Character.getNumericValue(fenChar);

                ///If the character is not a '/' nor a digit then it must be a letter
                ///The uppercase letters stand for white while the lowercase letters stand for the black pieces
                ///respectivly the p is pawn, k is king, n is knight, b is bishop and q is queen
                ///if the character is one of the pieces then add the piece in the given row and col place
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
                        if(engine.gameMode.equals("ThreeChecksKing"))
                            pieceList.add(new King(engine, col, row, isWhite));
                        else
                            pieceList.add(new King(engine, col, row, isWhite));
                        break;
                    case 'p':
                        pieceList.add(new Pawn(engine, col, row, isWhite));
                        break;
                }
                col ++; ///move to the next column
            }
        }
    }
}
