package Game.GameEngine;

import Game.Piece.*;
import resources.Variables;

import javax.swing.JLabel;
import java.awt.image.BufferedImage;

/**
 * Three Checks Chess variant — extends the standard ChessEngine.
 * In this variant, delivering three checks to the opponent's king wins the game,
 * in addition to all standard win conditions (checkmate, time, etc.).
 *
 * Architecture:
 *   - Overrides createPiece() and loadPositionFromFen() to use ThreeChecksKing
 *   - Overrides updateGameState() to add check counting and three-checks win condition
 *   - Check counters track how many checks each side has RECEIVED
 */
public class ThreeChecksChess extends ChessEngine {

    /// How many checks white's king has received (if reaches 3, black wins)
    public int whiteChecksReceived = 0;
    /// How many checks black's king has received (if reaches 3, white wins)
    public int blackChecksReceived = 0;

    public ThreeChecksChess() {
        super();
        this.gameMode = "ThreeChecks";
    }

    /// UI labels for displaying check counts — set by Frame.setGame()
    public JLabel whiteCheckIndicator;
    public JLabel blackCheckIndicator;

    /**
     * Factory method override — creates ThreeChecksKing instead of King.
     * All other pieces delegate to the parent's createPiece().
     */
    @Override
    protected Piece createPiece(Character pieceCharacter, int col, int row, boolean isWhite) {
        if (Character.toLowerCase(pieceCharacter) == 'k') {
            return new ThreeChecksKing(this, col, row, isWhite);
        }
        return super.createPiece(pieceCharacter, col, row, isWhite);
    }

    /**
     * Overrides the FEN position loader to ensure kings are created as ThreeChecksKing.
     * All other piece types are created normally.
     */
    @Override
    protected void loadPositionFromFen(String fenPosition) {
        int row = 0;
        int col = 0;

        for (int i = 0; i < fenPosition.length(); i++) {
            char fenChar = fenPosition.charAt(i);
            if (fenChar == '/') {
                row++;
                col = 0;
            } else if (Character.isDigit(fenChar)) {
                col += Character.getNumericValue(fenChar);
            } else {
                boolean isWhite = Character.isUpperCase(fenChar);
                char pieceChar = Character.toLowerCase(fenChar);

                switch (pieceChar) {
                    case 'r': pieceList.add(new Rook   (this, col, row, isWhite)); break;
                    case 'n': pieceList.add(new Knight (this, col, row, isWhite)); break;
                    case 'b': pieceList.add(new Bishop (this, col, row, isWhite)); break;
                    case 'q': pieceList.add(new Queen  (this, col, row, isWhite)); break;
                    case 'k': pieceList.add(new ThreeChecksKing(this, col, row, isWhite)); break;
                    case 'p': pieceList.add(new Pawn   (this, col, row, isWhite)); break;
                }
                col++;
            }
        }
    }

    /**
     * Extended game state evaluation for Three Checks variant.
     *
     * After each move, checks whether the opponent's king is currently in check.
     * If so, increments the check counter. If the counter reaches 3,
     * the game ends with a win for the checking side.
     *
     * Note: This runs AFTER isWhiteToMove has been toggled in makeMove().
     * So if isWhiteToMove == true, it means BLACK just moved.
     *   - We check if white's king is in check (black delivered a check → whiteChecksReceived++)
     *   - We check if black's king is in check (shouldn't happen, but for safety)
     *
     * Standard win conditions (checkmate, stalemate, insufficient material)
     * are handled by super.updateGameState() which runs first.
     */
    @Override
    protected void updateGameState() {
        /// First, check standard game-over conditions (checkmate, stalemate, insufficient material)
        /// If the game is already over from checkmate/stalemate, don't override it
        super.updateGameState();

        if (isGameOver) {
            return; /// Game already ended via checkmate, stalemate, or insufficient material
        }

        /// Now check the three-checks win condition
        /// After makeMove(), isWhiteToMove has been toggled.
        /// The side that is NOW to move is the side whose king we check for being in check.
        Piece currentKing = findKing(isWhiteToMove);

        if (currentKing != null) {
            Move checkMove = new Move(this, currentKing, currentKing.col, currentKing.row);
            if (checkScanner.isKingChecked(checkMove)) {
                /// The current side's king is in check — the opponent just delivered a check
                if (isWhiteToMove) {
                    /// White's king is in check (black delivered the check)
                    whiteChecksReceived++;
                    if (currentKing instanceof ThreeChecksKing) {
                        ((ThreeChecksKing) currentKing).checksReceived = whiteChecksReceived;
                    }
                    if (whiteChecksReceived >= 3) {
                        gameOverMessage = "Black Wins by Three Checks!";
                        isGameOver = true;
                        if (timers != null) timers.setTimers();
                        board.repaint();
                    }
                } else {
                    /// Black's king is in check (white delivered the check)
                    blackChecksReceived++;
                    if (currentKing instanceof ThreeChecksKing) {
                        ((ThreeChecksKing) currentKing).checksReceived = blackChecksReceived;
                    }
                    if (blackChecksReceived >= 3) {
                        gameOverMessage = "White Wins by Three Checks!";
                        isGameOver = true;
                        if (timers != null) timers.setTimers();
                        board.repaint();
                    }
                }
            }
        }

        /// Repaint check indicator labels (positioned by Frame, outside the board)
        if (whiteCheckIndicator != null) whiteCheckIndicator.repaint();
        if (blackCheckIndicator != null) blackCheckIndicator.repaint();
    }
}