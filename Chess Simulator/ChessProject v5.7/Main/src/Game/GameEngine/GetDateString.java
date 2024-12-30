package Game.GameEngine;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GetDateString {

    // This class will be used for identifying the games by their started time.
    // When identified, they can be used to analyze the game.

    public static String returnDateString(){
        Date date = new Date(); // date when the game starts.

        // Takes the current time in the specified format.
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy hh:mm:ss");

        return sdf.format(date);
    }

}
