package GUI.Board.Menu;

import GUI.Board.ChessClock;
import Game.GameEngine.ChessEngine;
import Game.GameEngine.ThreeChecksChess;
import resources.GaziTheme;
import resources.Theme;
import resources.Variables;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Main application frame for Gazi Chess.
 * Manages the menu screen, game board, and modal dialogs.
 */
public class Frame extends JFrame {

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

    /// Tracks whether dark theme is active
    private boolean isDarkTheme = true;

    /// Persistent toolbar panel — always visible at top-left
    private JPanel toolbarPanel;

    /// Modal backdrop — used to block background clicks
    private JPanel modalBackdrop;

    public Frame(ChessEngine engine) {
        this.engine = engine;
        createNewGame = new CreateNewGame(this, engine);

        // ─── Background panel with custom painting ───────────
        panel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

                GradientPaint gp = new GradientPaint(0, 0, Variables.frameBackGroundColor,
                        getWidth(), getHeight(), Variables.frameGradientEndColor);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Title
                g2d.setFont(new Font("SansSerif", Font.BOLD, 48));
                String title = "Gazi Chess";
                FontMetrics fm = g2d.getFontMetrics();
                int titleX = (getWidth() - fm.stringWidth(title)) / 2;
                int titleY = 95;

                g2d.setColor(new Color(0, 0, 0, 40));
                g2d.drawString(title, titleX + 1, titleY + 1);
                g2d.setColor(Variables.frameTextColor);
                g2d.drawString(title, titleX, titleY);

                // Subtitle
                g2d.setFont(new Font("SansSerif", Font.PLAIN, 15));
                String subtitle = "Select a game mode to start playing";
                fm = g2d.getFontMetrics();
                g2d.setColor(Variables.frameSubTextColor);
                g2d.drawString(subtitle, (getWidth() - fm.stringWidth(subtitle)) / 2, 125);

                // Category cards
                drawCategoryCard(g2d, "Standard Chess", getCategoryX(getWidth(), 0), 155, getCategoryWidth(getWidth()));
                drawCategoryCard(g2d, "Three Checks", getCategoryX(getWidth(), 1), 155, getCategoryWidth(getWidth()));
            }

            private int getCategoryWidth(int totalWidth) {
                return Math.min(320, (totalWidth - 160) / 2);
            }

            private int getCategoryX(int totalWidth, int index) {
                int cardW = getCategoryWidth(totalWidth);
                int totalCards = 2 * cardW + 40;
                int startX = (totalWidth - totalCards) / 2;
                return startX + index * (cardW + 40);
            }

