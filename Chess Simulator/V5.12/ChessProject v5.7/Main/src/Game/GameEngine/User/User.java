package Game.GameEngine.User;

import Game.GameEngine.ChessEngine;
import Game.GameEngine.Move;

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

    public void joinGame(ChessEngine engine, boolean isWhite) {
        this.engine = engine;
        this.inGameIsWhite = isWhite;
    }

    /// Overloading
    public void joinGame(ChessEngine engine, boolean isWhite, boolean isBothColors) {
        this.engine = engine;
        this.inGameIsWhite = isWhite;
        this.isBothColors = isBothColors;
    }

    final public void readMoveFromInput(String caller, Move move){
        if(!caller.equals("MouseListener")){
            throw new IllegalArgumentException("caller must be MouseListener");
        }
        this.move = move;
        makeMove();
    }

    protected void makeMove(){
        engine.requestAMove(this, this.move);

        if(this.isBothColors)
            this.inGameIsWhite = !this.inGameIsWhite;
    }






}
