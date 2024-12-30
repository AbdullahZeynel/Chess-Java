package GUI.Board;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessClock {
    private Timer whiteTimer;
    private Timer blackTimer;
    private int whiteTime;
    private int blackTime;
    private JLabel whiteLabel;
    private JLabel blackLabel;

    public ChessClock(int whiteTimeInSeconds, int blackTimeInSeconds, JLabel whiteLabel, JLabel blackLabel) {
        
        // Initializing the required fields.
        this.whiteTime = whiteTimeInSeconds;
        this.blackTime = blackTimeInSeconds;
        this.whiteLabel = whiteLabel;
        this.blackLabel = blackLabel;

        this.whiteLabel.setBounds(1310, 170, 300, 80);
        this.whiteLabel.setOpaque(true);
        this.whiteLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.whiteLabel.setBackground(Color.DARK_GRAY);
        this.whiteLabel.setForeground(Color.WHITE);
        this.whiteLabel.setFont(new Font("Arial", Font.PLAIN, 25));

        this.blackLabel.setBounds(1310, 400, 300, 80);
        this.blackLabel.setOpaque(true);
        this.blackLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.blackLabel.setBackground(Color.DARK_GRAY);
        this.blackLabel.setForeground(Color.WHITE);
        this.blackLabel.setFont(new Font("Arial", Font.PLAIN, 25));


        // Initializing the white timer.
        whiteTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (whiteTime > 0) {
                    whiteTime--;
                    updateLabel(whiteLabel, whiteTime);
                } else {
                    whiteTimer.stop();
                    // Handle timer expiration
                }
            }
        });

        // Initializing  the black timer.
        blackTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (blackTime > 0) {
                    blackTime--;
                    updateLabel(blackLabel, blackTime);
                } else {
                    blackTimer.stop();
                    // Handle timer expiration. Whoops forgor to handle this before the presentation. And Ayberk teacher noticed!
                }
            }
        });

        // This code will make our labels that we input to our choice.
        updateLabel(whiteLabel, whiteTime);
        updateLabel(blackLabel, blackTime);
    }

    // update the label with the remaining time
    private void updateLabel(JLabel label, int timeInSeconds) {
        int minutes = timeInSeconds / 60;
        int seconds = timeInSeconds % 60;
        label.setText(String.format("%02d:%02d", minutes, seconds));
    }

    // start the white timer
    public void startWhiteTimer() {
        blackTimer.stop();
        whiteTimer.start();
    }

    // start the black timer
    public void startBlackTimer() {
        whiteTimer.stop();
        blackTimer.start();
    }

    // This is the method used for switching the timer after every round.
    public void switchTimer(){
        if(whiteTimer.isRunning()){
            startBlackTimer();
        } else if(blackTimer.isRunning()){
            startWhiteTimer();
        }

    }

    // Stopping the timers
    public void setTimers(){
        whiteTimer.stop();
        blackTimer.stop();
    }

    // Stopping the timers and resetting their values. Use THIS after a match ending.
    public void resetTimers(int whiteTimeInSeconds, int blackTimeInSeconds) {
        whiteTimer.stop();
        blackTimer.stop();
        this.whiteTime = whiteTimeInSeconds;
        this.blackTime = blackTimeInSeconds;
        updateLabel(whiteLabel, whiteTime);
        updateLabel(blackLabel, blackTime);
    }

    //check if the white timer has expired
    public boolean isWhiteTimeOver() {
        return whiteTime <= 0;
    }

    // check if the black timer has expired
    public boolean isBlackTimeOver() {
        return blackTime <= 0;
    }


}
