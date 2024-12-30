package Game.GameEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class TakeGameLogs {

    // This class will be used to take the local time when the game has starded and it will
    // Log the FEN moves that happened during the game.
    // Construct a new Interface that has an option "Analyze a Game"
    // Then read the time and the FEN inputs from the file so you can construct a story of the game.
    // If the user choses he can analyze the game with stockfish from the software.

    public static void takeLogs (String FEN, String date){
        try{
            File gameLogs = new File("GameLogs.txt");
            FileWriter fw = new FileWriter(gameLogs, true);

            if (gameLogs.createNewFile()) {
                fw.write(date + "\n"); // Deleting the previous games logs. And writing the current date.
            }

            if (gameLogs.exists()){
                fw.append(FEN);
                fw.close();
            }
            // Common expection handeling.
        }catch (FileNotFoundException e1){
            System.out.println("File could not be opened.");
        }catch (IOException e2){
            System.out.println("Input value is not valid.");
        }
    }

}
