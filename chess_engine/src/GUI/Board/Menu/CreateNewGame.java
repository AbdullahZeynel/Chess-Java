package GUI.Board.Menu;

import GUI.Board.ChessClock;
import Game.GameEngine.ChessEngine;
import resources.Variables;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class CreateNewGame extends JLabel {

    // Initializing the buttons.
    public JButton exitButton;
    public JButton createButton;

    // Initializing the frame and required fields for the ChessClock.
    private Frame frame;
    private ChessEngine engine;
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

    public CreateNewGame(Frame frame, ChessEngine engine) {
        this.frame = frame;
        this.engine = engine;

        setLayout(null);
        setOpaque(false);

        // Override painting for rounded card with shadow
        // We'll draw in paintComponent

        // Declaring the text fields on the right of the combo boxes.
        TextField modeField = new TextField("Mode", 50, 80, 150, 50);
        modeField.setForeground(Variables.frameAccentColor);
        modeField.setFont(new Font("SansSerif", Font.BOLD, 16));

        TextField variantTextField = new TextField("Variant", 50, 190, 150, 50);
        variantTextField.setForeground(Variables.frameAccentColor);
        variantTextField.setFont(new Font("SansSerif", Font.BOLD, 16));

        TextField timeTextField = new TextField("Time", 50, 300, 150, 50);
        timeTextField.setForeground(Variables.frameAccentColor);
        timeTextField.setFont(new Font("SansSerif", Font.BOLD, 16));

        TextField selectColorTextField = new TextField("Color", 500, 80, 150, 50);
        selectColorTextField.setForeground(Variables.frameAccentColor);
        selectColorTextField.setFont(new Font("SansSerif", Font.BOLD, 16));

        // Setting the combo box for variant selection.
        String[] variantText = {"Standart", "MergeChess", "ThreeChecks"};
        JComboBox<String> variantC = createStyledComboBox(variantText);
        variantC.setBounds(220, 190, 220, 50);

        // Setting the combo box for time selection.
        String[] timeText = {"1", "2", "3", "5", "10", "15", "30", "90"};
        JComboBox<String> timeC = createStyledComboBox(timeText);
        timeC.setBounds(220, 300, 220, 50);

        // Setting the combo box for the playing mode selection.
        String[] modeText = {"Online", "Offline", "vs. Stockfish"};
        JComboBox<String> modeC = createStyledComboBox(modeText);
        modeC.setBounds(220, 80, 220, 50);

        // Create Game button
        createButton = createStyledButton("Create The Game", Variables.buttonPrimaryColor, Variables.buttonPrimaryHoverColor);
        createButton.setBounds(50, 500, 600, 70);
        createButton.addActionListener(e -> {

            this.whiteTime = Integer.parseInt(timeC.getSelectedItem().toString()) * 60;
            this.blackTime = Integer.parseInt(timeC.getSelectedItem().toString()) * 60;
            this.timeIncrement = 1;
            this.variant = variantC.getSelectedItem().toString();
            this.mode = modeC.getSelectedItem().toString();
            this.startingColor = startingColor;
            this.chessClock = new ChessClock(whiteTime, blackTime, whiteLabel, blackLabel);

            this.frame.layeredPane.remove(this.frame.panel);
            this.frame.layeredPane.remove(this.frame.createNewGame);

            this.frame.layeredPane.add(engine.board);
            int boardSize = Variables.cols * Variables.tileSize;
            int bx = (frame.getWidth() - boardSize) / 2 - 80;
            int by = (frame.getHeight() - boardSize) / 2 - 20;
            engine.board.setBounds(bx, by, boardSize, boardSize);

            this.frame.layeredPane.add(whiteLabel);
            this.frame.layeredPane.add(blackLabel);

            whiteLabel.setBounds(bx + boardSize + 30, by + boardSize - 100, 200, 70);
            blackLabel.setBounds(bx + boardSize + 30, by + 30, 200, 70);

            chessClock.setTimers();
            engine.getClocks(chessClock);

            this.frame.layeredPane.revalidate();
            this.frame.layeredPane.repaint();
            this.frame.revalidate();
            this.frame.repaint();
        });

        // Exit button - styled as icon
        exitButton = createStyledButton("\u2715", Variables.buttonSecondaryColor, new Color(200, 60, 60));
        exitButton.setBounds(830, 15, 45, 45);
        exitButton.setFont(new Font("SansSerif", Font.BOLD, 20));

        // Color buttons
        JButton whiteColorButton = createStyledButton("W", Variables.buttonSecondaryColor, Variables.buttonSecondaryHoverColor);
        whiteColorButton.setBounds(510, 150, 55, 55);
        whiteColorButton.addActionListener(e -> startingColor = "white");

        JButton randomColorButton = createStyledButton("R", Variables.buttonSecondaryColor, Variables.buttonSecondaryHoverColor);
        randomColorButton.setBounds(575, 145, 65, 65);
        randomColorButton.addActionListener(e -> startingColor = Math.random() > 0.5 ? "white" : "black");

        JButton blackColorButton = createStyledButton("B", Variables.buttonSecondaryColor, Variables.buttonSecondaryHoverColor);
        blackColorButton.setBounds(650, 150, 55, 55);
        blackColorButton.addActionListener(e -> startingColor = "black");

        // Title for popup
        JLabel titleLabel = new JLabel("New Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(Variables.frameTextColor);
        titleLabel.setBounds(0, 15, 830, 45);

        // Adding components
        add(titleLabel);
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
        setBounds(450, 100, 900, 700);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Shadow
        g2d.setColor(new Color(0, 0, 0, 80));
        g2d.fill(new RoundRectangle2D.Double(6, 6, getWidth() - 6, getHeight() - 6, 24, 24));

        // Background
        g2d.setColor(Variables.framePanelColor);
        g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 6, getHeight() - 6, 24, 24));

        // Border
        g2d.setColor(Variables.frameBorderColor);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 6, getHeight() - 6, 24, 24));

        super.paintComponent(g);
    }

    private JButton createStyledButton(String text, Color normalColor, Color hoverColor) {
        JButton btn = new JButton(text) {
            boolean hovered = false;

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                    @Override
                    public void mouseExited(MouseEvent e) { hovered = false; repaint(); }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color bg = hovered ? hoverColor : normalColor;
                g2d.setColor(bg);
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 14, 14));

                g2d.setColor(Variables.buttonTextColor);
                FontMetrics fm = g2d.getFontMetrics(getFont());
                g2d.setFont(getFont());
                g2d.drawString(getText(),
                    (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setBackground(Variables.buttonSecondaryColor);
        combo.setForeground(Variables.frameTextColor);
        combo.setFont(new Font("SansSerif", Font.PLAIN, 15));
        combo.setBorder(BorderFactory.createLineBorder(Variables.frameBorderColor));
        combo.setFocusable(false);
        return combo;
    }
}
