package Game.GameEngine.User;

import Game.GameEngine.ChessEngine;
import Game.GameEngine.Move;

/**
 * Represents a player in the chess game.
 * Manages player identity, color assignment, and move submission.
 *
 * Supports both single-color and dual-color (offline practice) modes.
 */
public class User {

    public String userName;
    private String password;
    private String userID;

    private Move move;

    public boolean inGameIsWhite;
    public boolean isBothColors;

    ChessEngine engine;

    public User(String userName) {
        this.userName = userName;
    }

    /// Join a game with a specific color
    public void joinGame(ChessEngine engine, boolean isWhite) {
        this.engine = engine;
        this.inGameIsWhite = isWhite;
    }

    /// Overloaded — join a game with optional dual-color mode (for offline play)
    public void joinGame(ChessEngine engine, boolean isWhite, boolean isBothColors) {
        this.engine = engine;
        this.inGameIsWhite = isWhite;
        this.isBothColors = isBothColors;
    }

    /// Receives a move from the mouse input handler and submits it
    final public void readMoveFromInput(String caller, Move move){
        if(!caller.equals("MouseListener")){
            throw new IllegalArgumentException("caller must be MouseListener");
        }
        this.move = move;
        makeMove();
    }

    /// Submits the move to the engine. In dual-color mode, switches sides after each move.
    protected void makeMove(){
        engine.requestAMove(this, this.move);

        if(this.isBothColors)
            this.inGameIsWhite = !this.inGameIsWhite;
    }
}