            private void drawCategoryCard(Graphics2D g2d, String title, int x, int y, int w) {
                int h = 320;
                g2d.setColor(Variables.framePanelColor);
                g2d.fill(new RoundRectangle2D.Double(x, y, w, h, 16, 16));
                g2d.setColor(Variables.frameBorderColor);
                g2d.setStroke(new BasicStroke(1));
                g2d.draw(new RoundRectangle2D.Double(x, y, w, h, 16, 16));

                g2d.setFont(new Font("SansSerif", Font.BOLD, 17));
                FontMetrics fm = g2d.getFontMetrics();
                g2d.setColor(Variables.frameTextColor);
                g2d.drawString(title, x + (w - fm.stringWidth(title)) / 2, y + 40);

                g2d.setColor(Variables.frameBorderColor);
                g2d.drawLine(x + 20, y + 58, x + w - 20, y + 58);
            }
        };
        panel.setBounds(0, 0, 1920, 1080);

        // ─── Standard Chess Buttons ─────────────────────────
        JButton stdBtn1 = createTimeButton("3+1", "Blitz");
        stdBtn1.addActionListener(e -> startQuickGame(180, 1, "standard"));

        JButton stdBtn2 = createTimeButton("5+1", "Rapid");
        stdBtn2.addActionListener(e -> startQuickGame(300, 1, "standard"));

        JButton stdBtn3 = createTimeButton("15+3", "Classical");
        stdBtn3.addActionListener(e -> startQuickGame(900, 3, "standard"));

        // ─── Three Checks Buttons ─────────────────────────
        JButton tcBtn1 = createTimeButton("3+1", "Blitz");
        tcBtn1.addActionListener(e -> startQuickGame(180, 1, "threeChecks"));

        JButton tcBtn2 = createTimeButton("5+1", "Rapid");
        tcBtn2.addActionListener(e -> startQuickGame(300, 1, "threeChecks"));

        JButton tcBtn3 = createTimeButton("15+3", "Classical");
        tcBtn3.addActionListener(e -> startQuickGame(900, 3, "threeChecks"));

        // Reposition buttons dynamically
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                repositionButtons(panel, new JButton[]{stdBtn1, stdBtn2, stdBtn3},
                        new JButton[]{tcBtn1, tcBtn2, tcBtn3});
            }
        });

        panel.add(stdBtn1);
        panel.add(stdBtn2);
        panel.add(stdBtn3);
        panel.add(tcBtn1);
        panel.add(tcBtn2);
        panel.add(tcBtn3);

        // ─── Modal Backdrop (blocks background clicks) ───────────
        modalBackdrop = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        modalBackdrop.setOpaque(false);
        modalBackdrop.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { e.consume(); }
            @Override public void mousePressed(MouseEvent e) { e.consume(); }
            @Override public void mouseReleased(MouseEvent e) { e.consume(); }
        });

        // ─── Create New Game Button ─────────────────────────
        JButton newGameButton = createPrimaryButton("Create a New Game");
        newGameButton.addActionListener(e -> openNewGameModal());
        panel.add(newGameButton);

        // Exit button closes the modal
        createNewGame.exitButton.addActionListener(e -> closeNewGameModal());

        // ─── Persistent Toolbar (PALETTE_LAYER — always on top) ─────
        toolbarPanel = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) { /* transparent */ }
        };
        toolbarPanel.setOpaque(false);
        toolbarPanel.setBounds(15, 15, 100, 40);

        JButton themeToggle = createSmallToolbarButton(true);
        themeToggle.setBounds(0, 0, 40, 40);
        toolbarPanel.add(themeToggle);

        JButton langToggle = createSmallToolbarButton(false);
        langToggle.setBounds(48, 0, 40, 40);
        toolbarPanel.add(langToggle);

        layeredPane.add(toolbarPanel, JLayeredPane.PALETTE_LAYER);

        // Frame setup
        setTitle("Gazi Chess Engine");
        setMinimumSize(new Dimension(1000, 750));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 800);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Variables.frameBackGroundColor);

        layeredPane.add(panel, JLayeredPane.DEFAULT_LAYER);
        add(layeredPane);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                layeredPane.setSize(getContentPane().getSize());
                panel.setSize(getContentPane().getSize());
                panel.repaint();
            }
        });

        setVisible(true);

        SwingUtilities.invokeLater(() -> {
            layeredPane.setSize(getContentPane().getSize());
            panel.setSize(getContentPane().getSize());
            panel.repaint();
        });
    }

    // ════════════════════════════════════════════════════════════
    // ─── Public API for modal management ───────────────────────
    // ════════════════════════════════════════════════════════════

    /// Opens the "Create New Game" modal dialog with backdrop
    private void openNewGameModal() {
        modalBackdrop.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.add(modalBackdrop, JLayeredPane.MODAL_LAYER);

        createNewGame.setBounds(
            (getWidth() - 880) / 2,
            (getHeight() - 480) / 2,
            880, 480
        );
        layeredPane.add(createNewGame, JLayeredPane.POPUP_LAYER);
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    /// Closes the "Create New Game" modal and removes the backdrop
    public void closeNewGameModal() {
        layeredPane.remove(createNewGame);
        layeredPane.remove(modalBackdrop);
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    /// Quick game start from the time-control buttons
    private void startQuickGame(int timeSecs, int increment, String variant) {
        setGame(timeSecs, timeSecs, increment, variant, "offline", "white");
    }

    /// Called by CreateNewGame to start a game from the modal
    public void startGameFromModal(int whiteTime, int blackTime, int increment, String variant, String mode, String color) {
        setGame(whiteTime, blackTime, increment, variant, mode, color);
    }

    // ════════════════════════════════════════════════════════════
    // ─── Private helpers ───────────────────────────────────────
    // ════════════════════════════════════════════════════════════

    /// Repositions time-control buttons inside the 2 category cards
    private void repositionButtons(JPanel panel, JButton[] std, JButton[] threeC) {
        int w = panel.getWidth();
        int cardW = Math.max(200, Math.min(320, (w - 160) / 2));
        int totalCards = 2 * cardW + 40;
        int startX = (w - totalCards) / 2;

        int btnW = Math.max(100, cardW - 40);
        int btnH = 48;
        int btnY0 = 230;
        int gap = 58;

        for (int cat = 0; cat < 2; cat++) {
            int cx = startX + cat * (cardW + 40) + 20;
            JButton[] btns = cat == 0 ? std : threeC;
            for (int i = 0; i < 3; i++) {
                btns[i].setBounds(cx, btnY0 + i * gap, btnW, btnH);
            }
        }

        // Reposition "Create a New Game" button
        for (Component c : panel.getComponents()) {
            if (c instanceof JButton && "Create a New Game".equals(((JButton) c).getText())) {
                int bw = Math.min(300, w - 100);
                c.setBounds((w - bw) / 2, 520, bw, 50);
            }
        }
    }

    /// Creates a small toolbar button (theme toggle or language toggle)
    private JButton createSmallToolbarButton(boolean isThemeButton) {
        JButton btn = new JButton() {
            boolean hovered = false;

            {
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                    @Override public void mouseExited(MouseEvent e) { hovered = false; repaint(); }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color bg = hovered ? Variables.buttonSecondaryHoverColor : Variables.buttonSecondaryColor;
                g2d.setColor(bg);
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));

                if (hovered) {
                    g2d.setColor(Variables.frameBorderColor);
                    g2d.setStroke(new BasicStroke(1));
                    g2d.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));
                }

                String label = isThemeButton ? (isDarkTheme ? "\u2600" : "\u263E") : "TR";

                g2d.setFont(new Font("SansSerif", Font.BOLD, isThemeButton ? 18 : 13));
                FontMetrics fm = g2d.getFontMetrics();
                g2d.setColor(hovered ? Variables.frameAccentColor : Variables.frameSubTextColor);
                g2d.drawString(label, (getWidth() - fm.stringWidth(label)) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        if (isThemeButton) {
            btn.setToolTipText("Toggle Light/Dark Theme");
            btn.addActionListener(e -> toggleTheme());
        } else {
            btn.setToolTipText("Toggle Language (TR/EN)");
            // TODO: Language toggle — planned for v6.2.6
        }

        return btn;
    }

    /// Toggle between dark and light theme
    private void toggleTheme() {
        isDarkTheme = !isDarkTheme;
        Theme.setTheme(isDarkTheme ? GaziTheme.DARK : GaziTheme.LIGHT);
        getContentPane().setBackground(Variables.frameBackGroundColor);

        // Recreate createNewGame so its JTextFields/JPopupMenus pick up new theme colors
        createNewGame = new CreateNewGame(this, engine);
        createNewGame.exitButton.addActionListener(e -> closeNewGameModal());

        // Refresh chess clock if in-game
        if (chessClock != null) {
            chessClock.refreshDisplay();
        }

        repaintAllComponents(layeredPane);
        repaint();
    }

    /// Recursively repaint all components
    private void repaintAllComponents(Container container) {
        for (Component c : container.getComponents()) {
            c.repaint();
            if (c instanceof Container) {
                repaintAllComponents((Container) c);
            }
        }
    }

    /// Creates a styled time-control button
    private JButton createTimeButton(String time, String label) {
        JButton btn = new JButton(time) {
            boolean hovered = false;

            {
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                    @Override public void mouseExited(MouseEvent e) { hovered = false; repaint(); }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color bg = hovered ? Variables.buttonSecondaryHoverColor : Variables.buttonSecondaryColor;
                g2d.setColor(bg);
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));

                if (hovered) {
                    g2d.setColor(Variables.frameAccentColor);
                    g2d.setStroke(new BasicStroke(1.5f));
                    g2d.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));
                }

                g2d.setFont(new Font("SansSerif", Font.BOLD, 17));
                FontMetrics fm = g2d.getFontMetrics();
                g2d.setColor(hovered ? Variables.buttonAccentTextColor : Variables.buttonSecondaryTextColor);
                g2d.drawString(time, (getWidth() - fm.stringWidth(time)) / 2, getHeight() / 2 - 1);

                g2d.setFont(new Font("SansSerif", Font.PLAIN, 11));
                fm = g2d.getFontMetrics();
                g2d.setColor(Variables.frameSubTextColor);
                g2d.drawString(label, (getWidth() - fm.stringWidth(label)) / 2, getHeight() / 2 + 14);
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /// Creates a styled primary action button
    JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text) {
            boolean hovered = false;

            {
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                    @Override public void mouseExited(MouseEvent e) { hovered = false; repaint(); }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color bg = hovered ? Variables.buttonPrimaryHoverColor : Variables.buttonPrimaryColor;
                g2d.setColor(bg);
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 14, 14));

                g2d.setFont(new Font("SansSerif", Font.BOLD, 16));
                FontMetrics fm = g2d.getFontMetrics();
                g2d.setColor(Variables.buttonTextColor);
                g2d.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /// Core game setup — removes menu, adds board + clock + controls
    private void setGame(int whiteTime, int blackTime, int timeIncrement, String variant, String mode, String startingColor) {
        // Create engine based on variant
        if (variant.equals("threeChecks")) {
            this.engine = new ThreeChecksChess();
        } else {
            this.engine = new ChessEngine();
        }
        this.createNewGame = new CreateNewGame(this, engine);
        this.createNewGame.exitButton.addActionListener(e -> closeNewGameModal());

        this.chessClock = new ChessClock(whiteTime, blackTime, whiteLabel, blackLabel);

        // Activate color selection — flip board if playing as black
        if (startingColor.equals("black")) {
            engine.board.isFlipped = true;
        }

        // Clear everything except toolbar
        this.layeredPane.removeAll();
        this.layeredPane.add(toolbarPanel, JLayeredPane.PALETTE_LAYER);

        // Add board
        this.layeredPane.add(engine.board);
        int boardSize = Variables.cols * Variables.tileSize;
        int bx = (getWidth() - boardSize) / 2 - 80;
        int by = (getHeight() - boardSize) / 2 - 20;
        engine.board.setBounds(bx, by, boardSize, boardSize);

        // Add clock labels — position depends on flip state
        this.layeredPane.add(whiteLabel);
        this.layeredPane.add(blackLabel);
        if (engine.board.isFlipped) {
            // Flipped: white clock on top, black on bottom
            whiteLabel.setBounds(bx + boardSize + 30, by + 10, 200, 90);
            blackLabel.setBounds(bx + boardSize + 30, by + boardSize - 100, 200, 90);
        } else {
            whiteLabel.setBounds(bx + boardSize + 30, by + boardSize - 100, 200, 90);
            blackLabel.setBounds(bx + boardSize + 30, by + 10, 200, 90);
        }

        // Back to Menu button
        JButton backButton = createPrimaryButton("\u2190 Menu");
        backButton.setBounds(bx + boardSize + 30, by + boardSize / 2 - 50, 160, 40);
        backButton.addActionListener(e -> returnToMenu());
        this.layeredPane.add(backButton);

        // Flip Board button
        JButton flipButton = createPrimaryButton("\u21C5 Flip");
        flipButton.setBounds(bx + boardSize + 30, by + boardSize / 2, 160, 40);
        flipButton.addActionListener(e -> {
            engine.board.flipBoard();
            // Swap clock label positions
            Rectangle wBounds = whiteLabel.getBounds();
            Rectangle bBounds = blackLabel.getBounds();
            whiteLabel.setBounds(bBounds);
            blackLabel.setBounds(wBounds);
        });
        this.layeredPane.add(flipButton);

        // Three Checks indicators
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

        // Initialize clock
        this.chessClock.setTimers();
        engine.setTimeIncrement(timeIncrement);
        engine.getClocks(this.chessClock);

        // Update title
        String timeLabel = (whiteTime / 60) + "+" + timeIncrement;
        setTitle("Gazi Chess \u2014 " + timeLabel + " " + variant);

        getContentPane().setBackground(Variables.frameBackGroundColor);

        this.layeredPane.revalidate();
        this.layeredPane.repaint();
        this.revalidate();
        this.repaint();
    }

    /// Return to the main menu from an active game
    private void returnToMenu() {
        layeredPane.removeAll();
        layeredPane.add(toolbarPanel, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(panel, JLayeredPane.DEFAULT_LAYER);
        setTitle("Gazi Chess Engine");

        if (chessClock != null) {
            chessClock.setTimers();
        }

        engine = new ChessEngine();
        createNewGame = new CreateNewGame(this, engine);
        createNewGame.exitButton.addActionListener(e -> closeNewGameModal());

        layeredPane.setSize(getContentPane().getSize());
        panel.setSize(getContentPane().getSize());
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    /// Creates check count indicator dots for Three Checks variant
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
                int y = (getHeight() - dotR) / 2;

                for (int i = 0; i < 3; i++) {
                    g2d.setColor(i < checks ? new Color(220, 50, 50) : Variables.buttonSecondaryColor);
                    g2d.fillOval(i * spacing, y, dotR, dotR);
                    g2d.setColor(Variables.frameBorderColor);
                    g2d.drawOval(i * spacing, y, dotR, dotR);
                }

                g2d.setColor(Variables.frameSubTextColor);
                g2d.setFont(new Font("SansSerif", Font.PLAIN, 11));
                g2d.drawString("Checks", 3 * spacing + 5, y + dotR - 2);
            }
        };
    }
}
