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
 * Left click: Click-to-select + click-to-move, OR drag-and-drop
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

    /// Tracks whether the piece is being dragged (vs. click-to-select)
    private boolean isDragging = false;

    public Input(ChessEngine engine) {
        this.engine = engine;
    }

    /// Clamps a board coordinate to the valid range [0, 7]
    private int clampToBoard(int value) {
        return Math.max(0, Math.min(7, value));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        /// Get the board coordinates of the click (clamped to board)
        col = clampToBoard(e.getX() / Variables.tileSize);
        row = clampToBoard(e.getY() / Variables.tileSize);
        isDragging = false;

        if (SwingUtilities.isRightMouseButton(e)){
            handleRightClick(e);
        } else if (SwingUtilities.isLeftMouseButton(e)) {
            // Clear any highlighted squares and arrows
            engine.clearTiles();
            engine.clearArrows();

            Piece pieceXY = engine.getPiece(col, row);

            if (engine.selectedPiece != null) {
                /// A piece is already selected — try to move it to the clicked square
                Move move = new Move(engine, engine.selectedPiece, col, row);

                if (engine.isValidMove(move)) {
                    /// Valid move — execute it
                    engine.requestAMove(this.getClass(), move);
                    engine.selectedPiece = null;
                    engine.board.repaint();
                    return;
                } else if (pieceXY != null && pieceXY.isWhite == engine.selectedPiece.isWhite) {
                    /// Clicked on another friendly piece — switch selection
                    engine.selectedPiece = pieceXY;
                    engine.board.repaint();
                    return;
                } else {
                    /// Clicked on an invalid square — deselect
                    engine.selectedPiece = null;
                    engine.board.repaint();
                    return;
                }
            }

            /// No piece selected yet — select one if present
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
                isDragging = true;
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
            if (isDragging && engine.selectedPiece != null) {
                /// Drag-and-drop mode — try to place piece at release position
                int col = clampToBoard(e.getX() / Variables.tileSize);
                int row = clampToBoard(e.getY() / Variables.tileSize);

                Move move = new Move(engine, engine.selectedPiece, col, row);

                if (engine.isValidMove(move)) {
                    engine.requestAMove(this.getClass(), move);
                } else {
                    /// Invalid move — snap the piece back to its original position
                    engine.selectedPiece.xPos = engine.selectedPiece.col * Variables.tileSize;
                    engine.selectedPiece.yPos = engine.selectedPiece.row * Variables.tileSize;
                }

                /// After drag, deselect (don't keep selection like click mode)
                engine.selectedPiece = null;
            }
            /// If not dragging (pure click), selection persists from mousePressed

            isDragging = false;
            engine.board.repaint();
        }
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
