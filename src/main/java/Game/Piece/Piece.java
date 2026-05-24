package Game.Piece;

import Game.GameEngine.ChessEngine;
import resources.Variables;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

/**
 * Abstract base class for all chess pieces.
 * Handles sprite loading from the piece sheet image and provides
 * common properties like position, color, and name.
 *
 * Each subclass must implement the PieceMoves interface to define
 * its specific movement rules.
 *
 * OPTIMIZATION: The sprite sheet is loaded ONCE (static), and all 12
 * piece sprites (6 types × 2 colors) are pre-scaled and cached.
 * This avoids repeated disk I/O and expensive SCALE_SMOOTH calls
 * for every piece instance (~32 pieces per game).
 */
public abstract class Piece implements PieceMoves {

    public int xPos, yPos;      /// Pixel position for rendering
    public int col, row;        /// Board position (0-7)

    public boolean isWhite;
    public String name;         /// Human-readable piece name (e.g., "King", "Pawn")
    public String pieceChar;    /// FEN character representation (e.g., "K", "P")
    public int value;           /// Material value for evaluation

    protected Image sprite;
    protected ChessEngine engine;

    public boolean isFirstMove = true;  /// Tracks whether the piece has moved (for castling, pawn double push)

    /// ─── Static Sprite Cache ───────────────────────────────────────────
    /// Loaded once, shared across all Piece instances.
    /// Key format: "column_row" (e.g., "0_0" = white king, "5_1" = black pawn)
    protected static BufferedImage sheet;
    protected static int sheetScale;
    private static final HashMap<String, Image> spriteCache = new HashMap<>();

    static {
        try {
            sheet = ImageIO.read(Variables.piecesResourceUrl);
            sheetScale = sheet.getWidth() / 6;
            preloadSprites();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /// Pre-scales all 12 piece sprites at startup (6 types × 2 colors).
    /// Uses SCALE_SMOOTH once per sprite instead of once per piece instance.
    private static void preloadSprites() {
        for (int col = 0; col < 6; col++) {
            for (int row = 0; row < 2; row++) {
                String key = col + "_" + row;
                Image scaled = sheet.getSubimage(col * sheetScale, row * sheetScale, sheetScale, sheetScale)
                        .getScaledInstance(Variables.tileSize, Variables.tileSize, BufferedImage.SCALE_SMOOTH);
                spriteCache.put(key, scaled);
            }
        }
    }

    /// Retrieves a pre-cached sprite by sheet position and color.
    /// This replaces the per-piece getSubimage + getScaledInstance calls.
    protected static Image getCachedSprite(int sheetCol, boolean isWhite) {
        String key = sheetCol + "_" + (isWhite ? 0 : 1);
        return spriteCache.get(key);
    }


    public Piece(ChessEngine engine) {
        this.engine = engine;
    }

    /// Draws the piece sprite at its current pixel position
    public void paint(Graphics g2d) {
        g2d.drawImage(sprite, xPos, yPos, null);
    }

    /// Draws the piece sprite at an explicit pixel position (for flip-aware rendering)
    public void paintAt(Graphics g2d, int px, int py) {
        g2d.drawImage(sprite, px, py, null);
    }

    // Abstract pieceMoves() is inherited from PieceMoves interface — no need to override here
}
