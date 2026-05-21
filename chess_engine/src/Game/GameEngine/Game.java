package Game.GameEngine;

import GUI.Board.Menu.Frame;

import javax.swing.SwingUtilities;

/**
 * Application entry point.
 * Creates the chess engine and launches the GUI on the EDT (Event Dispatch Thread).
 */
public class Game {

    public static void main(String[] args) {
        /// Swing components must be created on the Event Dispatch Thread
        /// to prevent threading issues and ensure UI stability.
        SwingUtilities.invokeLater(() -> {
            ChessEngine engine = new ChessEngine();
            Frame frame = new Frame(engine);
        });
    }
}