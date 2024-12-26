/*package GUI.Board.Menu;

import GUI.Board.ChessClock;
import Game.GameEngine.ChessEngine;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
    public JLayeredPane layeredPane = new JLayeredPane();
    public JPanel panel = new JPanel();
    public CreateNewGame createNewGame = new CreateNewGame();
    static ChessEngine engine = new ChessEngine();
    JLabel whiteLabel = new JLabel();
    JLabel blackLabel = new JLabel();


    public int whiteTime = 60;
    public int blackTime = 60;
    public int timeIncrement = 0;

    public String variant = "standard";
    public String mode = "offline";
    public String startingColor = "white";


    public Frame() {



        panel.setLayout(null);
        panel.setBounds(0,0,1920,1080);

        JButton standartSelect1 = new JButton("3 + 1");
        standartSelect1.setBounds(680, 200, 150, 50);
        standartSelect1.addActionListener(e -> {
            this.whiteTime = 180;
            this.blackTime = 180;
            this.timeIncrement = 1;
            this.variant = "standard";
            this.mode = "offline";
            this.startingColor = "white";

            layeredPane.remove(panel); // bu noktada pop up arayuzunun acik olmasi imkansiz. Cikartmayabiliriz

            ChessClock chessClock = new ChessClock(whiteTime,blackTime, whiteLabel,blackLabel);

            layeredPane.add(engine.board);
            engine.board.setBounds(620, 170, 680, 680);
            layeredPane.add(whiteLabel);
            layeredPane.add(blackLabel);

            this.layeredPane.revalidate();
            this.layeredPane.repaint();
            this.revalidate();
            this.repaint();


        });
        panel.add(standartSelect1);

        JButton standartSelect2 = new JButton(" 5 + 1");
        standartSelect2.setBounds(standartSelect1.getX() + standartSelect1.getWidth() + 50, standartSelect1.getY(), standartSelect1.getWidth(),standartSelect1.getHeight());
        standartSelect2.addActionListener(e -> {
            this.whiteTime = 300;
            this.blackTime = 300;
            this.timeIncrement = 1;
            this.variant = "standard";
            this.mode = "offline";
            this.startingColor = "white";

            layeredPane.remove(panel); // bu noktada pop up arayuzunun acik olmasi imkansiz. Cikartmayabiliriz

            ChessClock chessClock = new ChessClock(whiteTime,blackTime, whiteLabel,blackLabel);

            layeredPane.add(engine.board);
            engine.board.setBounds(620, 170, 680, 680);
            layeredPane.add(whiteLabel);
            layeredPane.add(blackLabel);

            this.layeredPane.revalidate();
            this.layeredPane.repaint();
            this.revalidate();
            this.repaint();


        });
        panel.add(standartSelect2);

        JButton standartSelect3 = new JButton(" 15 + 3");
        standartSelect3.setBounds(standartSelect2.getX() + standartSelect2.getWidth() + 50, standartSelect2.getY(), standartSelect2.getWidth(),standartSelect2.getHeight());
        standartSelect3.addActionListener(e -> {
            this.whiteTime = 900;
            this.blackTime = 900;
            this.timeIncrement = 3;
            this.variant = "standard";
            this.mode = "offline";
            this.startingColor = "white";

            layeredPane.remove(panel); // bu noktada pop up arayuzunun acik olmasi imkansiz. Cikartmayabiliriz

            ChessClock chessClock = new ChessClock(whiteTime,blackTime, whiteLabel,blackLabel);

            layeredPane.add(engine.board);
            engine.board.setBounds(620, 170, 680, 680);
            layeredPane.add(whiteLabel);
            layeredPane.add(blackLabel);

            this.layeredPane.revalidate();
            this.layeredPane.repaint();
            this.revalidate();
            this.repaint();


        });
        panel.add(standartSelect3);

        TextField standartTextField = new TextField("Standart",800,135,300,50);
        standartTextField.setForeground(Color.BLACK);
        panel.add(standartTextField);

        JButton mergeChessSelect1 = new JButton("3 + 1");
        mergeChessSelect1.setBounds(680, 350, 150, 50);
        mergeChessSelect1.addActionListener(e -> {
            this.whiteTime = 180;
            this.blackTime = 180;
            this.timeIncrement = 1;
            this.variant = "merge";
            this.mode = "offline";
            this.startingColor = "white";

            layeredPane.remove(panel); // bu noktada pop up arayuzunun acik olmasi imkansiz. Cikartmayabiliriz

            ChessClock chessClock = new ChessClock(whiteTime,blackTime, whiteLabel,blackLabel);

            layeredPane.add(engine.board);
            engine.board.setBounds(620, 170, 680, 680);
            layeredPane.add(whiteLabel);
            layeredPane.add(blackLabel);

            this.layeredPane.revalidate();
            this.layeredPane.repaint();
            this.revalidate();
            this.repaint();


        });
        panel.add(mergeChessSelect1);

        JButton mergeChessSelect2 = new JButton(" 5 + 1");
        mergeChessSelect2.setBounds(mergeChessSelect1.getX() + mergeChessSelect1.getWidth() + 50, mergeChessSelect1.getY(), mergeChessSelect1.getWidth(),mergeChessSelect1.getHeight());
        mergeChessSelect2.addActionListener(e -> {
            this.whiteTime = 300;
            this.blackTime = 300;
            this.timeIncrement = 1;
            this.variant = "merge";
            this.mode = "offline";
            this.startingColor = "white";

            layeredPane.remove(panel); // bu noktada pop up arayuzunun acik olmasi imkansiz. Cikartmayabiliriz

            ChessClock chessClock = new ChessClock(whiteTime,blackTime, whiteLabel,blackLabel);

            layeredPane.add(engine.board);
            engine.board.setBounds(620, 170, 680, 680);
            layeredPane.add(whiteLabel);
            layeredPane.add(blackLabel);

            this.layeredPane.revalidate();
            this.layeredPane.repaint();
            this.revalidate();
            this.repaint();


        });
        panel.add(mergeChessSelect2);

        JButton mergeChessSelect3 = new JButton(" 15 + 3");
        mergeChessSelect3.setBounds(mergeChessSelect2.getX() + mergeChessSelect2.getWidth() + 50, mergeChessSelect2.getY(), mergeChessSelect2.getWidth(),mergeChessSelect2.getHeight());
        mergeChessSelect3.addActionListener(e -> {
            this.whiteTime = 900;
            this.blackTime = 900;
            this.timeIncrement = 3;
            this.variant = "merge";
            this.mode = "offline";
            this.startingColor = "white";

            layeredPane.remove(panel); // bu noktada pop up arayuzunun acik olmasi imkansiz. Cikartmayabiliriz

            ChessClock chessClock = new ChessClock(whiteTime,blackTime, whiteLabel,blackLabel);

            layeredPane.add(engine.board);
            engine.board.setBounds(620, 170, 680, 680);
            layeredPane.add(whiteLabel);
            layeredPane.add(blackLabel);

            this.layeredPane.revalidate();
            this.layeredPane.repaint();
            this.revalidate();
            this.repaint();


        });
        panel.add(mergeChessSelect3);

        TextField mergeTextField = new TextField("MergeChess",800,285,300,50);
        mergeTextField.setForeground(Color.BLACK);
        panel.add(mergeTextField);

        JButton threeChecksSelect1 = new JButton("3 + 1");
        threeChecksSelect1.setBounds(680, 500, 150, 50);
        threeChecksSelect1.addActionListener(e -> {
            this.whiteTime = 180;
            this.blackTime = 180;
            this.timeIncrement = 1;
            this.variant = "threeChecks";
            this.mode = "offline";
            this.startingColor = "white";

            layeredPane.remove(panel); // bu noktada pop up arayuzunun acik olmasi imkansiz. Cikartmayabiliriz

            ChessClock chessClock = new ChessClock(whiteTime,blackTime, whiteLabel,blackLabel);

            layeredPane.add(engine.board);
            engine.board.setBounds(620, 170, 680, 680);
            layeredPane.add(whiteLabel);
            layeredPane.add(blackLabel);

            this.layeredPane.revalidate();
            this.layeredPane.repaint();
            this.revalidate();
            this.repaint();


        });
        panel.add(threeChecksSelect1);

        JButton threeChecksSelect2 = new JButton(" 5 + 1");
        threeChecksSelect2.setBounds(threeChecksSelect1.getX() + threeChecksSelect1.getWidth() + 50, threeChecksSelect1.getY(), threeChecksSelect1.getWidth(),threeChecksSelect1.getHeight());
        threeChecksSelect2.addActionListener(e -> {
            this.whiteTime = 300;
            this.blackTime = 300;
            this.timeIncrement = 1;
            this.variant = "threeChecks";
            this.mode = "offline";
            this.startingColor = "white";

            layeredPane.remove(panel); // bu noktada pop up arayuzunun acik olmasi imkansiz. Cikartmayabiliriz

            ChessClock chessClock = new ChessClock(whiteTime,blackTime, whiteLabel,blackLabel);

            layeredPane.add(engine.board);
            engine.board.setBounds(620, 170, 680, 680);
            layeredPane.add(whiteLabel);
            layeredPane.add(blackLabel);

            this.layeredPane.revalidate();
            this.layeredPane.repaint();
            this.revalidate();
            this.repaint();


        });
        panel.add(threeChecksSelect2);

        JButton threeChecksSelect3 = new JButton(" 15 + 3");
        threeChecksSelect3.setBounds(threeChecksSelect2.getX() + threeChecksSelect2.getWidth() + 50, threeChecksSelect2.getY(), threeChecksSelect2.getWidth(),threeChecksSelect2.getHeight());
        threeChecksSelect3.addActionListener(e -> {
            this.whiteTime = 900;
            this.blackTime = 900;
            this.timeIncrement = 3;
            this.variant = "threeChecks";
            this.mode = "offline";
            this.startingColor = "white";

            layeredPane.remove(panel); // bu noktada pop up arayuzunun acik olmasi imkansiz. Cikartmayabiliriz

            ChessClock chessClock = new ChessClock(whiteTime,blackTime, whiteLabel,blackLabel);

            layeredPane.add(engine.board);
            engine.board.setBounds(620, 170, 680, 680);
            layeredPane.add(whiteLabel);
            layeredPane.add(blackLabel);

            this.layeredPane.revalidate();
            this.layeredPane.repaint();
            this.revalidate();
            this.repaint();


        });
        panel.add(threeChecksSelect3);

        TextField threeChecksTextField = new TextField("ThreeChecks",800,435,300,50);
        threeChecksTextField.setForeground(Color.BLACK);
        panel.add(threeChecksTextField);

        JButton button = new JButton("Create a New Game");
        button.setBounds(780,650,350,75);
        button.addActionListener(e -> {
            if (e.getSource() == button) {
                layeredPane.add(createNewGame, JLayeredPane.POPUP_LAYER);
                layeredPane.revalidate();
                layeredPane.repaint();
                revalidate();
                repaint();
            }
        });
        panel.add(button);
        layeredPane.add(panel, JLayeredPane.DEFAULT_LAYER);

        add(layeredPane);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setBackground(Color.DARK_GRAY);

        createNewGame.exitButton.addActionListener(e -> {
            layeredPane.remove(createNewGame);
            layeredPane.revalidate();
            layeredPane.repaint();
        });



        setVisible(true);

    }

}


 */
