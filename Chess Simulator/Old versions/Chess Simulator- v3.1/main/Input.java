package main;

import pieces.Piece;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Input extends MouseAdapter {

    Board board;

    public Input(Board board) {
        this.board = board;
    }

    @Override
    public void mousePressed(MouseEvent e) {

        int col = e.getX() / board.tile_size;
        int row = e.getY() / board.tile_size;

        Piece pieceXY = board.getPiece(col, row);
        if (pieceXY != null) {      //If there is a piece there then set piece
            board.selectedPiece = pieceXY;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

        if (board.selectedPiece != null) {
            board.selectedPiece.xPos = e.getX() - board.tile_size / 2;
            board.selectedPiece.yPos = e.getY() - board.tile_size / 2;

            board.repaint();
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {

        int col = e.getX() / board.tile_size;
        int row = e.getY() / board.tile_size;

        if (board.selectedPiece != null) {
            Move move = new Move(board, board.selectedPiece, col, row);

            if(board.isValidMove(move)){
                board.makeMove(move);
            } else {        //return the piece to it's place
                board.selectedPiece.xPos = board.selectedPiece.col * board.tile_size;
                board.selectedPiece.yPos = board.selectedPiece.row * board.tile_size;
            }
        }

        board.selectedPiece = null;
        board.repaint();
    }



}
