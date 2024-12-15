package main;

import pieces.Arrow;
import pieces.Piece;
import pieces.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Input extends MouseAdapter {

    Board board;
    Graphics g;
    Tile tile = null;

    int col;
    int row;

    Point startPoint = null;
    Point endPoint = null;

    Arrow arrow = null;

    public Input(Board board) {
        this.board = board;
    }

    @Override
    public void mousePressed(MouseEvent e) {

        col = e.getX() / board.tile_size;
        row = e.getY() / board.tile_size;

        if (SwingUtilities.isRightMouseButton(e)){
            handleRightClick(e);
        } else if (SwingUtilities.isLeftMouseButton(e)){
            //clear strategy ovals
            board.clearTiles();
            board.clearArrows();

            // delete and draw the selected piece here

            Piece pieceXY = board.getPiece(col, row);
            if (pieceXY != null) {      //If there is a piece there then set piece
                board.selectedPiece = pieceXY;
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)){
            handleRightDragg(e);
        } else if (SwingUtilities.isLeftMouseButton(e)){
            if (board.selectedPiece != null) {
                board.selectedPiece.xPos = e.getX() - board.tile_size / 2;
                board.selectedPiece.yPos = e.getY() - board.tile_size / 2;

                board.repaint();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if (SwingUtilities.isRightMouseButton(e)){
            handleRightRelease(e);
        } else if (SwingUtilities.isLeftMouseButton(e)){

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
        }

        board.selectedPiece = null;
        board.repaint();
    }

    private void handleRightClick(MouseEvent e) {
        arrow = null;
        startPoint = e.getPoint();  //record the start point
        endPoint = e.getPoint();


        if (tile == null) {
            tile = new Tile(col, row); // Initialize tile if it is null
        }

        tile.col = col;
        tile.row = row;

    }

    private void handleRightRelease(MouseEvent e) {
        board.paintPlanning(tile, arrow);
    }

    private void handleRightDragg(MouseEvent e) {
        if (arrow == null) {
            arrow = new Arrow(startPoint, endPoint);
        }
        arrow.startPoint = startPoint;
        arrow.endPoint = e.getPoint();        //keep updating the end point
        board.repaint();
    }
}
