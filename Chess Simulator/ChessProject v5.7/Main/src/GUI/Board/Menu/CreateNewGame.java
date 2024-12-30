package GUI.Board.Menu;

import GUI.Board.ChessClock;
import Game.GameEngine.ChessEngine;

import javax.swing.*;
import java.awt.*;

public class CreateNewGame extends JLabel {

    // Initializing the buttons.
    public JButton exitButton = new JButton("Exit");
    public JButton createButton = new JButton("Create The Game");

    // Initializing the frame and required fields for the ChessClock.
    private Frame frame;
    JLabel whiteLabel = new JLabel();
    JLabel blackLabel = new JLabel();


    // These values are default. If user wont change any values, this would be the profile of the game.
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

        // Declaring the text fields on the right of the combo boxes.
        // The textboxes are modified. You can find more options about them on Textfield.java.
        TextField modeField = new TextField("Mode", 50,50,150,80);
        TextField variantTextField = new TextField("Variant", 50,200,150,80);
        TextField timeTextField = new TextField("Time", 50,350,150,80);
        TextField selectColorTextField = new TextField("Color", 550,120,180,80);

        // Setting the combo box for variant selection.
        String[] variantText = {"Standart", "MergeChess", "ThreeChecks"};
        JComboBox<String> variantC = new JComboBox<String>(variantText);
        variantC.setToolTipText("Variant");
        variantC.setBounds(220, 200, 200, 80);
        variantC.setBorder(null);

        // Setting the combo box for the combo selection.
        String[] timeText = {"1", "2", "3", "5", "10", "15", "30", "90"};
        JComboBox<String> timeC = new JComboBox<String>(timeText);
        timeC.setToolTipText("Time");
        timeC.setBounds(220, 350, 200, 80);

        // Settting the combo box for the playing mode selection.
        String[] modeText = {"Online", "Offline", "vs. Stockfish"};
        JComboBox<String> modeC = new JComboBox<String>(modeText);
        modeC.setBounds(220, 50, 200, 80);

        // This is the button that creates the game in the pop up menu.
        createButton.setBounds(220, 550, 600, 100);
        createButton.addActionListener(e -> {

            // This is the logic that starts the game.
        
            // Before we assign values to the board, engine and Clock objects, we Take assign selected values to our fields.
            // So user can start a game to their liking.
            this.whiteTime = Integer.parseInt(timeC.getSelectedItem().toString()) * 60; // you select it as minutes in the menu
            this.blackTime = Integer.parseInt(timeC.getSelectedItem().toString()) * 60; // we need to convert it to seconds to use in the funciton.
            this.timeIncrement = 1;
            this.variant = variantC.getSelectedItem().toString();
            this.mode = modeC.getSelectedItem().toString();
            this.startingColor = startingColor;
            // Initializing the chess clock.
            this.chessClock = new ChessClock(whiteTime, blackTime, whiteLabel, blackLabel);

            // Clearing the open tabs from the Frame.
            this.frame.layeredPane.remove(this.frame.panel);
            this.frame.layeredPane.remove(this.frame.createNewGame);

            // Adding the board object to the layeredPane.
            // This will automatically draw the tiles and pieces.
            this.frame.layeredPane.add(engine.board);
            // 680x680 boyularinda bir board.
            // 1920 - 680 = 1240/2=620 1020 - 680 = 340 / 2 = 170
            // This is the exact size of the board. Other parts of the Frame will be used for another stuff.
            engine.board.setBounds(620, 170, 680, 680);

            // Adding the timer labels to the right side of the board.
            this.frame.layeredPane.add(whiteLabel);
            this.frame.layeredPane.add(blackLabel);

            // This is our fault switch. It stops both of the timers to solve any issue that may be on their way.
            chessClock.setTimers();

            // Giving engine our currently created clocks.
            engine.getClocks(chessClock);

            // Some code for revalidating the panel to see the changes that we made.
            this.frame.layeredPane.revalidate();
            this.frame.layeredPane.repaint();
            this.frame.revalidate();
            this.frame.repaint();


        });

        exitButton.setBounds(950, 15, 50, 50);

        // These buttons will set the values of startingColor.
        JButton randomColorButton = new JButton("R");
        randomColorButton.setBounds(610, 215, 60, 65);

        JButton blackColorButton = new JButton("B");
        blackColorButton.setBounds(550, 230, 50, 50);

        JButton whiteColorButton = new JButton("W");
        whiteColorButton.setBounds(680, 230, 50, 50);

        // Some Label settings.
        setBackground(Color.DARK_GRAY);
        setOpaque(true);
        setLayout(null);
        // Adding our fields to the JLabel. Which is this object
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
