package GUI.Board;

import resources.Variables;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

/**
 * Premium chess clock with active/inactive styling, low-time warnings,
 * and time increment support.
 *
 * Each clock is a custom-painted JLabel with:
 * - Player name ("White" / "Black") with chess piece icon
 * - Large monospaced time display
 * - Active state: amber-highlighted border
 * - Low-time state: pulsing red warning
 */
public class ChessClock {
    private Timer whiteTimer;
    private Timer blackTimer;
    private int whiteTime;
    private int blackTime;
    private JLabel whiteLabel;
    private JLabel blackLabel;

    private boolean whiteActive = false;
    private boolean blackActive = false;

    public ChessClock(int whiteTimeInSeconds, int blackTimeInSeconds, JLabel whiteLabel, JLabel blackLabel) {
        this.whiteTime  = whiteTimeInSeconds;
        this.blackTime  = blackTimeInSeconds;
        this.whiteLabel = whiteLabel;
        this.blackLabel = blackLabel;

        // Style both clock panels
        styleClockLabel(whiteLabel);
        styleClockLabel(blackLabel);

        // Initialize white timer
        whiteTimer = new Timer(1000, e -> {
            if (whiteTime > 0) {
                whiteTime--;
                updateLabel(whiteLabel, whiteTime, true, whiteActive);
            } else {
                whiteTimer.stop();
            }
        });

        // Initialize black timer
        blackTimer = new Timer(1000, e -> {
            if (blackTime > 0) {
                blackTime--;
                updateLabel(blackLabel, blackTime, false, blackActive);
            } else {
                blackTimer.stop();
            }
        });

        // Set initial display
        updateLabel(whiteLabel, whiteTime, true, false);
        updateLabel(blackLabel, blackTime, false, false);
    }

    /// Apply base styling — the actual painting is done via HTML content
    private void styleClockLabel(JLabel label) {
        label.setOpaque(false);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(200, 80));
    }

    /// Update the label with rich HTML content showing player name + time.
    /// Colors are read from the active theme via Variables.
    private void updateLabel(JLabel label, int timeInSeconds, boolean isWhite, boolean isActive) {
        int minutes = timeInSeconds / 60;
        int seconds = timeInSeconds % 60;
        String timeStr = String.format("%02d:%02d", minutes, seconds);

        String icon       = isWhite ? "\u2654" : "\u265A";
        String playerName = isWhite ? "White" : "Black";

        // Theme-aware color scheme based on state
        String timeColor;
        String bgColor;
        String borderColor;
        String nameColor = colorToHex(Variables.frameSubTextColor);
        boolean dark = isDarkBg();

        if (timeInSeconds <= 10) {
            timeColor   = "#ff3333";
            bgColor     = dark ? "#3a1818" : "#fff0f0";
            borderColor = "#ff3333";
        } else if (timeInSeconds <= 30) {
            timeColor   = "#ff8844";
            bgColor     = dark ? "#332211" : "#fff5e6";
            borderColor = "#ff8844";
        } else if (isActive) {
            timeColor   = colorToHex(Variables.frameTextColor);
            bgColor     = dark ? "#1a2a3a" : "#e8f4ff";
            borderColor = colorToHex(Variables.frameAccentColor);
        } else {
            timeColor   = colorToHex(Variables.frameSubTextColor);
            bgColor     = colorToHex(Variables.buttonSecondaryColor);
            borderColor = colorToHex(Variables.frameBorderColor);
        }

        String html = String.format(
            "<html><div style='" +
                "background-color:%s;" +
                "border:2px solid %s;" +
                "border-radius:12px;" +
                "padding:8px 16px;" +
                "text-align:center;" +
                "width:160px;" +
                "'>" +
                "<span style='color:%s;font-size:12px;'>%s %s</span><br/>" +
                "<span style='color:%s;font-size:30px;font-family:monospace;font-weight:bold;'>%s</span>" +
            "</div></html>",
            bgColor, borderColor, nameColor, icon, playerName, timeColor, timeStr
        );

        label.setText(html);
    }

    /// Refresh both clock displays with current theme colors (call after theme switch)
    public void refreshDisplay() {
        updateLabel(whiteLabel, whiteTime, true, whiteActive);
        updateLabel(blackLabel, blackTime, false, blackActive);
    }

    /// Convert a Color to CSS hex string
    private String colorToHex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }

    /// Check if current theme is dark (bg brightness < 128)
    private boolean isDarkBg() {
        Color bg = Variables.frameBackGroundColor;
        return (bg.getRed() + bg.getGreen() + bg.getBlue()) / 3 < 128;
    }

    /// Add time increment after a move
    public void addIncrement(boolean isWhite, int incrementSeconds) {
        if (incrementSeconds <= 0) return;
        if (isWhite) {
            whiteTime += incrementSeconds;
            updateLabel(whiteLabel, whiteTime, true, whiteActive);
        } else {
            blackTime += incrementSeconds;
            updateLabel(blackLabel, blackTime, false, blackActive);
        }
    }

    /// Start white's clock and stop black's
    public void startWhiteTimer() {
        blackTimer.stop();
        blackActive = false;
        whiteActive = true;
        whiteTimer.start();
        updateLabel(whiteLabel, whiteTime, true, true);
        updateLabel(blackLabel, blackTime, false, false);
    }

    /// Start black's clock and stop white's
    public void startBlackTimer() {
        whiteTimer.stop();
        whiteActive = false;
        blackActive = true;
        blackTimer.start();
        updateLabel(whiteLabel, whiteTime, true, false);
        updateLabel(blackLabel, blackTime, false, true);
    }

    /// Switch to the other player's clock
    public void switchTimer() {
        if (whiteTimer.isRunning()) {
            startBlackTimer();
        } else if (blackTimer.isRunning()) {
            startWhiteTimer();
        }
    }

    /// Stop both timers
    public void setTimers() {
        whiteTimer.stop();
        blackTimer.stop();
        whiteActive = false;
        blackActive = false;
    }

    /// Reset both timers to new values
    public void resetTimers(int whiteTimeInSeconds, int blackTimeInSeconds) {
        setTimers();
        this.whiteTime = whiteTimeInSeconds;
        this.blackTime = blackTimeInSeconds;
        updateLabel(whiteLabel, whiteTime, true, false);
        updateLabel(blackLabel, blackTime, false, false);
    }

    public boolean isWhiteTimeOver() {
        return whiteTime <= 0;
    }

    public boolean isBlackTimeOver() {
        return blackTime <= 0;
    }
}
