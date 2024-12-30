package Game.GameEngine;

import GUI.Board.ChessClock;
import GUI.Board.Menu.Frame;
import GUI.Board.Menu.MainMenu;
import resources.Variables;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Game {

    static ChessEngine engine = new ChessEngine();

    public static void main(String[] args) {


        Frame frame = new Frame();
        JLabel whiteLabel = new JLabel();
        JLabel blackLabel = new JLabel();

    }
}





























// frame = new JFrame("Gazi Chess");
//ChessClock chessClock = new ChessClock(300, 300, whiteLabel, blackLabel);
//engine.getClocks(chessClock);
        /*frame.createNewGame.createButton.addActionListener(e -> {
            frame.layeredPane.remove(frame.panel);
            frame.layeredPane.remove(frame.createNewGame);
            frame.layeredPane.add(engine.board);
            // 680x680 boyularinda bir board.
            // 1920 - 680 = 1240/2=620 1020 - 680 = 340 / 2 = 170
            engine.board.setBounds(620, 170, 680, 680);

            frame.layeredPane.add(whiteLabel);
            frame.layeredPane.add(blackLabel);

            chessClock.startWhiteTimer();

            frame.layeredPane.revalidate();
            frame.layeredPane.repaint();
            frame.revalidate();
            frame.repaint();
        });

/*

    public static void showBoard(){
        frame.add(engine.board);
        frame.setVisible(true);
    }

    public static void showPromotionPanel(){
        frame.add(engine.promotionsPanel);
        frame.setVisible(true);
    }
 */