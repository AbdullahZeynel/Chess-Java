package GUI;

import resources.Variables;

import java.awt.*;

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

        ///The tileSize is the radius at the same time
        this.radius = Variables.tileSize;

        ///Calculate the center of the tile
        this.centerX = this.col * Variables.tileSize;
        this.centerY = this.row * Variables.tileSize;
    }

    private static boolean getOddOrEven(int col, int row) {    ///Calculate the odd and even tiles
        ///true for even (white), false for odd (black)
        return (col + row) % 2 == 0;
    }

    public void paintTile(Graphics2D g2d, Tile tile) {  ///Paint the white and dark tiles while setting the board
        oddOrEven = getOddOrEven(col, row);
        g2d.setColor(oddOrEven ? Variables.whiteTileColor : Variables.blackTileColor);  //Even tiles are white, odd tiles are black
        g2d.fillRect(tile.col * Variables.tileSize, tile.row * Variables.tileSize, Variables.tileSize, Variables.tileSize);
    }

    public void paintHighlightedSquare(Graphics g, Tile tile) { ///Method's old name was paint!!
        ///When rightClicked highlight the square with a nice circle and then extract from it the tileColor so that it's a while
        Graphics2D g2d = (Graphics2D) g;        //type casting

        g2d.setColor(Variables.highlightedSquareColor);         //highlighted squares
        g2d.fillOval(centerX, centerY, radius, radius); //paint the circle

        oddOrEven = getOddOrEven(col, row);

        g2d.setColor(oddOrEven ? Variables.whiteTileColor : Variables.blackTileColor);
        g2d.fillOval(tile.col * Variables.tileSize + 5, tile.row * Variables.tileSize + 5, Variables.tileSize - 10, Variables.tileSize - 10);
    }

    public static void rePaintCaptureTile(Graphics2D g2d, int col, int row) {
        boolean oddOrEven = getOddOrEven(col, row);

        g2d.setColor(oddOrEven ? Variables.whiteTileColor : Variables.blackTileColor);
        g2d.fillOval(col * Variables.tileSize, row * Variables.tileSize, Variables.tileSize, Variables.tileSize);
    }


    public static void paintChecks(Graphics2D g2d, int col, int row) {    ///When in check paint the King's square in red
        ///Maybe also paint the square of the checker piece in red in the future
        g2d.setColor(Variables.checkColor);
        g2d.fill3DRect(col * Variables.tileSize, row * Variables.tileSize, Variables.tileSize, Variables.tileSize, true);
    }

    public void paintCaptures(Graphics2D g2d, Tile tile) {  ///Paint the capture moves differently from the validMoves
        oddOrEven = getOddOrEven(col, row);

        g2d.setColor(Variables.captureMoveColor);
        g2d.fill3DRect(tile.col * Variables.tileSize, tile.row * Variables.tileSize, Variables.tileSize, Variables.tileSize, true);

        g2d.setColor(oddOrEven ? Variables.whiteTileColor : Variables.blackTileColor);
        g2d.fillOval(tile.col * Variables.tileSize, this.row * Variables.tileSize, Variables.tileSize, Variables.tileSize);
    }
}
