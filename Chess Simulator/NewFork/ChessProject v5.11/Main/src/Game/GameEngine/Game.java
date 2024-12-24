package Game.GameEngine;

import Game.GameEngine.User.AI_User;
import Game.GameEngine.User.ServerUser;
import Game.GameEngine.User.User;
import resources.Variables;

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class Game {
    static JFrame frame;
    static ChessEngine engine;

    static User host;
    static User player2;

    public static void main(String[] args) {
        String gameMode;
        /// Debugging part
        System.out.print("Enter Game Mode: ");
        Scanner sc = new Scanner(System.in);
        gameMode = sc.nextLine();

        frame = new JFrame("Gazi Chess");

        switch (gameMode){
            case "Analyse":
                createAnalyseGame();
                break;
            case "Online":
                createOnlineGame();
                break;
            case "AgainstAI":
                createAgainstAIGame();
                break;
            default:
                throw new Error("Invalid game mode");
        }

        if(!gameMode.equals("Analyse")){
            engine = new ChessEngine(host, player2);
        } else {
            engine = new ChessEngine(host);
        }


        //engine = new ChessEngine(user, user2);
        //engine = new ChessEngine(user);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().setBackground(Variables.frameBackGroundColor);
        frame.setLayout(new GridBagLayout());
        frame.setMinimumSize(Variables.defaultDimention);
        frame.setLocationRelativeTo(null);

        frame.add(engine.board);
        frame.setVisible(true);
    }

    private static void createAnalyseGame(){
        host = new User("Host");
    }

    private static void createOnlineGame(){
        host = new User("Host");
        player2 = new ServerUser("Server");
    }

    private static void createAgainstAIGame(){
        host = new User("Host");
        player2 = new AI_User("Stockfish");
    }
}


/*

    public static void showBoard(){
        frame.add(engine.board);
        frame.setVisible(true);
    }

    public static void showPromotionPanel(){
        frame.add(engine.promotionsPanel);
        frame.setVisible(true);
    }
 */