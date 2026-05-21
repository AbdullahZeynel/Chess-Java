package Game.GameEngine;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for generating formatted date strings.
 * Used to timestamp game logs for identification and replay.
 */
public class GetDateString {

    public static String returnDateString(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy hh:mm:ss");
        return sdf.format(date);
    }
}
