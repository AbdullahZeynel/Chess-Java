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
        this.setBackground(Variables.frameBackGroundColor);

        this.addMouseListener(input);
        this.addMouseMotionListener(input);
    }

    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;        ///TypeCasting

        /// Enable anti-aliasing for smoother rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        ///Painting the board
        for (int c = 0; c < Variables.cols; c++)
            for (int r = 0; r < Variables.rows; r++){
                paintTile(g2d, c, r);
            }

        /// Paint coordinate labels (a-h, 1-8)
        paintCoordinates(g2d);

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

        /// Paint game-over overlay if the game has ended
        if (engine.isGameOver && engine.gameOverMessage != null) {
            paintGameOverOverlay(g2d);
        }
    }

    /// Draws a semi-transparent overlay with the game result
    private void paintGameOverOverlay(Graphics2D g2d) {
        int boardWidth  = Variables.cols * Variables.tileSize;
        int boardHeight = Variables.rows * Variables.tileSize;

        // Semi-transparent dark overlay
        g2d.setColor(new Color(0, 0, 0, 160));
        g2d.fillRect(0, 0, boardWidth, boardHeight);

        // Result card background
        int cardW = 320;
        int cardH = 100;
        int cardX = (boardWidth - cardW) / 2;
        int cardY = (boardHeight - cardH) / 2;

        g2d.setColor(new Color(39, 37, 34, 240));
        g2d.fillRoundRect(cardX, cardY, cardW, cardH, 20, 20);
        g2d.setColor(Variables.frameAccentColor);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(cardX, cardY, cardW, cardH, 20, 20);

        // Result text
        g2d.setFont(new Font("SansSerif", Font.BOLD, 28));
        FontMetrics fm = g2d.getFontMetrics();
        String msg = engine.gameOverMessage;
        int textX = cardX + (cardW - fm.stringWidth(msg)) / 2;
        int textY = cardY + (cardH + fm.getAscent() - fm.getDescent()) / 2 - 5;

        // Text shadow
        g2d.setColor(new Color(0, 0, 0, 120));
        g2d.drawString(msg, textX + 1, textY + 1);
        // Main text
        g2d.setColor(Variables.frameAccentColor);
        g2d.drawString(msg, textX, textY);
    }

    /// Paint coordinate labels on the board edges
    private void paintCoordinates(Graphics2D g2d) {
        g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
        String[] files = {"a", "b", "c", "d", "e", "f", "g", "h"};

        for (int c = 0; c < 8; c++) {
            // Bottom row file labels (a-h)
            boolean isLight = (c + 7) % 2 == 0;
            g2d.setColor(isLight ? Variables.coordLightColor : Variables.coordDarkColor);
            g2d.drawString(files[c], c * Variables.tileSize + 3, 7 * Variables.tileSize + Variables.tileSize - 4);
        }

        for (int r = 0; r < 8; r++) {
            // Left column rank labels (1-8)
            boolean isLight = (r) % 2 == 0;
            g2d.setColor(isLight ? Variables.coordLightColor : Variables.coordDarkColor);
            g2d.drawString(String.valueOf(8 - r), 3, r * Variables.tileSize + 14);
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
