package Game.GameEngine;

import GUI.Arrow;
import GUI.Board.Board;
import GUI.Board.PromotionsPanel;
import GUI.Tile;
import Game.Piece.*;
import resources.Variables;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ChessEngine {

    /// Lists to store highlighted squares, player drawn arrows and active pieces on the board
    private ArrayList<Piece> pieceList            = new ArrayList<>();
    private ArrayList<Tile>  highlightedTilesList = new ArrayList<>();
    private ArrayList<Arrow> arrowList            = new ArrayList<>();

    public Piece selectedPiece                    = null;
    public Tile selectedTile                      = null;
    public Board board                            = null;
    public CheckScanner checkScanner              = null;
    public PromotionsPanel promotionsPanel        = null;

    private boolean isWhiteToMove                 = true;       /// Game Turn
    private boolean isGameOver                    = false;      /// Game Condition

    public int enPassantTile                      = -1;         /// The ordered number of the enPassant tile for a position

    public ChessEngine(){
        this.board                                = new Board(this);
        this.checkScanner                         = new CheckScanner(this);

        Variables.fenString = Variables.defaultStartingFenString;   /// Load the default gamestring from the Variables for the ordinary chess setup
        readFenString(Variables.fenString);
    }

    /// Takes in a col and a row number and scans the pieceList searching for a corresponding piece in the given tile.
    /// Returns either the found piece or null
    public Piece getPiece(int col, int row){
        for (Piece piece: pieceList)
            if(piece.col == col && piece.row == row)
                return piece;
        return null;
    }

    /// Is there a checkmate or stalemate or is the game already over due to insufficient materials?
    public boolean isGameOver(Piece king) {
        for (Piece piece: pieceList)
            if (sameTeam(piece, king)) {
                selectedPiece = (piece == king) ? king : null;

                /// Scans for valid moves for every piece. If the player has no valid moves then the game is over.
                for (int row = 0; row < Variables.rows; row++)
                    for (int col = 0; col < Variables.cols; col++) {
                        Move move = new Move(this, piece, col, row);
                        if (isValidMove(move)) {      //valid move found game is not over
                            return false;
                        }
                    }
            }
        return true;    //no move found game over
    }

    /// Encapsulation. If the caller class is permitted it gives permission and runs the make move method.
    public void requestAMove(Class<?> caller, Move move){
            String allowedClass = "GUI.Input";
            if(allowedClass.equals(caller.getName()))
                makeMove(move);
            /// Else throw exception!
    }

    /// Makes moves. The method responsible of moving, capturing pieces. Can call special sub functions like
    /// pawnMoves() or kingMoves() for special occaitions like enPassant, pawn double push or castle
    private void makeMove(Move move){
        if(move.piece instanceof Pawn){
            pawnMoves(move);
        } else if (move.piece instanceof King){
            moveRookInCastle(move);
        }

        move.piece.col = move.newCol;   /// Assign the new coordinates
        move.piece.row = move.newRow;
        move.piece.xPos = move.newCol * Variables.tileSize;     /// Assign the new display coordinates
        move.piece.yPos = move.newRow * Variables.tileSize;

        if(move.piece.isFirstMove)
            move.piece.isFirstMove = false;     ///Piece moved lol :D
            /// If it's the piece's first move then turn it to false cause the piece has moved

        capture(move.capturedPiece);
        ///Inspect capture

        isWhiteToMove = !isWhiteToMove; ///invert the turn
        updateGameState();
    }

    /// Makes the rook part of castle
    private void moveRookInCastle(Move move){
        ///If the newCol and newRow are on the castling tiles then run the if statement
        if(Math.abs(move.piece.col - move.newCol) == 2) {
            Piece rook;
            if (move.piece.col < move.newCol) {
                rook = getPiece(7, move.piece.row);
                rook.col = 5;
            } else{
                rook = getPiece(0, move.piece.row);
                rook.col = 3;
            }
            rook.xPos = rook.col * Variables.tileSize;  /// Rook is moved to the relative castle location
        }
    }

    /// Special makeMove method to handle pawn moves
    private void pawnMoves(Move move){
        //en passant
        int colorIndex = move.piece.isWhite ? 1 : -1;   ///When the black pawn moves +1 when the white moves -1

        if(getTileNum(move.newCol, move.newRow) == enPassantTile){
            move.capturedPiece = getPiece(move.newCol, move.newRow + colorIndex);
        }

        if (Math.abs(move.piece.row - move.newRow) == 2){
            enPassantTile = getTileNum(move.newCol, move.newRow +   colorIndex);
        } else {
            enPassantTile = -1;
        }

        //Promotions
        colorIndex = move.piece.isWhite ? 0 : 7;

        if(move.newRow == colorIndex){
            promotePawn(move);
        }
    }

    private void promotePawn(Move move){
        ///Here we must call the Promotion Panel
        ///Then based on the selected piece:
        Character promotionChar = null;
        String selectedPromotion = null;        //Will be assigned a value based on the chosen button by the user while doing the promotion

        switch(selectedPromotion){
            case "Queen":
                promotionChar = 'q';
                break;
            case "Rook":
                promotionChar = 'r';
                break;
            case "Bishop":
                promotionChar = 'b';
                break;
            case "Knight":
                promotionChar = 'n';
                break;
            default:
                ///No need to insert a default statement because the input is coming from the menu not directly from the user
                break;
        }

        addPieceToList(promotionChar, move.piece.isWhite, move.newCol, move.newRow);
        capture(move.piece);
    }

    private void capture(Piece piece){
        pieceList.remove(piece);
    }

    public boolean isValidMove(Move move){

        ///Check wether the game is over
        if(isGameOver)
            return false;

        ///Check turn
        if(move.piece.isWhite != isWhiteToMove)
            return false;

        ///Check teams.
        ///A piece can't capture it's teammate peace
        if(sameTeam(move.piece, move.capturedPiece))
            return false;

        ///The move must be correct according to the respective piece's movement rules
        if(!move.piece.pieceMoves(move.newCol, move.newRow))
            return false;

        ///If move collides then returns false
        if(move.piece instanceof Bishop || move.piece instanceof Queen || move.piece instanceof Rook){
            if(checkMovePieceCollision(move))
                return false;
        }

        ///if there is a check and the




        if(checkScanner.isKingChecked(move)){
            move.isKingChecked = true;
            return false;

        }



        return true;
    }

    private boolean checkMovePieceCollision(Move move){
        if(move.piece instanceof Bishop){
            Bishop bishop = (Bishop) move.piece;
            return bishop.moveCollidesWithPiece(move.newCol, move.newRow);

        } else if(move.piece instanceof Rook){
            Rook   rook = (Rook) move.piece;
            return rook.moveCollidesWithPiece(move.newCol, move.newRow);

        } else{
            Queen  queen = (Queen) move.piece;
            return queen.moveCollidesWithPiece(move.newCol, move.newRow);
        }
    }

    public boolean sameTeam(Piece p1, Piece p2){
        ///Compare the teams of the given two pieces. If they are from the same team
        ///return true, else return false
        if (p1 == null || p2 == null)
            return false;

        return p1.isWhite == p2.isWhite;
    }

    public int getTileNum (int col, int row){
        return row * Variables.rows + col;      ///returns the number of the relative tile
        ///I may move this to the tile class later on
    }

    public Piece findKing(boolean isWhite){     /// Find the king piece and send it from the pieceList
        for(Piece piece: pieceList)
            if(isWhite == piece.isWhite && piece instanceof King)
                return piece;

        return null;
    }

    private Piece createPiece(Character pieceCharacter, int col, int row, boolean isWhite) {
        switch (Character.toLowerCase(pieceCharacter)) {
            case 'r': return new Rook   (this, col, row, isWhite);
            case 'n': return new Knight (this, col, row, isWhite);
            case 'b': return new Bishop (this, col, row, isWhite);
            case 'q': return new Queen  (this, col, row, isWhite);
            case 'k': return new King   (this, col, row, isWhite);
            case 'p': return new Pawn   (this, col, row, isWhite);
            default:
                throw new IllegalArgumentException("Invalid piece name: " + pieceCharacter);
        }
    }

    private void addPieceToList(char pieceChar, int col, int row) {
        Character pieceCharacter = pieceChar;
        boolean isWhite          = getPieceColorBasedOnCase(pieceCharacter);
        Piece piece              = createPiece(pieceCharacter, col, row, isWhite);
        pieceList.add(piece);
    }

    ///Method Overloading. It can either be called with a pre-given isWhite info or
    /// the isWhite status can be determined by the letterCase of the pieceChar
    private void addPieceToList(char pieceChar, boolean isWhite, int col, int row) {
        Character pieceCharacter = pieceChar;
        Piece piece              = createPiece(pieceCharacter, col, row, isWhite);
        pieceList.add(piece);
    }


    ///We have 6 chars representing the first letters of piece names. Also in the FEN String the case determines
    ///The piece color. Uppercase stands for whitte while lowercase stands for black
    /// This function turns the given char into a Character Wrapper class then calls the
    /// Character.isUpperCase() method and return a boolean value that will be the value of the boolean isWhite
    private boolean getPieceColorBasedOnCase(Character pieceCharacter){
        return Character.isUpperCase(pieceCharacter);
    }


    //"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR"
    private void loadPositionFromFen(String fenPosition){
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
                        pieceList.add(new Rook(this, col, row, isWhite));
                        break;
                    case 'n':
                        pieceList.add(new Knight(this, col, row, isWhite));
                        break;
                    case 'b':
                        pieceList.add(new Bishop(this, col, row, isWhite));
                        break;
                    case 'q':
                        pieceList.add(new Queen(this, col, row, isWhite));
                        break;
                    case 'k':
                        pieceList.add(new King(this, col, row, isWhite));
                        break;
                    case 'p':
                        pieceList.add(new Pawn(this, col, row, isWhite));
                        break;

                }
                col ++; ///move to the next column
            }
        }
    }



