package main;

import pieces.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Board extends JPanel {

    public int tile_size = 85;

    int rows = 8;
    int cols = 8;

    ArrayList<Piece> pieceList = new ArrayList<>();

    public Piece selectedPiece;

    Input input = new Input(this);

    public int enPassantTile = -1;

    public Board(){
        this.setPreferredSize(new Dimension(cols * tile_size, rows * tile_size));

        addPieces(false);
        addPieces(true);

        this.addMouseListener(input);
        this.addMouseMotionListener(input);
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
        } else {
            move.piece.col = move.newCol;
            move.piece.row = move.newRow;
            move.piece.xPos = move.newCol * tile_size;
            move.piece.yPos = move.newRow * tile_size;

            move.piece.hasMoved = true;
            capture(move.capture);
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

        move.piece.col = move.newCol;
        move.piece.row = move.newRow;
        move.piece.xPos = move.newCol * tile_size;
        move.piece.yPos = move.newRow * tile_size;

        move.piece.hasMoved = true;

        capture(move.capture);
    }

    private void promotePawn(Move move){
        pieceList.add(new Queen(this, move.newCol, move.newRow, move.piece.isWhite));
        capture(move.piece);
    }

    public void capture(Piece piece){
        pieceList.remove(piece);
    }

    public boolean isValidMove(Move move){
        if (sameTeam(move.piece, move.capture)){    //can't capture your own piece
            return false;
        }

        if (!move.piece.isValidMovement(move.newCol, move.newRow)){
            return false;
        }

        if (move.piece.moveCollidesWithPiece(move.newCol, move.newRow)){
            return false;
        }

        return true;
    }

    public boolean sameTeam(Piece p1, Piece p2){
        if (p1 == null || p2 == null) return false;

        return p1.isWhite == p2.isWhite;
    }

    public int getTileNum(int col, int row){
        return row * rows + col;
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

    public void paintComponent(Graphics graphics){  //Setting the board
        Graphics2D g2d = (Graphics2D) graphics;

        for (int c=0; c < cols; c++)        //Setting the tiles
            for (int r=0; r < rows; r++){
                g2d.setColor(((c + r)%2 == 0) ? new Color(240, 217, 181) : new Color(186, 129, 83));
                g2d.fillRect(r * tile_size, c * tile_size, tile_size, tile_size);
                //lichess colors white:F0D9B5FF     black:B58863FF
            }

        //paint highlights
        if (selectedPiece != null)
            for (int r = 0; r < rows; r++)
                for (int c = 0; c < cols; c++)
                    if (isValidMove( new Move(this, selectedPiece, c, r))){

                        g2d.setColor(new Color(68, 180, 57, 190));
                        g2d.fillRect(c * tile_size, r * tile_size, tile_size, tile_size);
                    }


        for (Piece piece: pieceList){   //setting the pieces in pieceList
            piece.paint(g2d);
        }
    }
}