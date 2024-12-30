package Game.Piece;

import Game.GameEngine.ChessEngine;

public class ThreeChecksKing extends King {
    public int checksCount;
    public boolean isChecked;
    public boolean savedİsWhite;

    public ThreeChecksKing(ChessEngine engine, int col, int row, boolean isWhite) {
        super(engine, col, row, isWhite);
        this.checksCount = 0;
        this.isChecked = false;
    }

    public void shiftCheck(){
        this.isChecked = !this.isChecked;
    }
}
