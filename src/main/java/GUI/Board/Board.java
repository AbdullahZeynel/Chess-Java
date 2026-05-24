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
    public ChessEngine engine;
    public Input input;

    private ArrayList<Tile>   tileList;
    private ArrayList<Piece>  pieceList;
    private ArrayList<Arrow>  arrowList;

    /// Whether the board is flipped (black at bottom)
    public boolean isFlipped = false;

    public Board(ChessEngine engine){
        this.engine = engine;
        this.input = new Input(engine);

        this.setPreferredSize(Variables.boardDimention);
        this.setBackground(Variables.frameBackGroundColor);

        this.addMouseListener(input);
        this.addMouseMotionListener(input);
    }

    /// Flip the board perspective and repaint
    public void flipBoard() {
        isFlipped = !isFlipped;
        repaint();
    }

    /// Convert logical col to screen col (flip-aware)
    public int screenCol(int col) {
        return isFlipped ? 7 - col : col;
    }

    /// Convert logical row to screen row (flip-aware)
    public int screenRow(int row) {
        return isFlipped ? 7 - row : row;
    }

    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        /// Enable anti-aliasing for smoother rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        /// Paint the board tiles
        for (int c = 0; c < Variables.cols; c++)
            for (int r = 0; r < Variables.rows; r++){
                paintTile(g2d, c, r);
            }

        /// Paint coordinate labels (a-h, 1-8)
        paintCoordinates(g2d);

        getLists();

        /// Paint highlighted tiles (right-click squares)
        for (Tile tile : tileList){
            int sc = screenCol(tile.col);
            int sr = screenRow(tile.row);
            tile.paintHighlightedSquareAt(g2d, sc, sr);
        }

        /// Paint pieces
        for (Piece piece : pieceList){
            paintPieceFlipAware(g2d, piece);
        }

        /// Paint valid move highlights
        if(engine.selectedPiece != null)
            for(int r = 0; r < Variables.rows; r++)
                for(int c = 0; c < Variables.cols; c++)
                    if(engine.isValidMove(new Move(engine, engine.selectedPiece, c, r))){
                        int sc = screenCol(c);
                        int sr = screenRow(r);
                        int px = sc * Variables.tileSize;
                        int py = sr * Variables.tileSize;

                        if(engine.getPiece(c, r) != null){
                            // Capture move highlight
                            g2d.setColor(Variables.captureMoveColor);
                            g2d.fill3DRect(px, py, Variables.tileSize, Variables.tileSize, true);

                            // Repaint tile with circle cutout
                            boolean oddOrEven = (c + r) % 2 == 0;
                            g2d.setColor(oddOrEven ? Variables.whiteTileColor : Variables.blackTileColor);
                            g2d.fillOval(px, py, Variables.tileSize, Variables.tileSize);
                        } else {
                            // Valid move dot
                            g2d.setColor(Variables.validMoveColor);
                            int radius = Variables.tileSize / 4;
                            int centerX = (px + Variables.tileSize / 3) + 4;
                            int centerY = (py + Variables.tileSize / 3) + 4;
                            g2d.fillOval(centerX, centerY, radius, radius);
                        }
                    }

        /// Paint check indicators and pieces (second pass for layering)
        for (Piece piece: pieceList){
            if (piece instanceof King){
                Piece king = engine.findKing(piece.isWhite);
                if (engine.checkScanner.isKingChecked(new Move(engine, king, king.col, king.row))) {
                    int sc = screenCol(king.col);
                    int sr = screenRow(king.row);
                    g2d.setColor(Variables.checkColor);
                    g2d.fill3DRect(sc * Variables.tileSize, sr * Variables.tileSize, Variables.tileSize, Variables.tileSize, true);
                }
            }
            paintPieceFlipAware(g2d, piece);
        }

        /// Paint arrows
        for (Arrow arrow : arrowList){
            arrow.drawArrow(g2d);
        }

        /// Paint game-over overlay if the game has ended
        if (engine.isGameOver && engine.gameOverMessage != null) {
            paintGameOverOverlay(g2d);
        }
    }

    /// Draws a piece at its flip-aware screen position.
    /// During drag, uses the raw pixel position (xPos/yPos) set by Input.
    private void paintPieceFlipAware(Graphics2D g2d, Piece piece) {
        if (piece == engine.selectedPiece && input.isDragging()) {
            // Dragging — use raw pixel position
            piece.paint(g2d);
        } else {
            // Static — use flip-aware position
            int px = screenCol(piece.col) * Variables.tileSize;
            int py = screenRow(piece.row) * Variables.tileSize;
            piece.paintAt(g2d, px, py);
        }
    }

    /// Draws a theme-aware overlay with the game result
    private void paintGameOverOverlay(Graphics2D g2d) {
        int boardWidth  = Variables.cols * Variables.tileSize;
        int boardHeight = Variables.rows * Variables.tileSize;

        g2d.setColor(Variables.gameOverOverlayColor);
        g2d.fillRect(0, 0, boardWidth, boardHeight);

        g2d.setFont(new Font("SansSerif", Font.BOLD, 28));
        FontMetrics fm = g2d.getFontMetrics();
        String msg = engine.gameOverMessage;
        int cardW = Math.max(320, fm.stringWidth(msg) + 80);
        int cardH = 90;
        int cardX = (boardWidth - cardW) / 2;
        int cardY = (boardHeight - cardH) / 2;

        g2d.setColor(Variables.gameOverCardBgColor);
        g2d.fillRoundRect(cardX, cardY, cardW, cardH, 20, 20);

        g2d.setColor(Variables.gameOverCardBorderColor);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(cardX, cardY, cardW, cardH, 20, 20);

        int textX = cardX + (cardW - fm.stringWidth(msg)) / 2;
        int textY = cardY + (cardH + fm.getAscent() - fm.getDescent()) / 2;

        g2d.setColor(Variables.gameOverTextColor);
        g2d.drawString(msg, textX, textY);
    }

    /// Paint coordinate labels on the board edges (flip-aware)
    private void paintCoordinates(Graphics2D g2d) {
        g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
        String[] files = {"a", "b", "c", "d", "e", "f", "g", "h"};

        for (int c = 0; c < 8; c++) {
            int fc = isFlipped ? 7 - c : c;
            boolean isLight = (c + 7) % 2 == 0;
            g2d.setColor(isLight ? Variables.coordLightColor : Variables.coordDarkColor);
            g2d.drawString(files[fc], c * Variables.tileSize + 3, 7 * Variables.tileSize + Variables.tileSize - 4);
        }

        for (int r = 0; r < 8; r++) {
            int fr = isFlipped ? 7 - r : r;
            boolean isLight = (r) % 2 == 0;
            g2d.setColor(isLight ? Variables.coordLightColor : Variables.coordDarkColor);
            g2d.drawString(String.valueOf(8 - fr), 3, r * Variables.tileSize + 14);
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
        g2d.fillRect(col * Variables.tileSize, row * Variables.tileSize, Variables.tileSize, Variables.tileSize);
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

    public void paintPlanning(Tile t){
        if (t != null){
            Tile tile = new Tile(t.col, t.row);
            tileList.add(tile);
        }
        repaint();
    }

    public void paintPlanning(Arrow arr){
        if (arr != null){
            Arrow arrow = new Arrow(arr.startPoint, arr.endPoint);
            arrowList.add(arrow);
        }
        repaint();
    }
}
