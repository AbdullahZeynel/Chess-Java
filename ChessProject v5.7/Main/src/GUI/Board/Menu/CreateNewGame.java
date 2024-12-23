package GUI.Board.Menu;

import javax.swing.*;
import java.awt.*;

public class CreateNewGame extends JLabel {

    public JButton exitButton = new JButton("Exit");
    public JButton createButton = new JButton("Create The Game");

    public CreateNewGame() {

        TextField modeField = new TextField("Mode", 50,50,150,80);

        TextField variantTextField = new TextField("Variant", 50,200,150,80);

        TextField timeTextField = new TextField("Time", 50,350,150,80);

        TextField selectColorTextField = new TextField("Color", 550,120,180,80);

        String[] variant = {"Standart", "MergeChess", "ThreeChecks"};
        JComboBox<String> variantC = new JComboBox<String>(variant);
        variantC.setToolTipText("Variant");
        variantC.setBounds(220, 200, 200, 80);
        //variantC.setBackground(new Color(0xFF857676, true));
        variantC.setBorder(null);
        //variantC.setForeground(new Color(255, 255, 255));


        String[] time = {"1", "2", "3", "5", "10", "15", "30", "90"};
        JComboBox<String> timeC = new JComboBox<String>(time);
        timeC.setToolTipText("Time");
        timeC.setBounds(220, 350, 200, 80);

        String[] mode = {"Online", "Offline", "vs. Stockfish"};
        JComboBox<String> modeC = new JComboBox<String>(mode);
        modeC.setToolTipText("Time Increase");
        modeC.setBounds(220, 50, 200, 80);


        createButton.setBounds(220, 550,600,100);


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
