package Game.GameEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Logs game moves (as FEN strings) to a text file with timestamps.
 * Each game session appends its moves to GameLogs.txt.
 *
 * Future use: replay and analyze past games by reading the log file.
 */
public class TakeGameLogs {

    public static void takeLogs (String FEN, String date){
        try{
            File gameLogs = new File("GameLogs.txt");
            FileWriter fw = new FileWriter(gameLogs, true);

            if (gameLogs.createNewFile()) {
                fw.write(date + "\n");
            }

            if (gameLogs.exists()){
                fw.append(FEN);
                fw.close();
            }
        }catch (FileNotFoundException e1){
            System.out.println("File could not be opened.");
        }catch (IOException e2){
            System.out.println("Input value is not valid.");
        }
    }
}
