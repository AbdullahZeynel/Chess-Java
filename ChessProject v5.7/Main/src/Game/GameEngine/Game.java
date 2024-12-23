package Game.GameEngine;

import GUI.Board.Menu.Frame;
import resources.Variables;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Game {

    static ChessEngine engine = new ChessEngine();

    public static void main(String[] args) {
        //frame = new JFrame("Gazi Chess");

        Frame frame = new Frame();




        frame.createNewGame.createButton.addActionListener(e -> {

           frame.layeredPane.remove(frame.panel);
           frame.layeredPane.remove(frame.createNewGame);
           frame.layeredPane.add(engine.board);
           //                      680x680 boyularinda bir board.
            // 1920 - 680 = 1240/2=620 1020 - 680 = 340 / 2 = 170
           engine.board.setBounds(620,170,680,680);
           frame.layeredPane.revalidate();
           frame.layeredPane.repaint();
           frame.revalidate();
           frame.repaint();

        });


        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //frame.getContentPane().setBackground(Variables.frameBackGroundColor);
        //frame.setLayout(new GridBagLayout());
        //frame.setMinimumSize(Variables.defaultDimention);
        //frame.setLocationRelativeTo(null);

        //frame.add(engine.board);
        //frame.setVisible(true);
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