/*
    public void validateMoveWithFen(String fenString, Move move) throws FenMoveErrorException {
        // Example condition for mismatch
        if (!isMoveValidWithFen(fenString, move)) {
            throw new MoveLogicFenMismatchException(
                    "Move logic and FEN string mismatch detected! FEN: " + fenString + ", Move: " + move
            );
        }

 */

    ///castling
    private void loadFenCastling(String rightToCastle){
        ///bqr -> black queen rook, bkr -> black king rook
        ///wqr -> white queen rook, wkr -> white king rook

        try{
            Piece bqr = getPiece(0,0);
            Piece wqr = getPiece(0,7);
            Piece bkr = getPiece(7,0);
            Piece wkr = getPiece(7,7);

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
                Piece king = findKing(false);
                king.isFirstMove = true;
            }

            if (wqr.isFirstMove || wkr.isFirstMove){
                Piece king = findKing(true);
                king.isFirstMove = true;
            }

        }catch (Exception e){
            System.out.println("Castle pieces at FEN string are null");
        }
        //throw new IllegalArgumentException("Invalid piece name: " + pieceCharacter);
    }


    //"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR" "w" "KQkq" "-" "0 1"
    private void readFenString(String fenString){
        pieceList.clear();
        String[] parts = fenString.split(" ");

        String fenPosittion  = parts[0];
        String colorToMove   = parts[1];
        String rightToCastle = parts[2];
        String enPassant = parts[3];

        loadPositionFromFen(fenPosittion);

        /// color to move
        isWhiteToMove = colorToMove.equals("w");

        loadFenCastling(rightToCastle);

        //en passant square
        if (enPassant.equals("-")) {
            enPassantTile = -1;
        } else {
            //row * rows + col
            //for example e3 -> 3 - 1 (zero based) -> 7 - 2 (inverting) -> 8 * 5 -> 'e' - 'a' = 4 -> 44 (zero based 0-63)
            enPassantTile = (7 - (parts[3].charAt(1) - '1')) * Variables.rows + (parts[3].charAt(0) - 'a');
        }

    }

    public ArrayList invokeIfAllowed(Class<?> caller, String keyWord) {
        String allowedClass1 = "GUI.Board.Board";
        String allowedClass2 = "Game.GameEngine.CheckScanner";

        if ((allowedClass1.equals(caller.getName())||(allowedClass2.equals(caller.getName())))) {
            switch (keyWord){
                case "pieceList":
                    return getPieceList();
                case "arrowList":
                    return getArrowList();
                case "tileList":
                    return getTileList();
            }
        } else {
            throw new SecurityException("Access denied!");
        }
        return null;
    }

    private ArrayList getPieceList(){
        return pieceList;
    }
    private ArrayList getArrowList(){
        return arrowList;
    }
    private ArrayList getTileList (){
        return highlightedTilesList;
    }





    private void updateGameState() {

        Piece king = findKing(isWhiteToMove);
        if (checkScanner.isGameOver(king)) {
            if (checkScanner.isKingChecked((new Move(this, king, king.col, king.row)))) { //game over
                System.out.println(isWhiteToMove ? "Black Wins!" : "White Wins!");
            }  else {    //stale-mate
                System.out.println("Stale Mate!");
            }
        } else if (insufficientMaterial(true) && insufficientMaterial(false)) {
            //game over due to insufficient materials
            System.out.println("Insufficient Materials! (Draw)");
            isGameOver = true;
        }

    }

    public void clearTiles(){
        highlightedTilesList.clear();
    }

    public void clearArrows(){
        arrowList.clear();
    }


/*
*   Using stream() and lambda functions we're creating a new ArrayList this time only consisting of
*   the piece.name values of the given .isWhite peices.
*
*   inside the .filter() and the .map() methods we're using labda functions
*   the p -> p.isWhite == isWhite is eliminating the pieces where their .isWhite is not equal to the provided isWhite argument
*   and the p -> p.name is taking only the .name variables of the pieces in pieceList
*   finally with the .collect we're collecting the new list to a new ArrayList and assigning it to the names ArrayList
 */

    /*
    for(Piece piece: pieceList)
        if(piece.isWhite == isWhite)
            return piece

    for(Piece peice: newPieceList)
        return piece.name;
     */
    private boolean insufficientMaterial(boolean isWhite){
        ArrayList<String> names = pieceList.stream()
                .filter(p -> p.isWhite == isWhite)
                .map(p -> p.name)
                .collect(Collectors.toCollection(ArrayList::new));

        if (names.contains("Queen") || names.contains("Rook") || names.contains("Pawn")) {
            return false;   /// Sufficient materials to win the game
        }
        
        return names.size() < 3;    /// The King on it's one is not sufficient to win the game
        /// The same goes for Bishop and Knight there must be at least three pieces for the game to be won
    }

}


