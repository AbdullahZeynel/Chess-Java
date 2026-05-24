package GUI.Board.Menu;

import javax.swing.*;
import java.awt.*;

/**
 * Custom styled text field used as labels in the menu system.
 * Non-editable, transparent background, centered text.
 */
public class TextField extends JTextField {
    
    public TextField(String text, int xPos, int yPos, int width, int height) {
        setText(text);
        setBounds(xPos, yPos, width, height);
        setEditable(false);
        setFocusable(Boolean.FALSE);
        setHorizontalAlignment(JTextField.CENTER);
        setForeground(Color.WHITE);
        setFont(new Font("SansSerif", Font.PLAIN, 20));
        setOpaque(false);
        setBorder(null);
    }
}
