package GUI.Board;

import GUI.Input;
import Game.GameEngine.ChessEngine;
import resources.Variables;

import javax.swing.*;
import java.awt.*;

public class PromotionsPanel extends JPanel {
    public ChessEngine engine;
    public Input input;

    public  PromotionsPanel(ChessEngine engine){
        this.engine = engine;
        //this.input = new Input(engine);

        this.setPreferredSize(Variables.promoPanelDimention);

        //this.addMouseListener(input);
        //this.addMouseMotionListener(input);
    }

    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Variables.promotionPanelColor);
        g2d.fillRect(0, 0, Variables.tileSize, Variables.tileSize * 4);


    }
}
