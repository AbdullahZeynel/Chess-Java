package pieces;

import main.Board;
import res.Variables;

import java.awt.*;

public class Tile {
    public int col;
    public int row;

    public int radius;
    public int centerX;
    public int centerY;

    public Tile(int col, int row) {
        this.col = col;
        this.row = row;

        this.radius = Variables.tileSize;
        // Calculate the radius and diameter of the circle


        // Calculate the center of the tile
        this.centerX = (this.col * Variables.tileSize);
        this.centerY = (this.row * Variables.tileSize);
    }

    public void paint(Graphics g, Board board) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(new Color(71, 143, 138, 255));    //valid move color


        // Draw the circle
        g2d.fillOval(centerX, centerY, radius, radius);
    }

}
