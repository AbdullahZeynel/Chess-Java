package GUI;

import Game.GameEngine.ChessEngine;
import Game.GameEngine.Move;
import Game.GameEngine.User.User;
import Game.Piece.Piece;
import resources.Variables;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Input extends MouseAdapter {
    ChessEngine engine;
    User user;
    Graphics g;
    Tile tile = null;

    int col;
    int row;

    Point startPoint = null;
    Point endPoint = null;

    Arrow arrow = null;

    public Input(ChessEngine engine, User user) {
        this.engine = engine;
        this.user = user;
    }

    @Override
    public void mousePressed(MouseEvent e) {

        ///Basically get the coordinates of the click
        ///check wether there is a piece there or not
        ///if there is a piece assign it to board.selectedPiece


        col = e.getX() / Variables.tileSize;
        row = e.getY() / Variables.tileSize;

        if (SwingUtilities.isRightMouseButton(e)){
            handleRightClick(e);
        } else if (SwingUtilities.isLeftMouseButton(e)) {
            //clear highlighted circles
            engine.clearTiles();
            engine.clearArrows();

            ///delete and draw the selected piece here
            Piece pieceXY = engine.getPiece(col, row);
            if (pieceXY != null) {  //if there is a piece there then set piece
                if(user.isBothColors){
                    engine.selectedPiece = pieceXY;
                } else if(pieceXY.isWhite == user.inGameIsWhite){
                    engine.selectedPiece = pieceXY;
                }
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)){
            handleRightDragg(e);
        } else if (SwingUtilities.isLeftMouseButton(e)){
            if (engine.selectedPiece != null) {
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
                    user.readMoveFromInput("MouseListener", move);
                    //engine.makeMove( move);
                } else {        //return the piece to it's place
                    engine.selectedPiece.xPos = engine.selectedPiece.col * Variables.tileSize;
                    engine.selectedPiece.yPos = engine.selectedPiece.row * Variables.tileSize;
                }
            }
        }

        engine.selectedPiece = null;
        engine.board.repaint();
    }

        public void handleRightClick(MouseEvent e) {
            arrow = null;       /// reset the arrow object
            startPoint = e.getPoint();
            endPoint = e.getPoint();

            if (tile == null) {
                tile = new Tile(col, row);   /// Initialize tile if it is null
            }

            tile.col = col;
            tile.row = row;
        }

        private void handleRightRelease(MouseEvent e) {
            engine.board.paintPlanning(tile, arrow);
        /*
            engine.board.paintPlanning(tile);
            engine.board.paintPlanning(arrow);
         */
        }

        private void handleRightDragg(MouseEvent e) {
            if (arrow == null) {
                arrow = new Arrow(startPoint, endPoint);
            }
            arrow.startPoint = startPoint;
            arrow.endPoint = e.getPoint();        //keep updating the end point
            engine.board.repaint();
        }
}
