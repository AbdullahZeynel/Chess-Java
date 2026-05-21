package Game.Piece;

import Game.GameEngine.ChessEngine;

/**
 * Three Checks variant king — tracks how many times it has been checked.
 * When the check count reaches 3, the game ends.
 */
public class ThreeChecksKing extends King {
    public int checksCount;
    public boolean isChecked;
    public boolean savedIsWhite;

    public ThreeChecksKing(ChessEngine engine, int col, int row, boolean isWhite) {
        super(engine, col, row, isWhite);
        this.checksCount = 0;
        this.isChecked = false;
    }

    /// Toggles the check status
    public void shiftCheck(){
        this.isChecked = !this.isChecked;
    }
}
