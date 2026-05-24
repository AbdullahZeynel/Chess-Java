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

/**
 * Core chess engine responsible for managing game state, piece movement,
 * move validation, and turn management.
 *
 * This class enforces encapsulation by restricting list access through
 * the invokeIfAllowed() pattern — only approved caller classes can
 * retrieve the internal pieceList, arrowList, and tileList.
 */
public class ChessEngine {

    /// Lists to store highlighted squares, player-drawn arrows, and active pieces on the board
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

    protected boolean isWhiteToMove                 = true;       /// Game turn flag
    public boolean isGameOver                      = false;      /// Game over flag
    public String gameOverMessage                   = null;       /// Stores the result message for UI display

    public int enPassantTile                      = -1;         /// The ordered number of the en passant tile for the current position

    protected int halfMoveCounter                   = 0;
    protected int fullMoveCounter                   = 0;
    protected int timeIncrement                     = 0;          /// Seconds added after each move



    /// Default constructor — sets up the board with the standard starting position
    public ChessEngine(){
        this.board                                = new Board(this);
        this.checkScanner                         = new CheckScanner(this);

        Variables.fenString = Variables.defaultStartingFenString;   /// Load the default FEN string for the standard chess setup
        FEN.getPieceList(this);
        FEN.readFenString(Variables.fenString, this);
    }

    /// Configure game properties for a two-player game
    public void setChessEngineProperties(User host, User player2){
        activeUsers.add(host);
        activeUsers.add(player2);

        player2.joinGame(this,false);
        host.joinGame(this, true);
    }

    /// Configure game properties for a single-player (both colors) game
    public void setChessEngineProperties(User host){
        activeUsers.add(host);
        host.joinGame(this, true, true);
    }

    /// Scans the pieceList for a piece at the given column and row.
    /// Returns the piece if found, otherwise returns null.
    public Piece getPiece(int col, int row){
        for (Piece piece: pieceList)
            if(piece.col == col && piece.row == row)
                return piece;
        return null;
    }

    /// Overloaded getPiece — also validates piece name and color
    public Piece getPiece(int col, int row, boolean isWhite, String pieceName){
        for(Piece piece: pieceList)
            if(piece.col == col && piece.row == row)
                if(piece.name.equals(pieceName) && piece.isWhite == isWhite)
                    return piece;
        return null;
    }

    /// Encapsulation: Only GUI.Input is allowed to request moves through this method.
    /// Any other caller triggers an InvalidRequestSourceException.
    public void requestAMove(Class<?> caller, Move move){
            String allowedClass = "GUI.Input";
            if(allowedClass.equals(caller.getName()))
                makeMove(move);
            else {
                throw new InvalidRequestSourceException("Invalid move request source");
            }
    }

