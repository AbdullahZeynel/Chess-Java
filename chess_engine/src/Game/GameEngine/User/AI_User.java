package Game.GameEngine.User;

/**
 * AI player that makes moves using FEN string analysis.
 * TODO: Integrate with Stockfish or custom evaluation engine.
 */
public class AI_User extends User {
    public AI_User(String userName) {
        super(userName);
    }

    /// Makes a move by submitting a FEN string to the engine
    protected void makeMove(String FENString) {
        engine.requestAMove(this, FENString);
    }

    /// Entry point for AI move generation
    public void getAiMove(String FENString) {
        makeMove(FENString);
    }
}
