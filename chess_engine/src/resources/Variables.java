package resources;

import java.awt.*;

//"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
public class Variables {
    public static String fenString = null;        //Might change during the game.
    public static String defaultStartingFenString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";   //Default starting position
    public static String piecesFilePath = null; // resolved at runtime

    /// Resolve the pieces.png path relative to the source directory
    static {
        // Try multiple possible locations
        String[] possiblePaths = {
            "Main/src/resources/pieces.png",
            "Chess Simulator/ChessProject v5.7/Main/src/resources/pieces.png",
            "src/resources/pieces.png",
            "resources/pieces.png"
        };

        for (String path : possiblePaths) {
            java.io.File f = new java.io.File(path);
            if (f.exists()) {
                piecesFilePath = path;
                break;
            }
        }

        // If none found, try to locate via classpath
        if (piecesFilePath == null) {
            java.net.URL url = Variables.class.getClassLoader().getResource("resources/pieces.png");
            if (url != null && url.getProtocol().equals("file")) {
                piecesFilePath = url.getPath();
            }
        }

        // Final fallback
        if (piecesFilePath == null) {
            piecesFilePath = "Main/src/resources/pieces.png";
        }
    }

    public static int tileSize = 85;

    public static int rows = 8;
    public static int cols = 8;

    /// ─── Premium Color Palette ──────────────────────────────────────────

    /// Board tile colors – warm wood inspired (lichess-like)
    public static Color whiteTileColor            = new Color(240, 217, 181);   // warm cream
    public static Color blackTileColor            = new Color(181, 136, 99);    // rich walnut

    /// Highlight & interaction colors
    public static Color highlightedSquareColor    = new Color(255, 170, 0, 120);   // amber glow
    public static Color arrowColor                = new Color(255, 170, 0, 180);   // golden arrow

    public static Color checkColor                = new Color(235, 64, 52, 200);   // vivid red
    public static Color validMoveColor            = new Color(20, 85, 30, 160);    // elegant green
    public static Color captureMoveColor          = new Color(235, 64, 52, 140);   // capture red

    public static Color highlightedWhiteTileColor = new Color(247, 236, 118);      // bright yellow
    public static Color highlightedBlackTileColor = new Color(218, 195, 71);       // dark yellow

    /// Frame & panel colors
    public static Color frameBackGroundColor      = new Color(22, 21, 18);         // near-black
    public static Color framePanelColor           = new Color(39, 37, 34);         // dark surface
    public static Color frameAccentColor          = new Color(255, 170, 0);        // amber accent
    public static Color frameTextColor            = new Color(230, 225, 215);      // warm white
    public static Color frameSubTextColor         = new Color(160, 155, 145);      // muted text
    public static Color frameBorderColor          = new Color(65, 60, 52);         // subtle border

    public static Color promotionPanelColor       = new Color(39, 37, 34, 230);

    /// Button colors
    public static Color buttonPrimaryColor        = new Color(130, 105, 60);       // golden brown
    public static Color buttonPrimaryHoverColor   = new Color(160, 130, 70);       // lighter gold
    public static Color buttonSecondaryColor      = new Color(55, 52, 47);         // dark button
    public static Color buttonSecondaryHoverColor = new Color(75, 70, 62);         // hover dark
    public static Color buttonTextColor           = new Color(255, 255, 255);      // white text
    public static Color buttonAccentTextColor     = new Color(255, 200, 87);       // gold text

    /// Dimensions
    public static Dimension defaultDimention      = new Dimension(1000, 1000);
    public static Dimension boardDimention        = new Dimension(cols * tileSize, rows * tileSize);
    public static Dimension promoPanelDimention   = new Dimension(tileSize, 4 * tileSize);

    /// Coordinate label colors
    public static Color coordLightColor           = new Color(181, 136, 99);       // on light tiles
    public static Color coordDarkColor            = new Color(240, 217, 181);      // on dark tiles
}