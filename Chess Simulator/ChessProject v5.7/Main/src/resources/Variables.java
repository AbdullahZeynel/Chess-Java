package resources;

import java.awt.*;

//"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

public class Variables {
    public static String fenString = null;        //Might change during the game.
    public static String defaultStartingFenString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";   //Default starting position
    public static String piecesFilePath = "Main/src/resources/pieces.png";

    public static int tileSize = 85;

    public static int rows = 8;
    public static int cols = 8;

    public static Color highlightedSquareColor    = new Color(186, 215, 214, 255);    ///highlighted move color
    public static Color arrowColor                = new Color(143, 184, 182);

    public static Color whiteTileColor            = new Color(245, 235, 220);
    public static Color blackTileColor            = new Color(90, 90, 90, 255);

    public static Color checkColor                = new Color(219, 58, 65, 208);
    public static Color validMoveColor            = new Color(119, 167, 119, 194);
    public static Color captureMoveColor          = new Color(95, 127, 95, 194);

    public static Color highlightedWhiteTileColor = new Color(255, 242, 212);
    public static Color highlightedBlackTileColor = new Color(120, 120, 120, 255);

    public static Color frameBackGroundColor      = new Color(23, 21, 19);

    public static Color promotionPanelColor       = new Color(147, 147, 147, 157);


    public static Dimension defaultDimention      = new Dimension(1000, 1000);
    public static Dimension boardDimention        = new Dimension(cols * tileSize, rows * tileSize);
    public static Dimension promoPanelDimention   = new Dimension(tileSize, 4 * tileSize);


}


/*
    public static Color highlightedSquareColor    = new Color(71, 143, 138, 255);    ///highlighted move color
    public static Color arrowColor                = new Color (127, 204, 198);

    public static Color whiteTileColor            = new Color(240, 217, 181);
    public static Color blackTileColor            = new Color(69, 69, 69, 255);

    public static Color checkColor                = new Color(140, 13, 15, 208);
    public static Color validMoveColor            = new Color(78, 115, 78, 194);
    public static Color captureMoveColor          = new Color(41, 69, 41, 194);

    public static Color highlightedWhiteTileColor = new Color(246, 213, 161);
    public static Color highlightedBlackTileColor = new Color(92, 92, 92, 255);

    public static Color frameBackGroundColor      = new Color(23, 21, 19);

 */