/*
package GUI.Board.Menu;

import GUI.Board.ChessClock;
import Game.GameEngine.ChessEngine;

import javax.swing.*;
import java.awt.*;

public class CreateNewGame extends JLabel {

    public JButton exitButton = new JButton("Exit");
    public JButton createButton = new JButton("Create The Game");

    Frame frame = new Frame();
    JLabel whiteLabel = new JLabel();
    JLabel blackLabel = new JLabel();

    ChessClock chessClock = new ChessClock(300, 300, whiteLabel, blackLabel);
    static ChessEngine engine = new ChessEngine();

    public CreateNewGame(Frame frame) {

        TextField modeField = new TextField("Mode", 50,50,150,80);

        TextField variantTextField = new TextField("Variant", 50,200,150,80);

        TextField timeTextField = new TextField("Time", 50,350,150,80);

        TextField selectColorTextField = new TextField("Color", 550,120,180,80);

        String[] variant = {"Standart", "MergeChess", "ThreeChecks"};
        JComboBox<String> variantC = new JComboBox<String>(variant);
        variantC.setToolTipText("Variant");
        variantC.setBounds(220, 200, 200, 80);
        variantC.setBorder(null);




        String[] time = {"1", "2", "3", "5", "10", "15", "30", "90"};
        JComboBox<String> timeC = new JComboBox<String>(time);
        timeC.setToolTipText("Time");
        timeC.setBounds(220, 350, 200, 80);

        String[] mode = {"Online", "Offline", "vs. Stockfish"};
        JComboBox<String> modeC = new JComboBox<String>(mode);
        modeC.setBounds(220, 50, 200, 80);



        createButton.setBounds(220, 550,600,100);
        createButton.addActionListener(e -> {
            this.frame.layeredPane.remove(this.frame.panel);
            this.frame.layeredPane.remove(this.frame.createNewGame);
            this.frame.layeredPane.add(engine.board);
            // 680x680 boyularinda bir board.
            // 1920 - 680 = 1240/2=620 1020 - 680 = 340 / 2 = 170
            engine.board.setBounds(620, 170, 680, 680);

            this.frame.layeredPane.add(whiteLabel);
            this.frame.layeredPane.add(blackLabel);

            chessClock.startWhiteTimer();

            this.frame.layeredPane.revalidate();
            this.frame.layeredPane.repaint();
            this.frame.revalidate();
            this.frame.repaint();
        });


        exitButton.setBounds(950, 15,50,50);

        JButton randomColorButton = new JButton("R");
        randomColorButton.setBounds(610, 215,60,65);

        JButton blackColorButton = new JButton("B");
        blackColorButton.setBounds(550, 230,50,50);

        JButton whiteColorButton = new JButton("W");
        whiteColorButton.setBounds(680, 230,50,50);

        setBackground(Color.DARK_GRAY);
        setOpaque(true);
        setLayout(null);
        add(exitButton);
        add(selectColorTextField);
        add(whiteColorButton);
        add(blackColorButton);
        add(randomColorButton);
        add(createButton);
        add(variantTextField);
        add(timeTextField);
        add(modeField);
        add(variantC);
        add(modeC);
        add(timeC);
        setBounds(450, 100, 1020, 820);

    }


}


 */

package GUI.Board.Menu;

import GUI.Board.ChessClock;
import Game.GameEngine.ChessEngine;

import javax.swing.*;
import java.awt.*;

public class CreateNewGame extends JLabel {

    public JButton exitButton = new JButton("Exit");
    public JButton createButton = new JButton("Create The Game");

    private Frame frame;
    JLabel whiteLabel = new JLabel();
    JLabel blackLabel = new JLabel();

    public int whiteTime = 60;
    public int blackTime = 60;
    public int timeIncrement = 0;

    public String startingColor = "white";
    public String variant = "standart";
    public String mode = "offline";



    ChessClock chessClock;
    static ChessEngine engine = new ChessEngine();

    public CreateNewGame(Frame frame) {
        this.frame = frame;


        TextField modeField = new TextField("Mode", 50,50,150,80);
        TextField variantTextField = new TextField("Variant", 50,200,150,80);
        TextField timeTextField = new TextField("Time", 50,350,150,80);
        TextField selectColorTextField = new TextField("Color", 550,120,180,80);

        String[] variantText = {"Standart", "MergeChess", "ThreeChecks"};
        JComboBox<String> variantC = new JComboBox<String>(variantText);
        variantC.setToolTipText("Variant");
        variantC.setBounds(220, 200, 200, 80);
        variantC.setBorder(null);

        String[] timeText = {"1", "2", "3", "5", "10", "15", "30", "90"};
        JComboBox<String> timeC = new JComboBox<String>(timeText);
        timeC.setToolTipText("Time");
        timeC.setBounds(220, 350, 200, 80);

        String[] modeText = {"Online", "Offline", "vs. Stockfish"};
        JComboBox<String> modeC = new JComboBox<String>(modeText);
        modeC.setBounds(220, 50, 200, 80);

        createButton.setBounds(220, 550, 600, 100);
        createButton.addActionListener(e -> {



            this.whiteTime = Integer.parseInt(timeC.getSelectedItem().toString()) * 60; // you select it as minutes in the menu
            this.blackTime = Integer.parseInt(timeC.getSelectedItem().toString()) * 60; // we need to convert it to seconds to use in the funciton.
            this.timeIncrement = 1;
            this.variant = variantC.getSelectedItem().toString();
            this.mode = modeC.getSelectedItem().toString();
            this.startingColor = startingColor;
            this.chessClock = new ChessClock(whiteTime, blackTime, whiteLabel, blackLabel);


            this.frame.layeredPane.remove(this.frame.panel);
            this.frame.layeredPane.remove(this.frame.createNewGame);
            this.frame.layeredPane.add(engine.board);
            // 680x680 boyularinda bir board.
            // 1920 - 680 = 1240/2=620 1020 - 680 = 340 / 2 = 170
            engine.board.setBounds(620, 170, 680, 680);

            this.frame.layeredPane.add(whiteLabel);
            this.frame.layeredPane.add(blackLabel);

            chessClock.setTimers();
            engine.getClocks(chessClock);

            this.frame.layeredPane.revalidate();
            this.frame.layeredPane.repaint();
            this.frame.revalidate();
            this.frame.repaint();


        });

        exitButton.setBounds(950, 15, 50, 50);

        JButton randomColorButton = new JButton("R");
        randomColorButton.setBounds(610, 215, 60, 65);

        JButton blackColorButton = new JButton("B");
        blackColorButton.setBounds(550, 230, 50, 50);

        JButton whiteColorButton = new JButton("W");
        whiteColorButton.setBounds(680, 230, 50, 50);

        setBackground(Color.DARK_GRAY);
        setOpaque(true);
        setLayout(null);
        add(exitButton);
        add(selectColorTextField);
        add(whiteColorButton);
        add(blackColorButton);
        add(randomColorButton);
        add(createButton);
        add(variantTextField);
        add(timeTextField);
        add(modeField);
        add(variantC);
        add(modeC);
        add(timeC);
        setBounds(450, 100, 1020, 820);
    }
}