    /// Encapsulation: Only registered users can request moves.
    /// If it's not the user's turn, an InvalidTurnException is thrown.
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
        } else {
            throw new InvalidTurnException("Invalid turn exception");
        }
    }

    /// Overloaded requestAMove — accepts a FEN string instead of a Move object
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
        } else {
            throw new InvalidTurnException("Invalid turn exception.");
        }
    }

    /// Attach the chess clock to the engine
    public void getClocks(ChessClock clocks){
        this.timers = clocks;
        if(this.timers != null);
    }

    /// Make a move using a FEN string — logs the position, updates timers, and refreshes game state
    protected void makeMove(String FENString) {
        FENString = FEN.createFEN(this);
        String Date = GetDateString.returnDateString();
        TakeGameLogs.takeLogs(FENString, Date);


        loadPositionFromFen(FENString);

        isWhiteToMove = !isWhiteToMove; /// Invert the turn

        if(isFirstGameMove)
            this.timers.startWhiteTimer();
        else
            this.timers.switchTimer();

        isFirstGameMove = false;

        updateGameState();
    }

    /// Core move execution method.
    /// Handles piece movement, captures, and delegates to special move handlers
    /// for pawns (en passant, double push, promotion) and kings (castling).
    protected void makeMove(Move move){
        /// Check time before making the move
        checkTimeOver();
        if (isGameOver) return;

        String FENString = FEN.createFEN(this);

        String Date = GetDateString.returnDateString();
        TakeGameLogs.takeLogs(FENString, Date);

        if(isFirstGameMove) {
            this.timers.startWhiteTimer();
        }

        /// Apply time increment to the player who just moved, then switch
        this.timers.addIncrement(isWhiteToMove, timeIncrement);
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
            move.piece.isFirstMove = false;     /// The piece has moved, no longer eligible for first-move privileges

        capture(move.capturedPiece);

        isWhiteToMove = !isWhiteToMove; /// Invert the turn
        updateGameState();
    }

    /// Handles the rook's movement during castling.
    /// If the king moved 2 squares, the corresponding rook is repositioned.
    protected void moveRookInCastle(Move move){
        if(Math.abs(move.piece.col - move.newCol) == 2) {
            Piece rook;
            if (move.piece.col < move.newCol) {
                rook = getPiece(7, move.piece.row);
                rook.col = 5;
            } else{
                rook = getPiece(0, move.piece.row);
                rook.col = 3;
            }
            rook.xPos = rook.col * Variables.tileSize;
        }
    }

    /// Handles special pawn moves: en passant capture, double push, and promotion
    protected void pawnMoves(Move move){
        int colorIndex = move.piece.isWhite ? 1 : -1;   /// Direction offset: +1 for black, -1 for white

        // En passant capture
        if(getTileNum(move.newCol, move.newRow) == enPassantTile){
            move.capturedPiece = getPiece(move.newCol, move.newRow + colorIndex);
        }

        // Double push — set en passant target tile
        if (Math.abs(move.piece.row - move.newRow) == 2){
            enPassantTile = getTileNum(move.newCol, move.newRow + colorIndex);
        } else {
            enPassantTile = -1;
        }

        // Promotion check
        colorIndex = move.piece.isWhite ? 0 : 7;
        if(move.newRow == colorIndex){
            promotePawn(move);
        }
    }

    /// Promotes a pawn to the selected piece (defaults to Queen).
    /// TODO: Integrate with PromotionsPanel for user selection
    protected void promotePawn(Move move){
        Character promotionChar = null;
        String selectedPromotion = "Queen";  /// Default promotion piece
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

    /// Removes a captured piece from the piece list
    protected void capture(Piece piece){
        pieceList.remove(piece);
    }

    /// Validates whether a move is legal.
    /// Checks: game over, turn order, team conflict, piece movement rules,
    /// path collision, and king safety (no moving into check).
    /// Note: Timer checks are done in makeMove(), NOT here — this method is called
    /// thousands of times during game-over scanning and must be fast.
    public boolean isValidMove(Move move){
        /// Check whether the game is over
        if(isGameOver)
            return false;

        /// Check board boundaries — target must be within 0-7
        if(move.newCol < 0 || move.newCol > 7 || move.newRow < 0 || move.newRow > 7)
            return false;

        /// Check turn
        if(move.piece.isWhite != isWhiteToMove)
            return false;

        /// A piece cannot capture its own teammate
        if(sameTeam(move.piece, move.capturedPiece))
            return false;

        /// The move must comply with the piece's movement rules
        if(!move.piece.pieceMoves(move.newCol, move.newRow))
            return false;

        /// Check for path collision (sliding pieces only)
        if(move.piece instanceof Bishop || move.piece instanceof Queen || move.piece instanceof Rook){
            if(checkMovePieceCollision(move))
                return false;
        }

        /// The move must not leave the king in check
        if(checkScanner.isKingChecked(move)){
            move.isKingChecked = true;
            return false;
        }
        return true;
    }

    /// Delegates collision checking to the specific sliding piece type
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

    /// Returns true if both pieces belong to the same team
    public boolean sameTeam(Piece p1, Piece p2){
        if (p1 == null || p2 == null)
            return false;

        return p1.isWhite == p2.isWhite;
    }

    /// Converts a (col, row) pair to a single tile number (0-63)
    public int getTileNum (int col, int row){
        return row * Variables.rows + col;
    }

    /// Searches the piece list for the king of the specified color
    public Piece findKing(boolean isWhite){
        for(Piece piece: pieceList)
            if(isWhite == piece.isWhite && piece instanceof King)
                return piece;

        return null;
    }

    /// Factory method — creates a piece instance based on FEN character
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

    /// Adds a piece to the list — color determined by letter case (uppercase = white)
    protected void addPieceToList(char pieceChar, int col, int row) {
        Character pieceCharacter = pieceChar;
        boolean isWhite          = getPieceColorBasedOnCase(pieceCharacter);
        Piece piece              = createPiece(pieceCharacter, col, row, isWhite);
        pieceList.add(piece);
    }

    /// Overloaded — adds a piece with an explicitly provided color
    protected void addPieceToList(char pieceChar, boolean isWhite, int col, int row) {
        Character pieceCharacter = pieceChar;
        Piece piece              = createPiece(pieceCharacter, col, row, isWhite);
        pieceList.add(piece);
    }

    /// In FEN notation, uppercase = white and lowercase = black.
    /// This method converts the character case to a boolean color flag.
    protected boolean getPieceColorBasedOnCase(Character pieceCharacter){
        return Character.isUpperCase(pieceCharacter);
    }


    /// Parses a FEN position string and populates the piece list accordingly.
    /// Characters: r=rook, n=knight, b=bishop, q=queen, k=king, p=pawn
    /// Digits represent consecutive empty tiles, '/' moves to the next row.
    protected void loadPositionFromFen(String fenPosition){
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
                col ++; /// Move to the next column
            }
        }
    }

    /// Access control mechanism — only approved caller classes can retrieve internal lists.
    /// Allowed callers: GUI.Board.Board, Game.GameEngine.CheckScanner
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

    /// Overloaded access control — accepts a string caller name.
    /// Allowed caller: "FEN"
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

    /// Evaluates the current game state after each move:
    /// checkmate, stalemate, or draw by insufficient material.
    /// Sets isGameOver and gameOverMessage for UI display.
    protected void updateGameState() {
        Piece king = findKing(isWhiteToMove);
        if (checkScanner.isGameOver(king)) {
            String finalFEN = FEN.createFEN(this);
            TakeGameLogs.takeLogs(finalFEN, GetDateString.returnDateString());
            if (checkScanner.isKingChecked((new Move(this, king, king.col, king.row)))) {
                gameOverMessage = isWhiteToMove ? "Black Wins!" : "White Wins!";
            }  else {
                gameOverMessage = "Stalemate!";
            }
            isGameOver = true;
            if (timers != null) timers.setTimers();
            board.repaint();
        } else if (insufficientMaterial(true) && insufficientMaterial(false)) {
            gameOverMessage = "Insufficient Material!";
            isGameOver = true;
            if (timers != null) timers.setTimers();
            board.repaint();
        }
    }

    /// Checks if either player's time has expired — called once per actual move
    protected void checkTimeOver() {
        if (timers == null) return;
        if (timers.isWhiteTimeOver()) {
            gameOverMessage = "Black Wins on Time!";
            isGameOver = true;
            timers.setTimers();
            board.repaint();
        } else if (timers.isBlackTimeOver()) {
            gameOverMessage = "White Wins on Time!";
            isGameOver = true;
            timers.setTimers();
            board.repaint();
        }
    }

    /// Sets the time increment value (seconds added after each move)
    public void setTimeIncrement(int increment) {
        this.timeIncrement = increment;
    }

    /// Clears all highlighted tiles from the board
    public void clearTiles(){
        highlightedTilesList.clear();
    }

    /// Clears all drawn arrows from the board
    public void clearArrows(){
        arrowList.clear();
    }

    /// Checks whether a player has insufficient material to deliver checkmate.
    /// Uses stream filtering and lambda functions to extract piece names by color,
    /// then evaluates whether enough material remains.
    ///
    /// A player needs at least a Queen, Rook, or Pawn to have sufficient material.
    /// King alone or King + one minor piece (Bishop/Knight) is insufficient.
    protected boolean insufficientMaterial(boolean isWhite){
        ArrayList<String> names = pieceList.stream()
                .filter(p -> p.isWhite == isWhite)
                .map(p -> p.name)
                .collect(Collectors.toCollection(ArrayList::new));

        if (names.contains("Queen") || names.contains("Rook") || names.contains("Pawn")) {
            return false;   /// Sufficient material to win
        }

        return names.size() < 3;    /// King alone or King + one minor piece is insufficient
    }

    /// Checks whether a player's clock has run out
    protected boolean isTimeOver(boolean isWhite){
        if(isWhite)
            return timers.isWhiteTimeOver();
        else
            return timers.isBlackTimeOver();
    }
}
