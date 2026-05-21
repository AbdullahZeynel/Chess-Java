package Game.Piece;

import Game.GameEngine.ChessEngine;
import resources.Variables;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Abstract base class for all chess pieces.
 * Handles sprite loading from the piece sheet image and provides
 * common properties like position, color, and name.
 *
 * Each subclass must implement the PieceMoves interface to define
 * its specific movement rules.
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

    BufferedImage sheet;

    {
        try{
            sheet = ImageIO.read(new File(Variables.piecesFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /// The sprite sheet has 6 columns (one per piece type)
    protected int sheetScale = sheet.getWidth()/6;


    public Piece(ChessEngine engine) {
        this.engine = engine;
    }

    /// Draws the piece sprite at its current pixel position
    public void paint(Graphics g2d) {
        g2d.drawImage(sprite, xPos, yPos, null);
    }

    // Abstract pieceMoves() is inherited from PieceMoves interface — no need to override here
}
