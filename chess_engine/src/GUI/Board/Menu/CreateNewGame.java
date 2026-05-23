package GUI.Board.Menu;

import Game.GameEngine.ChessEngine;
import resources.Variables;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class CreateNewGame extends JLabel {

    public JButton exitButton;
    public JButton createButton;

    private Frame frame;
    public String startingColor = "white";
    private int selectedColorIndex = 0;

    private int selectedMinutes = 5;
    private int selectedIncrement = 0;
    private String selectedTimeLabel = "5+0";

    private static Image whiteKingSprite;
    private static Image blackKingSprite;

    static {
        try {
            BufferedImage sheet = ImageIO.read(new File(Variables.piecesFilePath));
            int scale = sheet.getWidth() / 6;
            whiteKingSprite = sheet.getSubimage(0, 0, scale, scale)
                    .getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            blackKingSprite = sheet.getSubimage(0, scale, scale, scale)
                    .getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        } catch (Exception ignored) {}
    }

    /// All time presets: {label, minutes, increment, category}
    private static final String[][] PRESETS = {
        {"1+0","1","0"},  {"1+1","1","1"},  {"2+1","2","1"},
        {"3+0","3","0"},  {"3+2","3","2"},  {"5+0","5","0"},
        {"5+3","5","3"},  {"10+0","10","0"}, {"10+5","10","5"},
        {"15+10","15","10"}, {"30+0","30","0"}, {"90+30","90","30"},
    };

    public CreateNewGame(Frame frame, ChessEngine engine) {
        this.frame = frame;
        setLayout(null);
        setOpaque(false);

        // ─── Title ───────────────────────────
        add(makeTitle());

        // ─── Variant Selector ────────────────
        add(createLabel("Variant", 50, 68));
        String[] variants = {"Standard", "Three Checks"};
        StyledDropdown variantSel = new StyledDropdown(variants);
        variantSel.setBounds(150, 62, 200, 36);
        add(variantSel);

        // ─── Time Control Grid ───────────────
        add(createLabel("Time", 50, 115));

        int gridX = 50, gridY = 145;
        int chipW = 90, chipH = 38, gapX = 8, gapY = 8;
        int cols = 4;
        for (int i = 0; i < PRESETS.length; i++) {
            JButton chip = createTimeChip(PRESETS[i]);
            int col = i % cols, row = i / cols;
            chip.setBounds(gridX + col * (chipW + gapX), gridY + row * (chipH + gapY), chipW, chipH);
            add(chip);
        }

        // ─── Custom Input Row ────────────────
        int customY = gridY + 3 * (chipH + gapY) + 10;
        add(createLabel("Custom", 50, customY + 4));

        JTextField minF = styledField("5", 150, customY, 50, 32);
        add(minF);
        add(createSmallLabel("min", 205, customY + 8));

        add(createSmallLabel("+", 230, customY + 6));

        JTextField incF = styledField("0", 245, customY, 45, 32);
        add(incF);
        add(createSmallLabel("sec", 295, customY + 8));

        JButton applyBtn = makeSmallBtn("Set");
        applyBtn.setBounds(330, customY, 55, 32);
        applyBtn.addActionListener(e -> {
            try {
                int m = Math.max(1, Math.min(180, Integer.parseInt(minF.getText().trim())));
                int inc = Math.max(0, Math.min(60, Integer.parseInt(incF.getText().trim())));
                selectedMinutes = m; selectedIncrement = inc;
                selectedTimeLabel = m + "+" + inc;
                repaint();
            } catch (NumberFormatException ignored) {}
        });
        add(applyBtn);

        // ─── Play As ─────────────────────────
        add(createLabel("Play As", 500, 68));
        int[] cX = {500, 578, 656};
        for (int i = 0; i < 3; i++) {
            JButton cb = createColorButton(i);
            cb.setBounds(cX[i], 100, 70, 80);
            add(cb);
        }

        // ─── Create Game ─────────────────────
        int btnY = customY + 55;
        createButton = makeActionBtn("Create Game");
        createButton.setBounds(50, btnY, 700, 50);
        createButton.addActionListener(e -> {
            String v = variantSel.getSelectedItem().equals("Three Checks") ? "threeChecks" : "standard";
            int wt = selectedMinutes * 60;
            frame.closeNewGameModal();
            frame.startGameFromModal(wt, wt, selectedIncrement, v, "offline", startingColor);
        });
        add(createButton);

        // ─── Exit ────────────────────────────
        exitButton = makeExitBtn();
        exitButton.setBounds(810, 15, 36, 36);
        add(exitButton);

        setBounds(0, 0, 880, btnY + 80);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(0, 0, 0, 40));
        g2d.fill(new RoundRectangle2D.Double(3, 3, getWidth() - 3, getHeight() - 3, 18, 18));
        g2d.setColor(Variables.framePanelColor);
        g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 3, getHeight() - 3, 18, 18));
        g2d.setColor(Variables.frameBorderColor);
        g2d.setStroke(new BasicStroke(1));
        g2d.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 3, getHeight() - 3, 18, 18));
        super.paintComponent(g);
    }

    // ── Factory Methods ──────────────────────────────────────

    private JLabel makeTitle() {
        JLabel l = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2d.setFont(new Font("SansSerif", Font.BOLD, 24));
                g2d.setColor(Variables.frameTextColor);
                g2d.drawString("New Game", 50, g2d.getFontMetrics().getAscent() + 2);
            }
        };
        l.setBounds(0, 15, 400, 38);
        return l;
    }

    private JLabel createLabel(String t, int x, int y) {
        JLabel l = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setFont(new Font("SansSerif", Font.BOLD, 13));
                g2.setColor(Variables.frameSubTextColor);
                g2.drawString(t, 0, g2.getFontMetrics().getAscent());
            }
        };
        l.setBounds(x, y, 120, 22);
        return l;
    }

    private JLabel createSmallLabel(String t, int x, int y) {
        JLabel l = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
                g2.setColor(Variables.frameSubTextColor);
                g2.drawString(t, 0, g2.getFontMetrics().getAscent());
            }
        };
        l.setBounds(x, y, 30, 18);
        return l;
    }

    private JButton createTimeChip(String[] d) {
        String label = d[0]; int mins = Integer.parseInt(d[1]); int inc = Integer.parseInt(d[2]);
        JButton b = new JButton() {
            boolean hover = false;
            { addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hover=true; repaint(); }
                @Override public void mouseExited(MouseEvent e) { hover=false; repaint(); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean sel = label.equals(selectedTimeLabel);
                g2.setColor(sel ? Variables.buttonPrimaryColor : (hover ? Variables.buttonSecondaryHoverColor : Variables.buttonSecondaryColor));
                g2.fill(new RoundRectangle2D.Double(0,0,getWidth()-1,getHeight()-1,10,10));
                if (sel) { g2.setColor(Variables.frameAccentColor); g2.setStroke(new BasicStroke(1.5f)); g2.draw(new RoundRectangle2D.Double(0,0,getWidth()-1,getHeight()-1,10,10)); }
                g2.setFont(new Font("SansSerif", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                g2.setColor(sel ? Variables.buttonTextColor : (hover ? Variables.buttonAccentTextColor : Variables.buttonSecondaryTextColor));
                g2.drawString(label,(getWidth()-fm.stringWidth(label))/2,(getHeight()+fm.getAscent()-fm.getDescent())/2);
            }
        };
        b.setContentAreaFilled(false); b.setBorderPainted(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(e -> { selectedMinutes=mins; selectedIncrement=inc; selectedTimeLabel=label; getParent().repaint(); });
        return b;
    }

    private JTextField styledField(String def, int x, int y, int w, int h) {
        JTextField f = new JTextField(def);
        f.setBounds(x,y,w,h);
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setHorizontalAlignment(JTextField.CENTER);
        f.setBackground(Variables.buttonSecondaryColor);
        f.setForeground(Variables.buttonSecondaryTextColor);
        f.setCaretColor(Variables.frameTextColor);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Variables.frameBorderColor),
                BorderFactory.createEmptyBorder(2,4,2,4)));
        return f;
    }

    private JButton makeSmallBtn(String t) {
        JButton b = new JButton() {
            boolean h=false;
            { addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e){h=true;repaint();}
                @Override public void mouseExited(MouseEvent e){h=false;repaint();}
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(h?Variables.buttonPrimaryHoverColor:Variables.buttonPrimaryColor);
                g2.fill(new RoundRectangle2D.Double(0,0,getWidth()-1,getHeight()-1,8,8));
                g2.setFont(new Font("SansSerif",Font.BOLD,11)); FontMetrics fm=g2.getFontMetrics();
                g2.setColor(Variables.buttonTextColor);
                g2.drawString(t,(getWidth()-fm.stringWidth(t))/2,(getHeight()+fm.getAscent()-fm.getDescent())/2);
            }
        };
        b.setContentAreaFilled(false);b.setBorderPainted(false);b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JButton makeActionBtn(String t) {
        JButton b = new JButton() {
            boolean h=false;
            { addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e){h=true;repaint();}
                @Override public void mouseExited(MouseEvent e){h=false;repaint();}
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(h?Variables.buttonPrimaryHoverColor:Variables.buttonPrimaryColor);
                g2.fill(new RoundRectangle2D.Double(0,0,getWidth()-1,getHeight()-1,14,14));
                g2.setFont(new Font("SansSerif",Font.BOLD,17)); FontMetrics fm=g2.getFontMetrics();
                g2.setColor(Variables.buttonTextColor);
                g2.drawString(t,(getWidth()-fm.stringWidth(t))/2,(getHeight()+fm.getAscent()-fm.getDescent())/2);
            }
        };
        b.setContentAreaFilled(false);b.setBorderPainted(false);b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JButton makeExitBtn() {
        JButton b = new JButton() {
            boolean h=false;
            { addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e){h=true;repaint();}
                @Override public void mouseExited(MouseEvent e){h=false;repaint();}
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                if(h){g2.setColor(new Color(200,50,50));g2.fill(new RoundRectangle2D.Double(0,0,getWidth()-1,getHeight()-1,10,10));}
                g2.setFont(new Font("SansSerif",Font.BOLD,16)); FontMetrics fm=g2.getFontMetrics();
                g2.setColor(h?Color.WHITE:Variables.frameSubTextColor);
                String x="\u2715";
                g2.drawString(x,(getWidth()-fm.stringWidth(x))/2,(getHeight()+fm.getAscent()-fm.getDescent())/2);
            }
        };
        b.setContentAreaFilled(false);b.setBorderPainted(false);b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JButton createColorButton(int ci) {
        JButton b = new JButton() {
            boolean h=false;
            { addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e){h=true;repaint();}
                @Override public void mouseExited(MouseEvent e){h=false;repaint();}
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                boolean sel=(selectedColorIndex==ci);
                g2.setColor(sel?Variables.buttonPrimaryColor:(h?Variables.buttonSecondaryHoverColor:Variables.buttonSecondaryColor));
                g2.fill(new RoundRectangle2D.Double(0,0,getWidth()-1,getHeight()-1,12,12));
                if(sel){g2.setColor(Variables.frameAccentColor);g2.setStroke(new BasicStroke(2));g2.draw(new RoundRectangle2D.Double(0,0,getWidth()-1,getHeight()-1,12,12));}
                int ss=36;
                if(ci==0&&whiteKingSprite!=null) g2.drawImage(whiteKingSprite,(getWidth()-ss)/2,6,ss,ss,null);
                else if(ci==2&&blackKingSprite!=null) g2.drawImage(blackKingSprite,(getWidth()-ss)/2,6,ss,ss,null);
                else if(ci==1&&whiteKingSprite!=null&&blackKingSprite!=null){int hs=28;g2.drawImage(whiteKingSprite,getWidth()/2-hs-2,8,hs,hs,null);g2.drawImage(blackKingSprite,getWidth()/2+2,8,hs,hs,null);}
                String l=ci==0?"White":(ci==2?"Black":"Random");
                g2.setFont(new Font("SansSerif",Font.PLAIN,10));FontMetrics fm=g2.getFontMetrics();
                g2.setColor(sel?new Color(220,220,220):Variables.frameSubTextColor);
                g2.drawString(l,(getWidth()-fm.stringWidth(l))/2,getHeight()-8);
            }
        };
        b.setContentAreaFilled(false);b.setBorderPainted(false);b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(e->{selectedColorIndex=ci;startingColor=ci==0?"white":(ci==2?"black":(Math.random()>0.5?"white":"black"));getParent().repaint();});
        return b;
    }

    /// Dropdown with JPopupMenu
    private class StyledDropdown extends JPanel {
        private String[] opts; private int sel=0; private boolean h=false;
        StyledDropdown(String[] o){opts=o;setOpaque(false);setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter(){
                @Override public void mouseEntered(MouseEvent e){h=true;repaint();}
                @Override public void mouseExited(MouseEvent e){h=false;repaint();}
                @Override public void mouseClicked(MouseEvent e){showP();}
            });
        }
        String getSelectedItem(){return opts[sel];}
        private void showP(){
            JPopupMenu p=new JPopupMenu();p.setBackground(Variables.framePanelColor);
            p.setBorder(BorderFactory.createLineBorder(Variables.frameBorderColor));
            for(int i=0;i<opts.length;i++){final int idx=i;
                JMenuItem it=new JMenuItem(opts[i]);it.setFont(new Font("SansSerif",Font.PLAIN,13));
                it.setBackground(Variables.framePanelColor);it.setForeground(Variables.buttonSecondaryTextColor);
                it.setBorder(BorderFactory.createEmptyBorder(5,12,5,12));
                it.addMouseListener(new MouseAdapter(){
                    @Override public void mouseEntered(MouseEvent e){it.setBackground(Variables.buttonSecondaryHoverColor);}
                    @Override public void mouseExited(MouseEvent e){it.setBackground(Variables.framePanelColor);}
                });
                it.addActionListener(ev->{sel=idx;repaint();});p.add(it);
            }
            p.show(this,0,getHeight());
        }
        @Override protected void paintComponent(Graphics g){
            Graphics2D g2=(Graphics2D)g;g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(h?Variables.buttonSecondaryHoverColor:Variables.buttonSecondaryColor);
            g2.fill(new RoundRectangle2D.Double(0,0,getWidth()-1,getHeight()-1,10,10));
            g2.setColor(h?Variables.frameAccentColor:Variables.frameBorderColor);g2.setStroke(new BasicStroke(1));
            g2.draw(new RoundRectangle2D.Double(0,0,getWidth()-1,getHeight()-1,10,10));
            g2.setFont(new Font("SansSerif",Font.PLAIN,13));FontMetrics fm=g2.getFontMetrics();
            g2.setColor(Variables.buttonSecondaryTextColor);
            g2.drawString(opts[sel],12,(getHeight()+fm.getAscent()-fm.getDescent())/2);
            g2.setColor(Variables.frameSubTextColor);
            g2.drawString("\u25BE",getWidth()-18,(getHeight()+fm.getAscent()-fm.getDescent())/2);
        }
    }
}
