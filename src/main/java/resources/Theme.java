package resources;

import java.awt.Color;

/**
 * Defines the complete color palette for a UI theme.
 * All visual components read their colors from Theme.current,
 * which can be swapped at runtime for light/dark mode switching.
 *
 * Usage: Theme.current.accent, Theme.current.lightTile, etc.
 * To switch themes: Theme.setTheme(GaziTheme.DARK) then repaint.
 */
public class Theme {

    /// The active theme — all UI components reference this
    public static Theme current;

    // ─── Board ──────────────────────────────────────────────
    public Color lightTile;
    public Color darkTile;

    // ─── Coordinates on board ───────────────────────────────
    public Color coordOnLight;
    public Color coordOnDark;

    // ─── UI Backgrounds ─────────────────────────────────────
    public Color bgPrimary;         // Main window background
    public Color bgGradientEnd;     // Gradient endpoint
    public Color bgSurface;         // Panel/card surface
    public Color bgCard;            // Card interior

    // ─── Accent & Branding ──────────────────────────────────
    public Color accent;            // Primary accent (Gazi Blue)
    public Color accentHover;       // Accent on hover
    public Color accentGold;        // Gold highlight (Gazi Gold)

    // ─── Text ───────────────────────────────────────────────
    public Color textPrimary;
    public Color textSecondary;
    public Color textMuted;

    // ─── Borders ────────────────────────────────────────────
    public Color border;

    // ─── Buttons ────────────────────────────────────────────
    public Color btnPrimary;
    public Color btnPrimaryHover;
    public Color btnSecondary;
    public Color btnSecondaryHover;
    public Color btnText;
    public Color btnSecondaryText;      // Text color on secondary buttons (may differ in light theme)
    public Color btnAccentText;

    // ─── Game State Colors ──────────────────────────────────
    public Color validMoveColor;
    public Color captureMoveColor;
    public Color checkColor;
    public Color lastMoveHighlight;

    // ─── Highlights (right-click) ───────────────────────────
    public Color highlightedSquare;
    public Color arrowColor;

    // ─── Highlighted tiles (selected) ───────────────────────
    public Color highlightedLightTile;
    public Color highlightedDarkTile;

    // ─── Promotion Panel ────────────────────────────────────
    public Color promoPanel;

    // ─── Game Over Overlay ──────────────────────────────────
    public Color gameOverOverlay;       // Semi-transparent overlay
    public Color gameOverCardBg;        // Result card background
    public Color gameOverCardBorder;    // Result card border
    public Color gameOverText;          // Result text color

    /// Switch the active theme and update Variables references
    public static void setTheme(Theme theme) {
        current = theme;
        Variables.applyTheme(theme);
    }
}
