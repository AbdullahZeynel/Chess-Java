

package pieces.Testers;

import main.Board;
import pieces.Piece;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class KnightBackUp extends Piece {
    public KnightBackUp(Board board, int col, int row, boolean isWhite) {
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * board.tile_size;
        this.yPos = row * board.tile_size;

        this.isWhite = isWhite;
        this.name = isWhite ? "WhiteKnight" : "DarkKnight";

        /*
        {
            try {
                sheet = ImageIO.read(new File("res/"+name+".png"));
                this.sprite = sheet;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

         */
    }
}

