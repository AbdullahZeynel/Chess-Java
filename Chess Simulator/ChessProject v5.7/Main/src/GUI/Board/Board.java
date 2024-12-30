package GUI.Board;

import GUI.Arrow;
import GUI.Input;
import GUI.Tile;
import Game.GameEngine.ChessEngine;
import Game.GameEngine.Move;
import Game.Piece.King;
import Game.Piece.Piece;
import resources.Variables;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Board extends JPanel {
    public ChessEngine engine; // we can make these two private
    public Input input;

    private ArrayList<Tile>   tileList;
    private ArrayList<Piece>  pieceList;
    private ArrayList<Arrow>  arrowList;

    public Board(ChessEngine engine){
        this.engine = engine;
        this.input = new Input(engine);

        this.setPreferredSize(Variables.boardDimention);

        this.addMouseListener(input);
        this.addMouseMotionListener(input);
    }

    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;        ///TypeCasting

        ///Painting the board
        for (int c = 0; c < Variables.cols; c++)
            for (int r = 0; r < Variables.rows; r++){
                paintTile(g2d, c, r);
            }

        getLists();
        ///Paint highlighted tiles
        for (Tile tile : tileList){
            tile.paintHighlightedSquare(g2d, tile);
        }

        ///Paint peices
        for (Piece piece : pieceList){
            piece.paint(g2d);
        }

        ///Paint highlights
        if(engine.selectedPiece != null)
            for(int r = 0; r < Variables.rows; r++)
                for(int c = 0; c < Variables.cols; c++)
                    if(engine.isValidMove(new Move(engine, engine.selectedPiece, c, r))){
                        if(engine.getPiece(c, r) != null){
                            ///Checkes whether there is a piece at the aimed tile or not
                            ///Because it is already a valid move then surely it is not of the same team
                            ///Or at least it is not an invalid move. This is out of the concerns of this method
                            ///Now we just want to check wether the valid move is just a move or in fact a capture move

                            g2d.setColor(Variables.captureMoveColor);
                            g2d.fill3DRect(c * Variables.tileSize, r * Variables.tileSize, Variables.tileSize, Variables.tileSize, true);

                            Tile.rePaintCaptureTile(g2d, c, r);
                        } else {
                            g2d.setColor(Variables.validMoveColor);

                            ///Calculate the radius of the circle
                            int radius = Variables.tileSize / 4;

                            ///Calculate the center of the tile
                            int centerX = (c * Variables.tileSize + Variables.tileSize / 3) + 4;
                            int centerY = (r * Variables.tileSize + Variables.tileSize / 3) + 4;

                            ///Draw the circle
                            g2d.fillOval(centerX, centerY, radius, radius);
                        }
                    }

        for (Piece piece: pieceList){
            if (piece instanceof King){
                Piece king = engine.findKing(piece.isWhite);
                if (engine.checkScanner.isKingChecked(new Move(engine, king, king.col, king.row)))
                    Tile.paintChecks(g2d, king.col, king.row);
            }
            piece.paint(g2d);
        }

        ///Paint arrows
        for (Arrow arrow : arrowList){
            arrow.drawArrow(g2d);
        }
    }

    private void getLists(){
        pieceList = engine.invokeIfAllowed(this.getClass(),"pieceList");

        arrowList = engine.invokeIfAllowed(this.getClass(),"arrowList");

        tileList = engine.invokeIfAllowed(this.getClass(),"tileList");

    }

    private void paintTile(Graphics2D g2d, int col, int row){
        boolean oddOrEven = ((col + row)%2 == 0);
        g2d.setColor(oddOrEven ? Variables.whiteTileColor : Variables.blackTileColor);
        g2d.fillRect(col * Variables.tileSize, row * Variables.tileSize, Variables.tileSize, Variables.tileSize );

    }

    public void paintPlanning(Tile t, Arrow arr){
        if (t != null){
            Tile tile = new Tile(t.col, t.row);
            tileList.add(tile);
        }

        if (arr != null){
            Arrow arrow = new Arrow(arr.startPoint, arr.endPoint);
            arrowList.add(arrow);
        }

        repaint();
    }

    ///Method Overloading
    public void paintPlanning(Tile t){
        if (t != null){
            Tile tile = new Tile(t.col, t.row);
            tileList.add(tile);
        }

        repaint();
    }
    ///Method Overloading
    public void paintPlanning(Arrow arr){
        if (arr != null){
            Arrow arrow = new Arrow(arr.startPoint, arr.endPoint);
            arrowList.add(arrow);
        }

        repaint();
    }
}
