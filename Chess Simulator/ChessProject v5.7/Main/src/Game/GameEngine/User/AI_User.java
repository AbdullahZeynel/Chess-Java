package Game.GameEngine.User;

public class AI_User extends User {
    public AI_User(String userName) {
        super(userName);
    }


    protected void makeMove(String FENString) {
        engine.requestAMove(this, FENString);
    }

    public void getAiMove(String FENString) {
        makeMove(FENString);
    }
}
