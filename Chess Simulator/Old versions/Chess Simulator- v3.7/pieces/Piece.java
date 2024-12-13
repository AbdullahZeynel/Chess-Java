package pieces;

import main.Board;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Piece {

    public int xPos, yPos;
    public int col, row;

    public boolean isWhite;
    public String name;
    public int value;

    public boolean hasMoved = false;  //for pawn and king

    BufferedImage sheet;


    {
        try{
            sheet = ImageIO.read(new File("res/pieces.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected int sheetScale = sheet.getWidth()/6;

    Image sprite;
    Board board;

    public Piece(Board board){

        this.board = board;
    }

    public void paint(Graphics g2d){

        g2d.drawImage(sprite, xPos, yPos, null);
    }
    public void paintChecks(Graphics g2d){
        g2d.setColor(new Color(195, 17, 20, 208));
        g2d.fillRect(this.col * board.tile_size, this.row * board.tile_size, board.tile_size, board.tile_size);
    }

    public boolean isValidMovement(int col, int row){
        return true;
    }

    public boolean moveCollidesWithPiece(int col, int row){
        return false;
    }

}
