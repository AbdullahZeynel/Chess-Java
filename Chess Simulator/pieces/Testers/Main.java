package pieces.Testers;

import main.Board;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<String> pieceColor = Arrays.asList("White", "Dark");
        List<String> pieceNames = Arrays.asList("King", "Queen", "Bishop", "Knight", "Rook", "Pawn");
        String finalName;

        for(String Color: pieceColor)
            for(String Name: pieceNames){
                finalName = Color + Name;

                boolean color = Color.equals("White");
                Board board = new Board();
                ImageDivider piece = new ImageDivider(board, color, finalName, pieceNames.indexOf(Name));
            }

    }
}
