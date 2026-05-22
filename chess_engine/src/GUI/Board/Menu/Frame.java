package GUI.Board.Menu;

import GUI.Board.ChessClock;
import Game.GameEngine.ChessEngine;
import Game.GameEngine.ThreeChecksChess;
import resources.Variables;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class Frame extends JFrame {

    // Declaring the required fields.
    public JLayeredPane layeredPane = new JLayeredPane();
    public JPanel panel;
    public CreateNewGame createNewGame;
    private ChessEngine engine;
    JLabel whiteLabel = new JLabel();
    JLabel blackLabel = new JLabel();
    ChessClock chessClock;

    public int whiteTime = 60;
    public int blackTime = 60;
    public int timeIncrement = 0;

    public String variant = "standard";
    public String mode = "offline";
    public String startingColor = "white";

    public Frame(ChessEngine engine) {
        this.engine = engine;

        // Creating a pop up menu Label. It needs a Frame parameter so well give it this.
        createNewGame = new CreateNewGame(this, engine);

        // The panel will be our background panel with custom painting
        panel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

                // Gradient background
                GradientPaint gp = new GradientPaint(0, 0, Variables.frameBackGroundColor,
                        getWidth(), getHeight(), new Color(35, 32, 28));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Draw title
                g2d.setFont(new Font("Serif", Font.BOLD, 52));
                String title = "\u265A  Gazi Chess";
                FontMetrics fm = g2d.getFontMetrics();
                int titleX = (getWidth() - fm.stringWidth(title)) / 2;
                int titleY = 100;

                // Title shadow
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.drawString(title, titleX + 2, titleY + 2);
                // Title main
                g2d.setColor(Variables.frameAccentColor);
                g2d.drawString(title, titleX, titleY);

                // Subtitle
                g2d.setFont(new Font("SansSerif", Font.PLAIN, 16));
                String subtitle = "Select a game mode to start playing";
                fm = g2d.getFontMetrics();
                g2d.setColor(Variables.frameSubTextColor);
                g2d.drawString(subtitle, (getWidth() - fm.stringWidth(subtitle)) / 2, 135);

                // Draw category cards
                drawCategoryCard(g2d, "Standard Chess", "\u265E", getCategoryX(getWidth(), 0), 165, getCategoryWidth(getWidth()));
                drawCategoryCard(g2d, "Merge Chess", "\u265D", getCategoryX(getWidth(), 1), 165, getCategoryWidth(getWidth()));
                drawCategoryCard(g2d, "Three Checks", "\u265A", getCategoryX(getWidth(), 2), 165, getCategoryWidth(getWidth()));
            }

            private int getCategoryWidth(int totalWidth) {
                return Math.min(280, (totalWidth - 200) / 3);
            }

            private int getCategoryX(int totalWidth, int index) {
                int cardW = getCategoryWidth(totalWidth);
                int totalCards = 3 * cardW + 2 * 30; // 30px gaps
                int startX = (totalWidth - totalCards) / 2;
                return startX + index * (cardW + 30);
            }

            private void drawCategoryCard(Graphics2D g2d, String title, String icon, int x, int y, int w) {
                int h = 320;
                // Card background with rounded corners
                g2d.setColor(Variables.framePanelColor);
                g2d.fill(new RoundRectangle2D.Double(x, y, w, h, 20, 20));

                // Card border
                g2d.setColor(Variables.frameBorderColor);
                g2d.setStroke(new BasicStroke(1));
                g2d.draw(new RoundRectangle2D.Double(x, y, w, h, 20, 20));

                // Icon
                g2d.setFont(new Font("Serif", Font.PLAIN, 42));
                FontMetrics fm = g2d.getFontMetrics();
                g2d.setColor(Variables.frameAccentColor);
                g2d.drawString(icon, x + (w - fm.stringWidth(icon)) / 2, y + 55);

                // Card title
                g2d.setFont(new Font("SansSerif", Font.BOLD, 16));
                fm = g2d.getFontMetrics();
                g2d.setColor(Variables.frameTextColor);
                g2d.drawString(title, x + (w - fm.stringWidth(title)) / 2, y + 85);

                // Divider line
                g2d.setColor(Variables.frameBorderColor);
                g2d.drawLine(x + 20, y + 100, x + w - 20, y + 100);
            }
        };
        panel.setBounds(0, 0, 1920, 1080);

        // ─── Standard Chess Buttons ─────────────────────────
        JButton standartSelect1 = createTimeButton("3+1", "Blitz");
        standartSelect1.addActionListener(e -> {
            this.whiteTime = 180; this.blackTime = 180; this.timeIncrement = 1;
            this.variant = "standard"; this.mode = "offline"; this.startingColor = "white";
            setGame(whiteTime, blackTime, timeIncrement, variant, mode, startingColor);
        });

        JButton standartSelect2 = createTimeButton("5+1", "Rapid");
        standartSelect2.addActionListener(e -> {
            this.whiteTime = 300; this.blackTime = 300; this.timeIncrement = 1;
            this.variant = "standard"; this.mode = "offline"; this.startingColor = "white";
            setGame(whiteTime, blackTime, timeIncrement, variant, mode, startingColor);
        });

        JButton standartSelect3 = createTimeButton("15+3", "Classical");
        standartSelect3.addActionListener(e -> {
            this.whiteTime = 900; this.blackTime = 900; this.timeIncrement = 3;
            this.variant = "standard"; this.mode = "offline"; this.startingColor = "white";
            setGame(whiteTime, blackTime, timeIncrement, variant, mode, startingColor);
        });

        // ─── Merge Chess Buttons ─────────────────────────
        JButton mergeChessSelect1 = createTimeButton("3+1", "Blitz");
        mergeChessSelect1.addActionListener(e -> {
            this.whiteTime = 180; this.blackTime = 180; this.timeIncrement = 1;
            this.variant = "merge"; this.mode = "offline"; this.startingColor = "white";
            setGame(whiteTime, blackTime, timeIncrement, variant, mode, startingColor);
        });

        JButton mergeChessSelect2 = createTimeButton("5+1", "Rapid");
        mergeChessSelect2.addActionListener(e -> {
            this.whiteTime = 300; this.blackTime = 300; this.timeIncrement = 1;
            this.variant = "merge"; this.mode = "offline"; this.startingColor = "white";
            setGame(whiteTime, blackTime, timeIncrement, variant, mode, startingColor);
        });

        JButton mergeChessSelect3 = createTimeButton("15+3", "Classical");
        mergeChessSelect3.addActionListener(e -> {
            this.whiteTime = 900; this.blackTime = 900; this.timeIncrement = 3;
            this.variant = "merge"; this.mode = "offline"; this.startingColor = "white";
            setGame(whiteTime, blackTime, timeIncrement, variant, mode, startingColor);
        });

        // ─── Three Checks Buttons ─────────────────────────
        JButton threeChecksSelect1 = createTimeButton("3+1", "Blitz");
        threeChecksSelect1.addActionListener(e -> {
            this.whiteTime = 180; this.blackTime = 180; this.timeIncrement = 1;
            this.variant = "threeChecks"; this.mode = "offline"; this.startingColor = "white";
            setGame(whiteTime, blackTime, timeIncrement, variant, mode, startingColor);
        });

        JButton threeChecksSelect2 = createTimeButton("5+1", "Rapid");
        threeChecksSelect2.addActionListener(e -> {
            this.whiteTime = 300; this.blackTime = 300; this.timeIncrement = 1;
            this.variant = "threeChecks"; this.mode = "offline"; this.startingColor = "white";
            setGame(whiteTime, blackTime, timeIncrement, variant, mode, startingColor);
        });

        JButton threeChecksSelect3 = createTimeButton("15+3", "Classical");
        threeChecksSelect3.addActionListener(e -> {
            this.whiteTime = 900; this.blackTime = 900; this.timeIncrement = 3;
            this.variant = "threeChecks"; this.mode = "offline"; this.startingColor = "white";
            setGame(whiteTime, blackTime, timeIncrement, variant, mode, startingColor);
        });

        // We'll use a ComponentListener to reposition buttons dynamically
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                repositionButtons(panel, new JButton[]{standartSelect1, standartSelect2, standartSelect3},
                        new JButton[]{mergeChessSelect1, mergeChessSelect2, mergeChessSelect3},
                        new JButton[]{threeChecksSelect1, threeChecksSelect2, threeChecksSelect3});
            }
        });

        panel.add(standartSelect1);
        panel.add(standartSelect2);
        panel.add(standartSelect3);
        panel.add(mergeChessSelect1);
        panel.add(mergeChessSelect2);
        panel.add(mergeChessSelect3);
        panel.add(threeChecksSelect1);
        panel.add(threeChecksSelect2);
        panel.add(threeChecksSelect3);

        // ─── Create New Game Button ─────────────────────────
        JButton button = createPrimaryButton("Create a New Game");
        button.addActionListener(e -> {
            if (e.getSource() == button) {
                createNewGame.setBounds(
                    (getWidth() - 900) / 2,
                    (getHeight() - 700) / 2,
                    900, 700
                );
                layeredPane.add(createNewGame, JLayeredPane.POPUP_LAYER);
                layeredPane.revalidate();
                layeredPane.repaint();
                revalidate();
                repaint();
            }
        });
        panel.add(button);

        createNewGame.exitButton.addActionListener(e -> {
            layeredPane.remove(createNewGame);
            layeredPane.revalidate();
            layeredPane.repaint();
        });

        // Setting the Frame options.
        setTitle("Gazi Chess Engine");
        setMinimumSize(new Dimension(1000, 750));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 800);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Variables.frameBackGroundColor);

        // Adding the panel to the default layer.
        layeredPane.add(panel, JLayeredPane.DEFAULT_LAYER);
        add(layeredPane);

        // Make layeredPane fill frame
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                layeredPane.setSize(getContentPane().getSize());
                panel.setSize(getContentPane().getSize());
                panel.repaint();
            }
        });

        setVisible(true);

        // Trigger initial layout
        SwingUtilities.invokeLater(() -> {
            layeredPane.setSize(getContentPane().getSize());
            panel.setSize(getContentPane().getSize());
            panel.repaint();
        });
    }

    private void repositionButtons(JPanel panel, JButton[] std, JButton[] merge, JButton[] threeC) {
        int w = panel.getWidth();
        int cardW = Math.min(280, (w - 200) / 3);
        int totalCards = 3 * cardW + 2 * 30;
        int startX = (w - totalCards) / 2;

        int btnW = cardW - 40;
        int btnH = 50;
        int btnY0 = 285; // starting y for first button inside card
        int gap = 60;

        for (int cat = 0; cat < 3; cat++) {
            int cx = startX + cat * (cardW + 30) + 20;
            JButton[] btns = cat == 0 ? std : (cat == 1 ? merge : threeC);
            for (int i = 0; i < 3; i++) {
                btns[i].setBounds(cx, btnY0 + i * gap, btnW, btnH);
            }
        }

        // Create New Game button
        Component[] comps = panel.getComponents();
        for (Component c : comps) {
            if (c instanceof JButton) {
                JButton b = (JButton) c;
                if ("Create a New Game".equals(b.getText())) {
                    int bw = 320;
                    int bh = 55;
                    b.setBounds((w - bw) / 2, 530, bw, bh);
                }
            }
        }
    }

    /// Creates a styled time-control button
    private JButton createTimeButton(String time, String label) {
        JButton btn = new JButton(time) {
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

                Color bg = hovered ? Variables.buttonSecondaryHoverColor : Variables.buttonSecondaryColor;
                g2d.setColor(bg);
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 12, 12));

                if (hovered) {
                    g2d.setColor(Variables.frameAccentColor);
                    g2d.setStroke(new BasicStroke(1.5f));
                    g2d.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 12, 12));
                }

                // Time text
                g2d.setFont(new Font("SansSerif", Font.BOLD, 18));
                FontMetrics fm = g2d.getFontMetrics();
                g2d.setColor(hovered ? Variables.buttonAccentTextColor : Variables.buttonTextColor);
                g2d.drawString(time, (getWidth() - fm.stringWidth(time)) / 2, getHeight() / 2 - 2);

                // Label text
                g2d.setFont(new Font("SansSerif", Font.PLAIN, 11));
                fm = g2d.getFontMetrics();
                g2d.setColor(Variables.frameSubTextColor);
                g2d.drawString(label, (getWidth() - fm.stringWidth(label)) / 2, getHeight() / 2 + 15);
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /// Creates a styled primary action button
    private JButton createPrimaryButton(String text) {
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

                Color bg = hovered ? Variables.buttonPrimaryHoverColor : Variables.buttonPrimaryColor;
                GradientPaint gp = new GradientPaint(0, 0, bg, 0, getHeight(), bg.darker());
                g2d.setPaint(gp);
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 16, 16));

                g2d.setFont(new Font("SansSerif", Font.BOLD, 18));
                FontMetrics fm = g2d.getFontMetrics();
                g2d.setColor(Variables.buttonTextColor);
                g2d.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void setGame(int whiteTime, int blackTime, int timeIncrement, String variant, String mode, String startingColor) {

        // ─── Variant-based engine creation ─────────────────────────
        // Create the appropriate engine type based on the selected variant.
        // Three Checks gets its own engine subclass with check counting.
        if (variant.equals("threeChecks")) {
            this.engine = new ThreeChecksChess();
        } else {
            this.engine = new ChessEngine();
        }
        this.createNewGame = new CreateNewGame(this, engine);

        // Initializing the chess clock
        this.chessClock = new ChessClock(whiteTime, blackTime, whiteLabel, blackLabel);

        // Removing the contents of the layered pane
        this.layeredPane.remove(this.panel);
        this.layeredPane.remove(this.createNewGame);

        // Adding the board
        this.layeredPane.add(engine.board);

        // Setting the board bounds - centered
        int boardSize = Variables.cols * Variables.tileSize;
        int bx = (getWidth() - boardSize) / 2 - 80;
        int by = (getHeight() - boardSize) / 2 - 20;
        engine.board.setBounds(bx, by, boardSize, boardSize);

        // Adding the chessClock labels
        this.layeredPane.add(whiteLabel);
        this.layeredPane.add(blackLabel);

        // Position clock labels beside board
        whiteLabel.setBounds(bx + boardSize + 30, by + boardSize - 100, 200, 90);
        blackLabel.setBounds(bx + boardSize + 30, by + 10, 200, 90);

        // ─── Back to Menu button ─────────────────────────
        JButton backButton = createPrimaryButton("\u2190 Menu");
        backButton.setBounds(bx + boardSize + 30, by + boardSize / 2 - 25, 160, 45);
        backButton.addActionListener(e -> {
            // Return to main menu
            layeredPane.removeAll();
            layeredPane.add(panel, JLayeredPane.DEFAULT_LAYER);
            setTitle("Gazi Chess Engine");
            chessClock.setTimers();
            // Reset engine for new game
            engine = new ChessEngine();
            createNewGame = new CreateNewGame(this, engine);
            layeredPane.setSize(getContentPane().getSize());
            panel.setSize(getContentPane().getSize());
            layeredPane.revalidate();
            layeredPane.repaint();
        });
        this.layeredPane.add(backButton);

        // ─── Three Checks indicator labels ─────────────────────────
        if (engine instanceof ThreeChecksChess) {
            ThreeChecksChess tcEngine = (ThreeChecksChess) engine;

            JLabel whiteCheckInd = createCheckIndicatorLabel(tcEngine, true);
            whiteCheckInd.setBounds(bx + boardSize + 30, by + boardSize - 10, 200, 30);
            this.layeredPane.add(whiteCheckInd);
            tcEngine.whiteCheckIndicator = whiteCheckInd;

            JLabel blackCheckInd = createCheckIndicatorLabel(tcEngine, false);
            blackCheckInd.setBounds(bx + boardSize + 30, by + 100, 200, 30);
            this.layeredPane.add(blackCheckInd);
            tcEngine.blackCheckIndicator = blackCheckInd;
        }

        // Stop timers before starting fresh
        this.chessClock.setTimers();

        // Pass time increment and clock to the engine
        engine.setTimeIncrement(timeIncrement);
        engine.getClocks(this.chessClock);

        // Update window title with game info
        String timeLabel = (whiteTime / 60) + "+" + timeIncrement;
        setTitle("Gazi Chess \u2014 " + timeLabel + " " + variant);

        // Set dark background for board area
        getContentPane().setBackground(Variables.frameBackGroundColor);

        // Revalidate and repaint
        this.layeredPane.revalidate();
        this.layeredPane.repaint();
        this.revalidate();
        this.repaint();
    }

    /// Creates a custom-painted JLabel that displays check count dots for Three Checks variant.
    /// The label reads the check count from the engine on every repaint.
    private JLabel createCheckIndicatorLabel(ThreeChecksChess tcEngine, boolean isWhiteSide) {
        return new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int checks = isWhiteSide ? tcEngine.whiteChecksReceived : tcEngine.blackChecksReceived;
                int dotR = 12;
                int spacing = 22;
                int startX = 0;
                int y = (getHeight() - dotR) / 2;

                Color filledColor  = new Color(220, 50, 50);
                Color emptyColor   = new Color(70, 68, 64);
                Color outlineColor = new Color(50, 48, 44);

                for (int i = 0; i < 3; i++) {
                    g2d.setColor(i < checks ? filledColor : emptyColor);
                    g2d.fillOval(startX + i * spacing, y, dotR, dotR);
                    g2d.setColor(outlineColor);
                    g2d.drawOval(startX + i * spacing, y, dotR, dotR);
                }

                // "Checks" label
                g2d.setColor(Variables.frameSubTextColor);
                g2d.setFont(new Font("SansSerif", Font.PLAIN, 11));
                g2d.drawString("Checks", startX + 3 * spacing + 5, y + dotR - 2);
            }
        };
    }
}
