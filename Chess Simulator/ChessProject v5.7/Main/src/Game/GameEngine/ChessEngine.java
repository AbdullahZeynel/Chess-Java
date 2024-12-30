package Game.GameEngine;

import GUI.Arrow;
import GUI.Board.Board;
import GUI.Board.ChessClock;
import GUI.Board.PromotionsPanel;
import GUI.Tile;
import Game.Exceptions.InvalidRequestSourceException;
import Game.Exceptions.InvalidTurnException;
import Game.GameEngine.User.User;
import Game.Piece.*;
import resources.Variables;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ChessEngine {

    /// Lists to store highlighted squares, player drawn arrows and active pieces on the board
    protected ArrayList<Piece> pieceList            = new ArrayList<>();
    protected ArrayList<Tile>  highlightedTilesList = new ArrayList<>();
    protected ArrayList<Arrow> arrowList            = new ArrayList<>();
    protected ArrayList<User>  activeUsers          = new ArrayList<>();

    public ChessClock timers;
    public Piece selectedPiece                    = null;
    public Tile selectedTile                      = null;
    public Board board                            = null;
    public CheckScanner checkScanner              = null;
    public PromotionsPanel promotionsPanel        = null;

    protected boolean isFirstGameMove             = true;
    public String gameMode                        = "Standard";

    protected boolean isWhiteToMove                 = true;       /// Game Turn
    protected boolean isGameOver                    = false;      /// Game Condition

    public int enPassantTile                      = -1;         /// The ordered number of the enPassant tile for a position

    protected int halfMoveCounter                   = 0;
    protected int fullMoveCounter                   = 0;



    /// ForDebugging
    public ChessEngine(){
        this.board                                = new Board(this);
        this.checkScanner                         = new CheckScanner(this);

        Variables.fenString = Variables.defaultStartingFenString;   /// Load the default gamestring from the Variables for the ordinary chess setup
        FEN.getPieceList(this);
        FEN.readFenString(Variables.fenString, this);
    }

    public void setChessEngineProperties(User host, User player2){
        activeUsers.add(host);
        activeUsers.add(player2);

        player2.joinGame(this,false);
        host.joinGame(this, true);
    }

    public void setChessEngineProperties(User host){
        activeUsers.add(host);
        host.joinGame(this, true, true);
    }


    /*

    /// Gonna be the main Constructor method
    public ChessEngine(User host, User player2) {
        this.board                                = new Board(this);  //host
        this.checkScanner                         = new CheckScanner(this);

        player2.joinGame(this , false);
        host.joinGame(this, true);

        activeUsers.add(host);
        activeUsers.add(player2);


        Variables.fenString = Variables.defaultStartingFenString;   /// Load the default gamestring from the Variables for the ordinary chess setup
        FEN.getPieceList(this);
        FEN.readFenString(Variables.fenString, this);
    }



    /// Overloading
    public ChessEngine(User host) {
        this.board                                = new Board(this); //host
        this.checkScanner                         = new CheckScanner(this);

        host.joinGame(this, true, true);

        activeUsers.add(host);


        Variables.fenString = Variables.defaultStartingFenString;   /// Load the default gamestring from the Variables for the ordinary chess setup
        FEN.getPieceList(this);
        FEN.readFenString(Variables.fenString, this);
    }
     */

    /// Takes in a col and a row number and scans the pieceList searching for a corresponding piece in the given tile.
    /// Returns either the found piece or null
    public Piece getPiece(int col, int row){
        for (Piece piece: pieceList)
            if(piece.col == col && piece.row == row)
                return piece;
        return null;
    }

    ///  Method overloading for getPiece
    public Piece getPiece(int col, int row, boolean isWhite, String pieceName){
        for(Piece piece: pieceList)
            if(piece.col == col && piece.row == row)
                if(piece.name.equals(pieceName) && piece.isWhite == isWhite)
                    return piece;
        return null;
    }

    /// Encapsulation. If the caller class is permitted it gives permission and runs the make move method.
    public void requestAMove(Class<?> caller, Move move){
            String allowedClass = "GUI.Input";
            if(allowedClass.equals(caller.getName()))
                makeMove(move);
            /// Else throws an Invalıd Request Source Exception.
            else {
                throw new InvalidRequestSourceException("Invalid move request source");
            }
    }

    /// Encapsulation. If the caller class is permitted it gives permission and runs the make move method.
    public void requestAMove(User user, Move move){
        User currentUser = null;
        for(User client: activeUsers){
            if(client == user){
                currentUser = client;
                break;
            }
        }

        if(currentUser == null){
            return;
        }

        if((user.inGameIsWhite == isWhiteToMove)){
            makeMove(move);
        }
        /// Else throws an Invalid Turn exception!
        else {
            throw new InvalidTurnException("Invalid turn exception");
        }
    }

    /// Overloading
    public void requestAMove(User user, String FENString){
        User currentUser = null;
        for(User client: activeUsers){
            if(client == user){
                currentUser = client;
                break;
            }
        }

        if(currentUser == null){
            return;
        }

        if((user.inGameIsWhite == isWhiteToMove)){
            makeMove(FENString);
        }
        /// Else throws an Invalid Turn exception!
        else {
            throw new InvalidTurnException("Invalid turn exception.");
        }
    }

    public void getClocks(ChessClock clocks){
        this.timers = clocks;
        if(this.timers != null);
    }

    /// Overloading
    protected void makeMove(String FENString) {
        FENString = FEN.createFEN(this);
        String Date = GetDateString.returnDateString();
        // file
        TakeGameLogs.takeLogs(FENString, Date);

        System.out.println(FENString);


        loadPositionFromFen(FENString);

        isWhiteToMove = !isWhiteToMove; ///invert the turn

        if(isFirstGameMove)
            this.timers.startWhiteTimer();
        else
            this.timers.switchTimer();

        isFirstGameMove = false;

        updateGameState();
    }

    /// Makes moves. The method responsible of moving, capturing pieces. Can call special sub functions like
    /// pawnMoves() or kingMoves() for special occaitions like enPassant, pawn double push or castle
    protected void makeMove(Move move){
        String FENString = FEN.createFEN(this);
        System.out.println(FENString);

        String Date = GetDateString.returnDateString();
        TakeGameLogs.takeLogs(FENString,Date);

        if(isFirstGameMove)
            this.timers.startWhiteTimer();
        else
            this.timers.switchTimer();

        isFirstGameMove = false;


        if(isWhiteToMove)
            fullMoveCounter++;

        halfMoveCounter++;

        if(move.capturedPiece != null)
            halfMoveCounter = 0;


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
    protected void moveRookInCastle(Move move){
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
    protected void pawnMoves(Move move){
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

    protected void promotePawn(Move move){
        ///Here we must call the Promotion Panel
        ///Then based on the selected piece:
        Character promotionChar = null;
        /// default
        String selectedPromotion = "Queen";  /// setting default to queen
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
                break;
        }
        addPieceToList(promotionChar, move.piece.isWhite, move.newCol, move.newRow);
        capture(move.piece);
    }
    protected void capture(Piece piece){
        pieceList.remove(piece);
    }

    public boolean isValidMove(Move move){
        if(isTimeOver(true)){
            System.out.println("White wins! Black loses out of time.");
            isGameOver = true;

        }
        if(isTimeOver(false)){
            System.out.println("Black wins! White loses out of time.");
            isGameOver = true;
        }

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

    protected boolean checkMovePieceCollision(Move move){
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

    protected Piece createPiece(Character pieceCharacter, int col, int row, boolean isWhite) {
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

    protected void addPieceToList(char pieceChar, int col, int row) {
        Character pieceCharacter = pieceChar;
        boolean isWhite          = getPieceColorBasedOnCase(pieceCharacter);
        Piece piece              = createPiece(pieceCharacter, col, row, isWhite);
        pieceList.add(piece);
    }

    ///Method Overloading. It can either be called with a pre-given isWhite info or
    /// the isWhite status can be determined by the letterCase of the pieceChar
    protected void addPieceToList(char pieceChar, boolean isWhite, int col, int row) {
        Character pieceCharacter = pieceChar;
        Piece piece              = createPiece(pieceCharacter, col, row, isWhite);
        pieceList.add(piece);
    }


    ///We have 6 chars representing the first letters of piece names. Also in the FEN String the case determines
    ///The piece color. Uppercase stands for whitte while lowercase stands for black
    /// This function turns the given char into a Character Wrapper class then calls the
    /// Character.isUpperCase() method and return a boolean value that will be the value of the boolean isWhite
    protected boolean getPieceColorBasedOnCase(Character pieceCharacter){
        return Character.isUpperCase(pieceCharacter);
    }


    //"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR"
    protected void loadPositionFromFen(String fenPosition){
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
    /// Overloading
    public ArrayList invokeIfAllowed(String caller, String keyWord) {
        String allowedClass = "FEN";

        if ((caller.equals(allowedClass))) {
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

    protected ArrayList getPieceList(){
        return pieceList;
    }
    protected ArrayList getArrowList(){
        return arrowList;
    }
    protected ArrayList getTileList (){
        return highlightedTilesList;
    }

    protected void updateGameState() {
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
    protected boolean insufficientMaterial(boolean isWhite){
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

    protected boolean isTimeOver(boolean isWhite){
        if(isWhite)
            return timers.isWhiteTimeOver();
        else
            return timers.isBlackTimeOver();
    }
}


