package GUI;

import resources.Variables;

import java.awt.*;

/**
 * Represents a highlighted tile on the board.
 * Used for right-click square highlighting, check indicators,
 * and capture move visualization.
 */
public class Tile {

    public int col;
    public int row;
    public int squareSide;

    public int radius;
    public int centerX;
    public int centerY;

    private boolean oddOrEven;

    public Tile(int col, int row) {
        this.col = col;
        this.row = row;

        this.radius = Variables.tileSize;

        this.centerX = this.col * Variables.tileSize;
        this.centerY = this.row * Variables.tileSize;
    }

    /// Returns true for even (light) tiles, false for odd (dark) tiles
    private static boolean getOddOrEven(int col, int row) {
        return (col + row) % 2 == 0;
    }

    /// Paints the base tile color (light or dark)
    public void paintTile(Graphics2D g2d, Tile tile) {
        oddOrEven = getOddOrEven(col, row);
        g2d.setColor(oddOrEven ? Variables.whiteTileColor : Variables.blackTileColor);
        g2d.fillRect(tile.col * Variables.tileSize, tile.row * Variables.tileSize, Variables.tileSize, Variables.tileSize);
    }

    /// Paints a highlighted ring on the tile (right-click indicator).
    /// Draws a colored circle then carves out a smaller circle in the tile color,
    /// creating a ring effect.
    public void paintHighlightedSquare(Graphics g, Tile tile) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Variables.highlightedSquareColor);
        g2d.fillOval(centerX, centerY, radius, radius);

        oddOrEven = getOddOrEven(col, row);

        g2d.setColor(oddOrEven ? Variables.whiteTileColor : Variables.blackTileColor);
        g2d.fillOval(tile.col * Variables.tileSize + 5, tile.row * Variables.tileSize + 5, Variables.tileSize - 10, Variables.tileSize - 10);
    }

    /// Repaints the tile with a circle cutout for capture move indicators
    public static void rePaintCaptureTile(Graphics2D g2d, int col, int row) {
        boolean oddOrEven = getOddOrEven(col, row);

        g2d.setColor(oddOrEven ? Variables.whiteTileColor : Variables.blackTileColor);
        g2d.fillOval(col * Variables.tileSize, row * Variables.tileSize, Variables.tileSize, Variables.tileSize);
    }

    /// Paints the king's square in red when in check
    public static void paintChecks(Graphics2D g2d, int col, int row) {
        g2d.setColor(Variables.checkColor);
        g2d.fill3DRect(col * Variables.tileSize, row * Variables.tileSize, Variables.tileSize, Variables.tileSize, true);
    }

    /// Paints capture move indicators with a circle overlay
    public void paintCaptures(Graphics2D g2d, Tile tile) {
        oddOrEven = getOddOrEven(col, row);

        g2d.setColor(Variables.captureMoveColor);
        g2d.fill3DRect(tile.col * Variables.tileSize, tile.row * Variables.tileSize, Variables.tileSize, Variables.tileSize, true);

        g2d.setColor(oddOrEven ? Variables.whiteTileColor : Variables.blackTileColor);
        g2d.fillOval(tile.col * Variables.tileSize, this.row * Variables.tileSize, Variables.tileSize, Variables.tileSize);
    }
}
