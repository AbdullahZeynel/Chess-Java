package Game.GameEngine;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GetDateString {

    public static String returnDateString(){
        Date date = new Date(); // date when the game starts.

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy hh:mm:ss");

        return sdf.format(date);
    }

}
