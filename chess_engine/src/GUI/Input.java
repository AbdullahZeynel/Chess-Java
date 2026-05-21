package GUI;

import GUI.Board.Board;
import Game.GameEngine.ChessEngine;
import Game.GameEngine.Move;
import Game.Piece.Piece;
import resources.Variables;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Handles all mouse input on the chess board.
 *
 * Left click: Select and drag pieces, make moves
 * Right click: Highlight squares and draw analysis arrows
 */
public class Input extends MouseAdapter {
    ChessEngine engine;
    Graphics g;
    Tile tile = null;

    int col;
    int row;

    Point startPoint = null;
    Point endPoint = null;

    Arrow arrow = null;

    public Input(ChessEngine engine) {
        this.engine = engine;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        /// Get the board coordinates of the click
        col = e.getX() / Variables.tileSize;
        row = e.getY() / Variables.tileSize;

        if (SwingUtilities.isRightMouseButton(e)){
            handleRightClick(e);
        } else if (SwingUtilities.isLeftMouseButton(e)) {
            // Clear any highlighted squares and arrows
            engine.clearTiles();
            engine.clearArrows();

            /// If there's a piece at the clicked position, select it
            Piece pieceXY = engine.getPiece(col, row);
            if (pieceXY != null) {
                engine.selectedPiece = pieceXY;
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)){
            handleRightDrag(e);
        } else if (SwingUtilities.isLeftMouseButton(e)){
            if (engine.selectedPiece != null) {
                /// Make the piece follow the cursor (centered on mouse)
                engine.selectedPiece.xPos = e.getX() - Variables.tileSize / 2;
                engine.selectedPiece.yPos = e.getY() - Variables.tileSize / 2;

                engine.board.repaint();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e){
        if (SwingUtilities.isRightMouseButton(e)){
            handleRightRelease(e);
        } else if (SwingUtilities.isLeftMouseButton(e)){

            int col = e.getX() / Variables.tileSize;
            int row = e.getY() / Variables.tileSize;

            if (engine.selectedPiece != null) {
                Move move = new Move(engine, engine.selectedPiece, col, row);

                if(engine.isValidMove(move)){
                    engine.requestAMove(this.getClass(), move);
                } else {
                    /// Invalid move — snap the piece back to its original position
                    engine.selectedPiece.xPos = engine.selectedPiece.col * Variables.tileSize;
                    engine.selectedPiece.yPos = engine.selectedPiece.row * Variables.tileSize;
                }
            }
        }

        engine.selectedPiece = null;
        engine.board.repaint();
    }

    /// Right click — begin highlighting a square or arrow
    public void handleRightClick(MouseEvent e) {
        arrow = null;
        startPoint = e.getPoint();
        endPoint = e.getPoint();

        if (tile == null) {
            tile = new Tile(col, row);
        }

        tile.col = col;
        tile.row = row;
    }

    /// Right release — commit the highlight or arrow to the board
    private void handleRightRelease(MouseEvent e) {
        engine.board.paintPlanning(tile, arrow);
    }

    /// Right drag — update the arrow endpoint in real-time
    private void handleRightDrag(MouseEvent e) {
        if (arrow == null) {
            arrow = new Arrow(startPoint, endPoint);
        }
        arrow.startPoint = startPoint;
        arrow.endPoint = e.getPoint();
        engine.board.repaint();
    }
}
