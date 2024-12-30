package Game.Piece;

import Game.GameEngine.ChessEngine;
import resources.Variables;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class Piece implements PieceMoves {

    public int xPos, yPos;
    public int col, row;

    public boolean isWhite;
    public String name;
    public String pieceChar;
    public int value;       //wtf is that bro?

    protected Image sprite;
    protected ChessEngine engine;

    public boolean isFirstMove = true;       //guess that's unncessasry here maybe will just add it to rook, king, pawn

    BufferedImage sheet;

    {
        try{
            sheet = ImageIO.read(new File(Variables.piecesFilePath));       //later on change the path to a String var in game or board, add more error handling
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected int sheetScale = sheet.getWidth()/6;      // /6 because there are 6 cols of pieces in the image



    public Piece(ChessEngine engine) {     //constr with board ?
        this.engine = engine;
    }

    public void paint(Graphics g2d) {
        g2d.drawImage(sprite, xPos, yPos, null);        //what does the null stands for?
    }

    /*
    No need to override the interface functions here cause it is an abstract class!
     */
}
