package Game.Piece;

import Game.GameEngine.ChessEngine;
import resources.Variables;

import java.awt.image.BufferedImage;

/**
 * Knight piece — moves in an L-shape: 2 squares in one direction and 1 in the other.
 * Knights can jump over other pieces (no collision detection needed).
 */
public class Knight extends Piece {
    public Knight(ChessEngine engine, int col, int row, boolean isWhite){
        super(engine);
        this.col = col;
        this.row = row;
        this.xPos = col * Variables.tileSize;
        this.yPos = row * Variables.tileSize;

        this.isWhite = isWhite;
        this.name = "Knight";
        this.pieceChar = "N";

        this.sprite = sheet.getSubimage(3 * sheetScale, (isWhite ? 0 : sheetScale), sheetScale, sheetScale)
                           .getScaledInstance(Variables.tileSize, Variables.tileSize, BufferedImage.SCALE_SMOOTH);
    }

    @Override
    public boolean pieceMoves(int col, int row) {
        /// Knight always moves in an L-shape: the product of the offsets must equal 2
        /// e.g., (2,1) -> 2*1=2, (1,2) -> 1*2=2
        return Math.abs(this.col - col) * Math.abs(this.row - row) == 2;
    }

}
