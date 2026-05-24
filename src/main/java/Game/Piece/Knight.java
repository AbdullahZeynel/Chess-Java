package Game.Piece;

import Game.GameEngine.ChessEngine;
import resources.Variables;



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

        this.sprite = getCachedSprite(3, isWhite);
    }

    @Override
    public boolean pieceMoves(int col, int row) {
        /// Knight always moves in an L-shape: the product of the offsets must equal 2
        /// e.g., (2,1) -> 2*1=2, (1,2) -> 1*2=2
        return Math.abs(this.col - col) * Math.abs(this.row - row) == 2;
    }

}
