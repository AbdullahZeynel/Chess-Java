package GUI.Board.Menu;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
    public JLayeredPane layeredPane = new JLayeredPane();
    public JPanel panel = new JPanel();
    public CreateNewGame createNewGame = new CreateNewGame();


    public Frame() {



        panel.setLayout(null);
        panel.setBounds(0,0,1920,1080);

        JButton button = new JButton("Create a New Game");
        button.setBounds(800,600,250,70);
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
