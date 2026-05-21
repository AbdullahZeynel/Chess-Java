package Game.GameEngine;

import GUI.Board.Menu.Frame;

/**
 * Application entry point.
 * Creates the chess engine and launches the GUI.
 */
public class Game {

    public static void main(String[] args) {
        /// Create a single ChessEngine instance and pass it to the Frame
        ChessEngine engine = new ChessEngine();
        Frame frame = new Frame(engine);
    }
}