package pieces.Testers;

import main.Board;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageDivider extends SpriteCreator {
/*
    public ImageDivider(Board board, int col, int row, boolean isWhite, String name, int order){
        super(board);
        this.col = col;
        this.row = row;
        this.isWhite = isWhite;

        this.xPos = col * board.tile_size;
        this.yPos = row * board.tile_size;

        this.name = name;
        this.sprite = sheet.getSubimage(order * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale);

        BufferedImage bufferedSprite = new BufferedImage(sprite.getWidth(null), sprite.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        bufferedSprite.getGraphics().drawImage(sprite, 0, 0, null);


        String path = "/pieces/" + this.name + ".png";
        File file = new File(path);

        {
            try{
                ImageIO.write(bufferedSprite, "png", file);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

    }
 */

    public ImageDivider(Board board, boolean isWhite, String name, int order){
        super(board);
        this.isWhite = isWhite;
        this.name = name;
        this.sprite = sheet.getSubimage(order * sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(board.tile_size, board.tile_size, BufferedImage.SCALE_SMOOTH);

        BufferedImage bufferedSprite = new BufferedImage(sprite.getWidth(null), sprite.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        bufferedSprite.getGraphics().drawImage(sprite, 0, 0, null);


        String path = "res/" + this.name + ".png";
        File file = new File(path);

        {
            try{
                ImageIO.write(bufferedSprite, "png", file);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
