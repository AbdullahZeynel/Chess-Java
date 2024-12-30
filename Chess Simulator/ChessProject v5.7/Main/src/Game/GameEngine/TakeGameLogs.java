package Game.GameEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class TakeGameLogs {


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
        }catch (FileNotFoundException e1){
            System.out.println("File could not be opened.");
        }catch (IOException e2){
            System.out.println("Input value is not valid.");
        }
    }

}