package GUI.Board.Menu;

import GUI.Board.ChessClock;
import Game.GameEngine.ChessEngine;
import resources.Variables;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
    public JLayeredPane layeredPane = new JLayeredPane();
    public JPanel panel = new JPanel();
    public CreateNewGame createNewGame;
    static ChessEngine engine = new ChessEngine();
    JLabel whiteLabel = new JLabel();
    JLabel blackLabel = new JLabel();
    ChessClock chessClock;

    public int whiteTime = 60;
    public int blackTime = 60;
    public int timeIncrement = 0;

    public String variant = "standard";
    public String mode = "offline";
    public String startingColor = "white";

    public Frame() {
        createNewGame = new CreateNewGame(this); // Pass the Frame instance to CreateNewGame

        panel.setLayout(null);
        panel.setBounds(0,0,1920,1080);

        JButton standartSelect1 = new JButton("3 + 1");
        standartSelect1.setBounds(680, 200, 150, 50);
        standartSelect1.addActionListener(e -> {

            this.whiteTime = 180;
            this.blackTime = 180;
            this.timeIncrement = 1;
            this.variant = "standard";
            this.mode = "offline";
            this.startingColor = "white";
            this.chessClock = new ChessClock(whiteTime, blackTime, whiteLabel, blackLabel);



            this.layeredPane.remove(this.panel);
            this.layeredPane.remove(this.createNewGame);
            this.layeredPane.add(engine.board);
            // 680x680 boyularinda bir board.
            // 1920 - 680 = 1240/2=620 1020 - 680 = 340 / 2 = 170
            engine.board.setBounds(620, 170, 680, 680);

            this.layeredPane.add(whiteLabel);
            this.layeredPane.add(blackLabel);

            this.chessClock.setTimers();
            engine.getClocks(this.chessClock);

            this.layeredPane.revalidate();
            this.layeredPane.repaint();
            this.revalidate();
            this.repaint();
        });
        panel.add(standartSelect1);

        JButton standartSelect2 = new JButton(" 5 + 1");
        standartSelect2.setBounds(standartSelect1.getX() + standartSelect1.getWidth() + 50, standartSelect1.getY(), standartSelect1.getWidth(),standartSelect1.getHeight());
        standartSelect2.addActionListener(e -> {
            this.whiteTime = 300;
            this.blackTime = 300;
            this.timeIncrement = 1;
            this.variant = "standard";
            this.mode = "offline";
            this.startingColor = "white";

            this.chessClock = new ChessClock(whiteTime, blackTime, whiteLabel, blackLabel);



            this.layeredPane.remove(this.panel);
            this.layeredPane.remove(this.createNewGame);
            this.layeredPane.add(engine.board);
            // 680x680 boyularinda bir board.
            // 1920 - 680 = 1240/2=620 1020 - 680 = 340 / 2 = 170
            engine.board.setBounds(620, 170, 680, 680);

            this.layeredPane.add(whiteLabel);
            this.layeredPane.add(blackLabel);

            this.chessClock.setTimers();
            engine.getClocks(this.chessClock);

            this.layeredPane.revalidate();
            this.layeredPane.repaint();
            this.revalidate();
            this.repaint();
            /*layeredPane.remove(panel);
            layeredPane.remove(createNewGame);
            layeredPane.add(engine.board);
            // 680x680 boyularinda bir board.
            // 1920 - 680 = 1240/2=620 1020 - 680 = 340 / 2 = 170
            engine.board.setBounds(620, 170, 680, 680);

            layeredPane.add(whiteLabel);
            layeredPane.add(blackLabel);

            chessClock.setTimers();
            engine.getClocks(chessClock);

            layeredPane.revalidate();
            layeredPane.repaint();
            revalidate();
            repaint();

             */

        });
        panel.add(standartSelect2);

        JButton standartSelect3 = new JButton(" 15 + 3");
        standartSelect3.setBounds(standartSelect2.getX() + standartSelect2.getWidth() + 50, standartSelect2.getY(), standartSelect2.getWidth(),standartSelect2.getHeight());
        standartSelect3.addActionListener(e -> {
            this.whiteTime = 900;
            this.blackTime = 900;
            this.timeIncrement = 3;
            this.variant = "standard";
            this.mode = "offline";
            this.startingColor = "white";

            this.chessClock = new ChessClock(whiteTime, blackTime, whiteLabel, blackLabel);



            this.layeredPane.remove(this.panel);
            this.layeredPane.remove(this.createNewGame);
            this.layeredPane.add(engine.board);
            // 680x680 boyularinda bir board.
            // 1920 - 680 = 1240/2=620 1020 - 680 = 340 / 2 = 170
            engine.board.setBounds(620, 170, 680, 680);

            this.layeredPane.add(whiteLabel);
            this.layeredPane.add(blackLabel);

            this.chessClock.setTimers();
            engine.getClocks(this.chessClock);

            this.layeredPane.revalidate();
            this.layeredPane.repaint();
            this.revalidate();
            this.repaint();


        });
        panel.add(standartSelect3);

        JButton mergeChessSelect1 = new JButton("3 + 1");
        mergeChessSelect1.setBounds(680, 350, 150, 50);
        mergeChessSelect1.addActionListener(e -> {
            this.whiteTime = 180;
            this.blackTime = 180;
            this.timeIncrement = 1;
            this.variant = "merge";
            this.mode = "offline";
            this.startingColor = "white";

            this.chessClock = new ChessClock(whiteTime, blackTime, whiteLabel, blackLabel);



            this.layeredPane.remove(this.panel);
            this.layeredPane.remove(this.createNewGame);
            this.layeredPane.add(engine.board);
            // 680x680 boyularinda bir board.
            // 1920 - 680 = 1240/2=620 1020 - 680 = 340 / 2 = 170
            engine.board.setBounds(620, 170, 680, 680);

            this.layeredPane.add(whiteLabel);
            this.layeredPane.add(blackLabel);

            this.chessClock.setTimers();
            engine.getClocks(this.chessClock);

            this.layeredPane.revalidate();
            this.layeredPane.repaint();
            this.revalidate();
            this.repaint();

        });
        panel.add(mergeChessSelect1);

        JButton mergeChessSelect2 = new JButton(" 5 + 1");
        mergeChessSelect2.setBounds(mergeChessSelect1.getX() + mergeChessSelect1.getWidth() + 50, mergeChessSelect1.getY(), mergeChessSelect1.getWidth(),mergeChessSelect1.getHeight());
        mergeChessSelect2.addActionListener(e -> {
            this.whiteTime = 300;
            this.blackTime = 300;
            this.timeIncrement = 1;
            this.variant = "merge";
            this.mode = "offline";
            this.startingColor = "white";

            this.chessClock = new ChessClock(whiteTime, blackTime, whiteLabel, blackLabel);



            this.layeredPane.remove(this.panel);
            this.layeredPane.remove(this.createNewGame);
            this.layeredPane.add(engine.board);
            // 680x680 boyularinda bir board.
            // 1920 - 680 = 1240/2=620 1020 - 680 = 340 / 2 = 170
            engine.board.setBounds(620, 170, 680, 680);

            this.layeredPane.add(whiteLabel);
            this.layeredPane.add(blackLabel);

            this.chessClock.setTimers();
            engine.getClocks(this.chessClock);

            this.layeredPane.revalidate();
            this.layeredPane.repaint();
            this.revalidate();
            this.repaint();


        });
        panel.add(mergeChessSelect2);

        JButton mergeChessSelect3 = new JButton(" 15 + 3");
        mergeChessSelect3.setBounds(mergeChessSelect2.getX() + mergeChessSelect2.getWidth() + 50, mergeChessSelect2.getY(), mergeChessSelect2.getWidth(),mergeChessSelect2.getHeight());
        mergeChessSelect3.addActionListener(e -> {
            this.whiteTime = 900;
            this.blackTime = 900;
            this.timeIncrement = 3;
            this.variant = "merge";
            this.mode = "offline";
            this.startingColor = "white";

            this.chessClock = new ChessClock(whiteTime, blackTime, whiteLabel, blackLabel);



            this.layeredPane.remove(this.panel);
            this.layeredPane.remove(this.createNewGame);
            this.layeredPane.add(engine.board);
            // 680x680 boyularinda bir board.
            // 1920 - 680 = 1240/2=620 1020 - 680 = 340 / 2 = 170
            engine.board.setBounds(620, 170, 680, 680);

            this.layeredPane.add(whiteLabel);
            this.layeredPane.add(blackLabel);

            this.chessClock.setTimers();
            engine.getClocks(this.chessClock);

            this.layeredPane.revalidate();
            this.layeredPane.repaint();
            this.revalidate();
            this.repaint();


        });
        panel.add(mergeChessSelect3);


        JButton threeChecksSelect1 = new JButton("3 + 1");
        threeChecksSelect1.setBounds(680, 500, 150, 50);
        threeChecksSelect1.addActionListener(e -> {
            this.whiteTime = 180;
            this.blackTime = 180;
            this.timeIncrement = 1;
            this.variant = "threeChecks";
            this.mode = "offline";
            this.startingColor = "white";

            this.chessClock = new ChessClock(whiteTime, blackTime, whiteLabel, blackLabel);



            this.layeredPane.remove(this.panel);
            this.layeredPane.remove(this.createNewGame);
            this.layeredPane.add(engine.board);
            // 680x680 boyularinda bir board.
            // 1920 - 680 = 1240/2=620 1020 - 680 = 340 / 2 = 170
            engine.board.setBounds(620, 170, 680, 680);

            this.layeredPane.add(whiteLabel);
            this.layeredPane.add(blackLabel);

            this.chessClock.setTimers();
            engine.getClocks(this.chessClock);

            this.layeredPane.revalidate();
            this.layeredPane.repaint();
            this.revalidate();
            this.repaint();


        });
        panel.add(threeChecksSelect1);

        JButton threeChecksSelect2 = new JButton(" 5 + 1");
        threeChecksSelect2.setBounds(threeChecksSelect1.getX() + threeChecksSelect1.getWidth() + 50, threeChecksSelect1.getY(), threeChecksSelect1.getWidth(),threeChecksSelect1.getHeight());
        threeChecksSelect2.addActionListener(e -> {
            this.whiteTime = 300;
            this.blackTime = 300;
            this.timeIncrement = 1;
            this.variant = "threeChecks";
            this.mode = "offline";
            this.startingColor = "white";

            this.chessClock = new ChessClock(whiteTime, blackTime, whiteLabel, blackLabel);



            this.layeredPane.remove(this.panel);
            this.layeredPane.remove(this.createNewGame);
            this.layeredPane.add(engine.board);
            // 680x680 boyularinda bir board.
            // 1920 - 680 = 1240/2=620 1020 - 680 = 340 / 2 = 170
            engine.board.setBounds(620, 170, 680, 680);

            this.layeredPane.add(whiteLabel);
            this.layeredPane.add(blackLabel);

            this.chessClock.setTimers();
            engine.getClocks(this.chessClock);

            this.layeredPane.revalidate();
            this.layeredPane.repaint();
            this.revalidate();
            this.repaint();


        });
        panel.add(threeChecksSelect2);

        JButton threeChecksSelect3 = new JButton(" 15 + 3");
        threeChecksSelect3.setBounds(threeChecksSelect2.getX() + threeChecksSelect2.getWidth() + 50, threeChecksSelect2.getY(), threeChecksSelect2.getWidth(),threeChecksSelect2.getHeight());
        threeChecksSelect3.addActionListener(e -> {
            this.whiteTime = 900;
            this.blackTime = 900;
            this.timeIncrement = 3;
            this.variant = "threeChecks";
            this.mode = "offline";
            this.startingColor = "white";

            this.chessClock = new ChessClock(whiteTime, blackTime, whiteLabel, blackLabel);



            this.layeredPane.remove(this.panel);
            this.layeredPane.remove(this.createNewGame);
            this.layeredPane.add(engine.board);
            // 680x680 boyularinda bir board.
            // 1920 - 680 = 1240/2=620 1020 - 680 = 340 / 2 = 170
            engine.board.setBounds(620, 170, 680, 680);

            this.layeredPane.add(whiteLabel);
            this.layeredPane.add(blackLabel);

            this.chessClock.setTimers();
            engine.getClocks(this.chessClock);

            this.layeredPane.revalidate();
            this.layeredPane.repaint();
            this.revalidate();
            this.repaint();

        });
        panel.add(threeChecksSelect3);


        JButton button = new JButton("Create a New Game");
        button.setBounds(780,650,350,75);
        button.addActionListener(e -> {
            if (e.getSource() == button) {
                layeredPane.add(createNewGame, JLayeredPane.POPUP_LAYER);
                layeredPane.revalidate();
                layeredPane.repaint();
                revalidate();
                repaint();
            }
        });
        panel.add(button);

        layeredPane.add(panel, JLayeredPane.DEFAULT_LAYER);
        add(layeredPane);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1920, 1080);
        getContentPane().setBackground(Variables.frameBackGroundColor);
        ///setBackground(Color.DARK_GRAY);

        createNewGame.exitButton.addActionListener(e -> {
            layeredPane.remove(createNewGame);
            layeredPane.revalidate();
            layeredPane.repaint();
        });

        TextField standartTextField = new TextField("Standart",800,135,300,50);
        standartTextField.setForeground(Color.BLACK);
        panel.add(standartTextField);

        TextField threeChecksTextField = new TextField("ThreeChecks",800,435,300,50);
        threeChecksTextField.setForeground(Color.BLACK);
        panel.add(threeChecksTextField);

        TextField mergeTextField = new TextField("MergeChess",800,285,300,50);
        mergeTextField.setForeground(Color.BLACK);
        panel.add(mergeTextField);



        setMinimumSize(new Dimension(1000, 1000));
        setVisible(true);
    }
}
            /*layeredPane.remove(panel);
            ChessClock chessClock = new ChessClock(whiteTime,blackTime, whiteLabel,blackLabel);
            layeredPane.add(engine.board);
            engine.board.setBounds(620, 170, 680, 680);
            layeredPane.add(whiteLabel);
            layeredPane.add(blackLabel);
            this.layeredPane.revalidate();
            this.layeredPane.repaint();
            this.revalidate();
            this.repaint();

             */