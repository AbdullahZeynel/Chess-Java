package resources;

import java.awt.*;

/**
 * Global constants and theme-driven color palette.
 *
 * Colors are initialized from GaziTheme.DARK at startup and can be
 * swapped at runtime via Theme.setTheme() → Variables.applyTheme().
 *
 * Non-color constants (dimensions, FEN strings, file paths) remain static.
 */
///"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
public class Variables {
    public static String fenString = null;        //Might change during the game.
    public static String defaultStartingFenString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";   //Default starting position
    public static String piecesFilePath = null; // resolved at runtime

    /// Resolve the pieces.png path relative to the working directory
    static {
        String[] possiblePaths = {
            "resources/pieces.png",
            "chess_engine/src/resources/pieces.png",
            "src/resources/pieces.png"
        };

        for (String path : possiblePaths) {
            java.io.File f = new java.io.File(path);
            if (f.exists()) {
                piecesFilePath = path;
                break;
            }
        }

        // Try classpath as fallback
        if (piecesFilePath == null) {
            java.net.URL url = Variables.class.getClassLoader().getResource("resources/pieces.png");
            if (url != null && url.getProtocol().equals("file")) {
                piecesFilePath = url.getPath();
            }
        }

        // Final fallback
        if (piecesFilePath == null) {
            piecesFilePath = "resources/pieces.png";
        }

        // Initialize default theme
        Theme.setTheme(GaziTheme.DARK);
    }

    public static int tileSize = 85;

    public static int rows = 8;
    public static int cols = 8;

    // ─── Theme-Driven Colors ─────────────────────────────────────────
    // These are updated by applyTheme() when the theme changes.

    /// Board tile colors
    public static Color whiteTileColor;
    public static Color blackTileColor;

    /// Highlight & interaction colors
    public static Color highlightedSquareColor;
    public static Color arrowColor;

    public static Color checkColor;
    public static Color validMoveColor;
    public static Color captureMoveColor;

    public static Color highlightedWhiteTileColor;
    public static Color highlightedBlackTileColor;

    /// Frame & panel colors
    public static Color frameBackGroundColor;
    public static Color frameGradientEndColor;
    public static Color framePanelColor;
    public static Color frameAccentColor;
    public static Color frameTextColor;
    public static Color frameSubTextColor;
    public static Color frameBorderColor;

    public static Color promotionPanelColor;

    /// Button colors
    public static Color buttonPrimaryColor;
    public static Color buttonPrimaryHoverColor;
    public static Color buttonSecondaryColor;
    public static Color buttonSecondaryHoverColor;
    public static Color buttonTextColor;
    public static Color buttonSecondaryTextColor;
    public static Color buttonAccentTextColor;

    /// Coordinate label colors
    public static Color coordLightColor;
    public static Color coordDarkColor;

    /// Game Over overlay
    public static Color gameOverOverlayColor;
    public static Color gameOverCardBgColor;
    public static Color gameOverCardBorderColor;
    public static Color gameOverTextColor;

    /// Dimensions
    public static Dimension defaultDimention      = new Dimension(1000, 1000);
    public static Dimension boardDimention        = new Dimension(cols * tileSize, rows * tileSize);
    public static Dimension promoPanelDimention   = new Dimension(tileSize, 4 * tileSize);

    /// Applies all colors from the given theme to the static fields.
    /// Called by Theme.setTheme() whenever the active theme changes.
    public static void applyTheme(Theme theme) {
        // Board
        whiteTileColor            = theme.lightTile;
        blackTileColor            = theme.darkTile;

        // Highlights
        highlightedSquareColor    = theme.highlightedSquare;
        arrowColor                = theme.arrowColor;

        // Game state
        checkColor                = theme.checkColor;
        validMoveColor            = theme.validMoveColor;
        captureMoveColor          = theme.captureMoveColor;

        // Highlighted tiles
        highlightedWhiteTileColor = theme.highlightedLightTile;
        highlightedBlackTileColor = theme.highlightedDarkTile;

        // Frame
        frameBackGroundColor      = theme.bgPrimary;
        frameGradientEndColor     = theme.bgGradientEnd;
        framePanelColor           = theme.bgSurface;
        frameAccentColor          = theme.accent;
        frameTextColor            = theme.textPrimary;
        frameSubTextColor         = theme.textMuted;
        frameBorderColor          = theme.border;

        // Promotion
        promotionPanelColor       = theme.promoPanel;

        // Buttons
        buttonPrimaryColor        = theme.btnPrimary;
        buttonPrimaryHoverColor   = theme.btnPrimaryHover;
        buttonSecondaryColor      = theme.btnSecondary;
        buttonSecondaryHoverColor = theme.btnSecondaryHover;
        buttonTextColor           = theme.btnText;
        buttonSecondaryTextColor  = theme.btnSecondaryText;
        buttonAccentTextColor     = theme.btnAccentText;

        // Coordinates
        coordLightColor           = theme.coordOnLight;
        coordDarkColor            = theme.coordOnDark;

        // Game Over
        gameOverOverlayColor      = theme.gameOverOverlay;
        gameOverCardBgColor       = theme.gameOverCardBg;
        gameOverCardBorderColor   = theme.gameOverCardBorder;
        gameOverTextColor         = theme.gameOverText;
    }
}