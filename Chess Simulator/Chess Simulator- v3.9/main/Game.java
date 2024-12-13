package main;

import javax.swing.*;
import java.awt.*;

public class Game {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.getContentPane().setBackground(new Color(23, 21, 19));
        frame.setLayout(new GridBagLayout());
        frame.setMinimumSize(new Dimension(1000, 1000));
        frame.setLocationRelativeTo(null);

        Board board = new Board();
        //board.addPieces(false);
        //board.addPieces(true);
        frame.add(board);

        frame.setVisible(true);



    }
}


/*
        DEBUGGING
                System.out.println("[DEBUGGING]");
        System.out.println(board);
        if (board.selectedPiece != null) {
            System.out.println("Selected piece: " + board.selectedPiece.name + " at " + board.selectedPiece.col + ", " + board.selectedPiece.row);
        }
 */