package resources;

import java.awt.Color;

/**
 * Gazi University branded themes — Light and Dark variants.
 *
 * Official Gazi University corporate colors:
 *   - Gazi Blue (primary):  #006699  RGB(0, 102, 153)
 *   - Silver:               #C0C0C0  RGB(192, 192, 192)
 *   - Gold:                 #FFD700  RGB(255, 215, 0)
 *   - Grey:                 #808080  RGB(128, 128, 128)
 *
 * Design philosophy:
 *   - Dark theme: near-black bg, white/grey text, blue accents sparingly
 *   - Light theme: warm white bg, dark text, blue accents
 *   - Move highlights: green tones for visibility
 *   - Board: high-contrast tiles for piece clarity
 *
 * v6.3 NOTE: This structure supports future per-field overrides
 * for a theme customization panel (different tile sets, piece sets, etc.)
 */
public class GaziTheme {

    /// ─── Dark Theme ────────────────────────────────────────
    public static final Theme DARK = createDarkTheme();

    /// ─── Light Theme ───────────────────────────────────────
    public static final Theme LIGHT = createLightTheme();

    private static Theme createDarkTheme() {
        Theme t = new Theme();

        // Board — high contrast for piece visibility
        t.lightTile             = new Color(210, 210, 210);     // Bright silver
        t.darkTile              = new Color(105, 105, 105);     // Medium grey (not too dark)

        // Coordinates
        t.coordOnLight          = new Color(105, 105, 105);
        t.coordOnDark           = new Color(210, 210, 210);

        // UI Backgrounds — near-black
        t.bgPrimary             = new Color(15, 15, 15);        // Near-black
        t.bgGradientEnd         = new Color(20, 20, 25);        // Slightly blue-black
        t.bgSurface             = new Color(28, 28, 32);        // Dark grey surface
        t.bgCard                = new Color(28, 28, 32);

        // Accent — Gazi Blue used sparingly
        t.accent                = new Color(0, 130, 190);       // Bright Gazi Blue
        t.accentHover           = new Color(0, 160, 220);
        t.accentGold            = new Color(255, 215, 0);       // Gold

        // Text — white/grey dominant
        t.textPrimary           = new Color(245, 245, 245);     // White
        t.textSecondary         = new Color(200, 200, 200);     // Light grey
        t.textMuted             = new Color(140, 140, 140);     // Medium grey

        // Borders
        t.border                = new Color(55, 55, 60);

        // Buttons
        t.btnPrimary            = new Color(0, 102, 153);       // Gazi Blue
        t.btnPrimaryHover       = new Color(0, 130, 190);
        t.btnSecondary          = new Color(38, 38, 42);
        t.btnSecondaryHover     = new Color(50, 50, 55);
        t.btnText               = new Color(255, 255, 255);
        t.btnSecondaryText      = new Color(240, 240, 240);
        t.btnAccentText         = new Color(0, 170, 230);

        // Game state — GREEN highlights for move visibility (high opacity)
        t.validMoveColor        = new Color(50, 180, 80, 190);  // Green, clearly visible
        t.captureMoveColor      = new Color(220, 60, 60, 200);  // Red, strong
        t.checkColor            = new Color(220, 50, 50, 180);  // Red check
        t.lastMoveHighlight     = new Color(255, 215, 0, 60);   // Gold 25%

        // Highlights (right-click) — GREEN to match move highlights
        t.highlightedSquare     = new Color(50, 180, 80, 140);
        t.arrowColor            = new Color(50, 180, 80, 200);

        // Highlighted tiles (selected piece)
        t.highlightedLightTile  = new Color(100, 190, 120);
        t.highlightedDarkTile   = new Color(55, 140, 75);

        // Promotion
        t.promoPanel            = new Color(28, 28, 32, 240);

        // Game Over overlay
        t.gameOverOverlay       = new Color(0, 0, 0, 120);         // Subtle dark overlay
        t.gameOverCardBg        = new Color(28, 28, 32, 245);      // Dark card
        t.gameOverCardBorder    = new Color(0, 130, 190);           // Blue border
        t.gameOverText          = new Color(245, 245, 245);         // White text

        return t;
    }

    private static Theme createLightTheme() {
        Theme t = new Theme();

        // Board — warm, high contrast
        t.lightTile             = new Color(240, 235, 225);     // Warm off-white
        t.darkTile              = new Color(140, 130, 120);     // Warm brown-grey

        // Coordinates
        t.coordOnLight          = new Color(140, 130, 120);
        t.coordOnDark           = new Color(240, 235, 225);

        // UI Backgrounds — clean, warm
        t.bgPrimary             = new Color(242, 240, 236);     // Warm light
        t.bgGradientEnd         = new Color(230, 228, 224);     // Slightly darker
        t.bgSurface             = new Color(255, 255, 255);     // White
        t.bgCard                = new Color(255, 255, 255);

        // Accent — Gazi Blue
        t.accent                = new Color(0, 102, 153);       // Gazi Blue
        t.accentHover           = new Color(0, 130, 190);
        t.accentGold            = new Color(190, 155, 0);       // Darker gold for light bg

        // Text — dark for readability
        t.textPrimary           = new Color(25, 25, 25);        // Near-black
        t.textSecondary         = new Color(60, 60, 60);
        t.textMuted             = new Color(120, 120, 120);

        // Borders
        t.border                = new Color(200, 198, 194);     // Warm grey

        // Buttons
        t.btnPrimary            = new Color(0, 102, 153);
        t.btnPrimaryHover       = new Color(0, 130, 190);
        t.btnSecondary          = new Color(238, 236, 232);     // Warm light grey
        t.btnSecondaryHover     = new Color(225, 222, 216);
        t.btnText               = new Color(255, 255, 255);
        t.btnSecondaryText      = new Color(30, 30, 30);
        t.btnAccentText         = new Color(0, 102, 153);

        // Game state — GREEN highlights (high opacity)
        t.validMoveColor        = new Color(40, 160, 70, 190);  // Green
        t.captureMoveColor      = new Color(210, 50, 50, 200);  // Red
        t.checkColor            = new Color(210, 50, 50, 170);
        t.lastMoveHighlight     = new Color(255, 200, 0, 70);

        // Highlights — GREEN
        t.highlightedSquare     = new Color(40, 160, 70, 140);
        t.arrowColor            = new Color(40, 160, 70, 200);

        // Highlighted tiles
        t.highlightedLightTile  = new Color(110, 200, 130);
        t.highlightedDarkTile   = new Color(65, 150, 80);

        // Promotion
        t.promoPanel            = new Color(255, 255, 255, 240);

        // Game Over overlay
        t.gameOverOverlay       = new Color(0, 0, 0, 80);          // Very subtle
        t.gameOverCardBg        = new Color(255, 255, 255, 245);   // White card
        t.gameOverCardBorder    = new Color(0, 102, 153);          // Blue border
        t.gameOverText          = new Color(25, 25, 25);           // Dark text

        return t;
    }
}
