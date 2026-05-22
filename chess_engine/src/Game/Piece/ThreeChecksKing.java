package Game.Piece;

import Game.GameEngine.ChessEngine;

/**
 * Three Checks variant king — tracks how many times it has been checked.
 * When the check count reaches 3, the game ends.
 *
 * The counter is managed by ThreeChecksChess.updateGameState(),
 * not by the king itself — the king only holds the state.
 */
public class ThreeChecksKing extends King {
    public int checksReceived;

    public ThreeChecksKing(ChessEngine engine, int col, int row, boolean isWhite) {
        super(engine, col, row, isWhite);
        this.checksReceived = 0;
    }
}
