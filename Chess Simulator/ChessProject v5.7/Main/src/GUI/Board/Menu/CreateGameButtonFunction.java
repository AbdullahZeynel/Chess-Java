package GUI.Board.Menu;

import GUI.Board.ChessClock;
import Game.GameEngine.ChessEngine;

public class CreateGameButtonFunction {

    public static Frame createButtonFunction(int whiteTime, int blackTime, int timeIncrement, String variant, String mode, String startingColor, Frame frame, ChessClock clock, ChessEngine engine) {
        frame.whiteTime = 300;
        frame.blackTime = 300;
        frame.timeIncrement = 1;
        frame.variant = "standard";
        frame.mode = "offline";
        frame.startingColor = "white";

        frame.layeredPane.remove(frame.panel); // bu noktada pop up arayuzunun acik olmasi imkansiz. Cikartmayabiliriz

        ChessClock chessClock = new ChessClock(whiteTime,blackTime, frame.whiteLabel,frame.blackLabel);

        frame.layeredPane.add(engine.board);
        engine.board.setBounds(620, 170, 680, 680);
        frame.layeredPane.add(frame.whiteLabel);
        frame.layeredPane.add(frame.blackLabel);

        frame.layeredPane.revalidate();
        frame.layeredPane.repaint();
        frame.revalidate();
        frame.repaint();

        return frame;
    }

}
