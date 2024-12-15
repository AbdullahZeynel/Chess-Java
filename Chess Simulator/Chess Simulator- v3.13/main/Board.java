package main;

import pieces.*;
import res.Variables;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Board extends JPanel {

    public String fenStartingPos = Variables.fenString;
    //"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"  --> starting pos

    public int tile_size = 85;

    int rows = 8;
    int cols = 8;

    ArrayList<Piece> pieceList = new ArrayList<>();
    ArrayList<Tile> tileList = new ArrayList<>();
    ArrayList<Arrow> arrowList = new ArrayList<>();


    public Piece selectedPiece;
    public Tile selectedTile;

    private boolean isWhiteToMove = true;
    private boolean isGameOver = false;

    Input input = new Input(this);

    public CheckScanner checkScanner = new CheckScanner(this);

    public int enPassantTile = -1;

    public Board(){
        this.setPreferredSize(new Dimension(cols * tile_size, rows * tile_size));

        loadPositionFromFen(fenStartingPos);

        //addPieces(false);
        //addPieces(true);

        this.addMouseListener(input);
        this.addMouseMotionListener(input);
    }

    public String listPieces() {
        StringBuilder sb = new StringBuilder();
        sb.append("Current pieces on the board:\n");

        for (Piece piece: pieceList) {
            sb.append(piece.name)
                    .append(" at (")
                    .append(piece.col)
                    .append(", ")
                    .append(piece.row)
                    .append(")\n");
        }
        return sb.toString();
    }

    public Piece getPiece(int col, int row){

        for (Piece piece : pieceList){
            if (piece.col == col && piece.row == row){
                return piece;   //If piece in place we click then get piece
            }
        }
        return null; //If we don't find anything
    }

    public void makeMove(Move move){

        if (move.piece.name.equals("Pawn")) {
            movePawn(move);
        } else if (move.piece.name.equals("King")) {
            moveKing(move);
        }

        move.piece.col = move.newCol;
        move.piece.row = move.newRow;
        move.piece.xPos = move.newCol * tile_size;
        move.piece.yPos = move.newRow * tile_size;

        move.piece.isFirstMove = false;

        capture(move.capture);

        isWhiteToMove = !isWhiteToMove;
        updateGameState();
    }

    private void moveKing(Move move){

        if (Math.abs(move.piece.col - move.newCol) == 2) {
            Piece rook;
            if (move.piece.col < move.newCol) {
                rook = getPiece(7, move.piece.row);
                rook.col = 5;
            } else {
                rook = getPiece(0, move.piece.row);
                rook.col = 3;
            }
            rook.xPos = rook.col * tile_size;
        }
    }

    private void movePawn(Move move){
        //en passant
        int colorIndex = move.piece.isWhite ? 1 : -1;

        if(getTileNum(move.newCol, move.newRow) == enPassantTile){
            move.capture = getPiece(move.newCol, move.newRow + colorIndex);
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
        pieceList.add(new Queen(this, move.newCol, move.newRow, move.piece.isWhite));
        capture(move.piece);
    }

    public void capture(Piece piece){
        pieceList.remove(piece);
    }

    public boolean isValidMove(Move move){
        //System.out.println("col: " + move.newCol + ", row: " + move.newRow);
        //DEBUGGING
        //checkScanner.printPieces();

        //check if game is over
        if(isGameOver)
            return false;




        //check turn
        if (move.piece.isWhite != isWhiteToMove)
            return false;


        //System.out.println("entering sameteam");
        if (sameTeam(move.piece, move.capture)){    //can't capture your own piece
            //System.out.println("same team");
            //sameTeamDebugg(move.piece, move.capture);
            return false;
        }

        if (!move.piece.isValidMovement(move.newCol, move.newRow)){
            return false;
        }

        if (move.piece.moveCollidesWithPiece(move.newCol, move.newRow)){
            return false;
        }

        if (checkScanner.isKingChecked(move)) {
            move.isKingChecked = true;
            return false;
        }

        return true;
    }

    /*
        public boolean isValidMove(Move move){
        System.out.println("col: " + move.newCol + ", row: " + move.newRow);
        //DEBUGGING
        //checkScanner.printPieces();

        //check if game is over
        if(isGameOver)
            return false;

        //check turn
        if (move.piece.isWhite != isWhiteToMove)
            return false;

        if (sameTeam(move.piece, move.capture)){    //can't capture your own piece
            return false;
        }

        if (!move.piece.isValidMovement(move.newCol, move.newRow)){
            return false;
        }

        if (move.piece.moveCollidesWithPiece(move.newCol, move.newRow)){
            return false;
        }

        if (checkScanner.isKingChecked(move)) {
            move.isKingChecked = true;
            return false;
        }

        return true;
    }
     */

    public boolean sameTeam(Piece p1, Piece p2){
        if (p1 == null || p2 == null) return false;

        return p1.isWhite == p2.isWhite;
    }

/*
    public boolean sameTeamDebugg(Piece p1, Piece p2){
        //if (p1 == null || p2 == null) return false;

        if (p1 == null) {
            System.out.println("p1 is null");
            if (p2 == null) {
                System.out.println("p2 is null");
                return false;
            }
        }

        System.out.println("p1 and p2 are not null");
        System.out.println("p1: " + p1.name + " " + p1.isWhite);
        System.out.println("p2: " + p2.name + " " + p2.isWhite);
        return p1.isWhite == p2.isWhite;
    }
 */

    public int getTileNum(int col, int row){
        return row * rows + col;
    }

    //checks and piece binding
    public Piece findKing(boolean isWhite) {       //Finding the king
        for (Piece piece : pieceList) {
            if (isWhite == piece.isWhite && piece.name.equals("King")) {
                return piece;
            }
        }
        //System.out.println("[DEBUGGING]");
        //System.out.println("King not found for color: " + (isWhite ? "White" : "Black"));
        return null;
    }

    public void addPieces(boolean isWhite){
        int row = isWhite ? 7 : 0;
        pieceList.add(new Rook(this, 0, row, isWhite));
        pieceList.add(new Knight(this, 1, row, isWhite));
        pieceList.add(new Bishop(this, 2, row, isWhite));

        pieceList.add(new Queen(this, 3, row, isWhite));
        pieceList.add(new King(this, 4, row, isWhite));

        pieceList.add(new Bishop(this, 5, row, isWhite));
        pieceList.add(new Knight(this, 6, row, isWhite));
        pieceList.add(new Rook(this, 7, row, isWhite));

        for (int c = 0; c < 8; c++){
            row = isWhite ? 6 : 1;
            pieceList.add(new Pawn(this, c, row, isWhite));
        }

    }

    //"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR" "w" "KQkq" "-" "0 1"
    public void loadPositionFromFen(String fenString){
        pieceList.clear();
        String[] parts = fenString.split(" ");

        //set up pieces
        String position = parts[0];
        int row=0;
        int col=0;

        for (int i = 0; i < position.length(); i++){
            char c = position.charAt(i);
            if (c == '/') {
                row++;
                col = 0;
            } else if (Character.isDigit(c)) {
                col += Character.getNumericValue(c);
            } else {
                boolean isWhite = Character.isUpperCase(c);
                char pieceChar = Character.toLowerCase(c);

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
                col ++;
            }
        }

        // color to move
        isWhiteToMove = parts[1].equals("w");

        //castling
        Piece bqr = getPiece(0, 0);
        if (bqr instanceof Rook){
            bqr.isFirstMove = parts[2].contains("q");           //if contains q then it can castles so it hasn't moved so !
        }

        Piece bkr = getPiece(7, 0);
        if (bkr instanceof Rook){
            bkr.isFirstMove = parts[2].contains("k");
        }

        Piece wqr = getPiece(0, 7);
        if (wqr instanceof Rook){
            wqr.isFirstMove = parts[2].contains("Q");
        }

        Piece wkr = getPiece(7, 7);
        if (wkr instanceof Rook){
            wkr.isFirstMove = parts[2].contains("K");
        }

        //en passant square
        if (parts[3].equals("-")) {
            enPassantTile = -1;
        } else {
            //row * rows + col
            //for example e3 -> 3 - 1 (zero based) -> 7 - 2 (inverting) -> 8 * 5 -> 'e' - 'a' = 4 -> 44 (zero based 0-63)
            enPassantTile = (7 - (parts[3].charAt(1) - '1')) * rows + (parts[3].charAt(0) - 'a');
        }
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

    private boolean insufficientMaterial(boolean isWhite){
        ArrayList<String> names = pieceList.stream()
                .filter(p -> p.isWhite == isWhite)
                .map(p -> p.name)
                .collect(Collectors.toCollection(ArrayList::new));

        if (names.contains("Queen") || names.contains("Rook") || names.contains("Pawn")) {
            return false;
        }
        return names.size() < 3;
    }

    public void paintTile (Graphics2D g2d, int col, int row, boolean shape, boolean tileOrCapture){
        if (tileOrCapture) {    //if painting a tile move
            g2d.setColor(((col + row)%2 == 0) ? new Color(240, 217, 181) : new Color(69, 69, 69, 255));

            if (shape) {        //false == oval , true == rect
                g2d.fillOval(col * tile_size, row * tile_size, tile_size, tile_size);
            } else {
                g2d.fillRect(row * tile_size, col * tile_size, tile_size, tile_size);
            }
        } else {    //capture move
            g2d.setColor(new Color(140, 13, 15, 208));
            g2d.fill3DRect(col * tile_size, row * tile_size, tile_size, tile_size, true);
        }

    }


    public void paintComponent(Graphics graphics){  //Setting the board
        Graphics2D g2d = (Graphics2D) graphics;

        for (int c=0; c < cols; c++)        //Setting the tiles
            for (int r=0; r < rows; r++){
                paintTile(g2d, c, r, false, true);
                //lichess colors white:F0D9B5FF     black:B58863FF
            }

        for (Tile tile: tileList){
            tile.paint(g2d, this);
            g2d.setColor(((tile.col + tile.row)%2 == 0) ? new Color(240, 217, 181) : new Color(69, 69, 69, 255));

            g2d.fillOval(tile.col * tile_size + 5, tile.row * tile_size + 5, tile_size -10, tile_size -10);
        }

        //paint highlights
        if (selectedPiece != null)
            for (int r = 0; r < rows; r++)
                for (int c = 0; c < cols; c++)
                    if (isValidMove( new Move(this, selectedPiece, c, r))){

                        //green color for the valid moves
                        if (getPiece(c,r) != null) {
                            //if there is a piece at the tile (if sameteam then it's not a valid move so no need to check teams
                            //so if there is a piece and it is a valid move then it is a captur move

                            g2d.setColor(new Color(78, 115, 78, 194));      //capture color
                            g2d.fill3DRect(c * tile_size, r * tile_size, tile_size + 1, tile_size + 1, true);

                            paintTile(g2d, c, r, true, true); //erase a circle to get the desired shape
                        } else {
                            g2d.setColor(new Color(79, 117, 79, 194));    //valid move color

                            // Calculate the radius and diameter of the circle
                            int radius = tile_size / 4;

                            // Calculate the center of the tile
                            int centerX = (c * tile_size + tile_size / 3) + 4;
                            int centerY = (r * tile_size + tile_size / 3) + 4;

                            // Draw the circle
                            g2d.fillOval(centerX, centerY, radius, radius);
                        }
                    }

        for (Piece piece: pieceList){   //setting the pieces in pieceList
            if (piece.name.equals("King")) {
                Piece king = findKing(piece.isWhite);
                if (checkScanner.isKingChecked((new Move(this, king, king.col, king.row)))){
                    paintTile(g2d, king.col, king.row, true, false);
                }
            }
            piece.paint(g2d);
        }

        for (Arrow arrow: arrowList) {
            //draw the straight arrow
            arrow.drawArrow(g2d);
        }
    }

    public void paintPlanning(Tile t, Arrow arr){
        Tile tile = new Tile(t.col, t.row);
        tileList.add(tile);

        if (arr != null){
            Arrow arrow = new Arrow(arr.startPoint, arr.endPoint);
            arrowList.add(arrow);
        }

        repaint();
    }

    public void clearTiles(){
        tileList.clear();
    }

    public void clearArrows(){
        arrowList.clear();
    }

    /*
            Graphics2D g2d = (Graphics2D) graphics;

        g2d.setColor(new Color(33, 78, 33, 194));

        // Calculate the radius and diameter of the circle
        int radius = tile_size / 2;

        // Calculate the center of the tile
        int centerX = (col * tile_size + tile_size / 3) + 4;
        int centerY = (row * tile_size + tile_size / 3) + 4;

        // Draw the circle
        g2d.fillOval(centerX, centerY, radius, radius);
     */
}