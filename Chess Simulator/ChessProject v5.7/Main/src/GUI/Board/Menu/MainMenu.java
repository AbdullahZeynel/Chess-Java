package GUI.Board.Menu;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends javax.swing.JLabel {

    public MainMenu() {


        JTextField menuLabel = new JTextField();
        menuLabel.setEditable(Boolean.FALSE);
        menuLabel.setFocusable(Boolean.FALSE);
        menuLabel.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        menuLabel.setText("Gazi Chess");
        menuLabel.setFont(new Font("Arial", Font.PLAIN, 26));
        menuLabel.setBounds(400,250,700,400);

        JButton openMenuButton = new JButton("Start Game");
        openMenuButton.setFocusable(false);
        openMenuButton.setFocusPainted(false);
        openMenuButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        openMenuButton.setBounds(550,500,450, 150);
        openMenuButton.addActionListener(e -> {
            Frame frame = new Frame();
        });

        menuLabel.add(openMenuButton);

        add(menuLabel);
        setSize(1920, 1080);

    }

}
