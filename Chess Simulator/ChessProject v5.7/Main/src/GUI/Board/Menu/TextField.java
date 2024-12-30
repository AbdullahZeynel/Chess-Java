package GUI.Board.Menu;

import javax.swing.*;
import java.awt.*;

public class TextField extends JTextField {
    
    public TextField(String text, int xPos, int yPos, int witdh, int height) {
        
        // These settings are used on All the TextFields that we use.
        // Both in Main menu and the Pop up menu.
        setText(text);
        setBounds(xPos, yPos, witdh, height);
        setEditable(false);
        setFocusable(Boolean.FALSE);
        setHorizontalAlignment(JTextField.CENTER);
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.PLAIN, 20));
        setOpaque(false);
        setBorder(null);


    }

}
