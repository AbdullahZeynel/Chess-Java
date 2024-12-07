package pieces;

import main.Board;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Rook extends Piece {
    public Rook(Board board, int col, int row, boolean isWhite){
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * board.tile_size;
        this.yPos = row * board.tile_size;

        this.isWhite = isWhite;
        this.name = "Rook";

        this.sprite = sheet.getSubimage(4 * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(board.tile_size, board.tile_size, BufferedImage.SCALE_SMOOTH);

        /*
                {
            try{
                BufferedImage sheet;
                String fileName = isWhite ? "White" : "Dark";
                fileName += this.name;
                sheet = ImageIO.read(new File("res/" + fileName + ".png"));
                this.sprite = sheet.getScaledInstance(board.tile_size, board.tile_size, BufferedImage.SCALE_SMOOTH);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
         */

    }
}
