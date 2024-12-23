package GUI.Board.Menu;

import javax.swing.*;
import java.awt.*;

public class TextField extends JTextField {

    //int xPos, yPos, witdh, height;
    //String text;

    public TextField(String text, int xPos, int yPos, int witdh, int height) {

        // We do not need to use the above fields currently.


        setText(text);
        setBounds(xPos, yPos, witdh, height);
        setEditable(false);
        setFocusable(Boolean.FALSE);
        setHorizontalAlignment(JTextField.CENTER);
        //setBackground(Color.MAGENTA);
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.PLAIN, 20));
        setOpaque(false);
        setBorder(null);



    }

}

/*

        JTextField variantTextField = new JTextField("Variant");
        variantTextField.setEditable(false);
        variantTextField.setHorizontalAlignment(JTextField.CENTER);
        variantTextField.setBackground(Color.MAGENTA);
        variantTextField.setForeground(Color.WHITE);
        variantTextField.setFont(new Font("Arial", Font.PLAIN, 20));
        variantTextField.setFocusable(Boolean.FALSE);
        variantTextField.setOpaque(true);
        variantTextField.setBounds(50, 200, 150, 80);

 */