package Game.GameEngine;

import resources.Variables;

import javax.swing.*;
import java.awt.*;

public class Game {
    static JFrame frame;
    static ChessEngine engine;

    public static void main(String[] args) {
        frame = new JFrame("Gazi Chess");
        engine = new ChessEngine();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().setBackground(Variables.frameBackGroundColor);
        frame.setLayout(new GridBagLayout());
        frame.setMinimumSize(Variables.defaultDimention);
        frame.setLocationRelativeTo(null);

        frame.add(engine.board);
        frame.setVisible(true);
    }
}


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