package Game.GameEngine;

import Game.GameEngine.User.User;
import Game.Piece.*;

public class ThreeChecksChess extends ChessEngine {

}

/*
    public ThreeChecksChess(User host) {
        super(host);
        this.gameMode = "ThreeChecksChess";
    }

    /// Overloading
    public ThreeChecksChess(User host, User player2) {
        super(host);
        this.gameMode = "ThreeChecksChess";
    }

    @Override
    protected void updateGameState() {
        //checksCountIncrement();
        super.updateGameState();
    }

    @Override
    protected Piece createPiece(Character pieceCharacter, int col, int row, boolean isWhite) {
        switch (Character.toLowerCase(pieceCharacter)) {
            case 'r':
                return new Rook(this, col, row, isWhite);
            case 'n':
                return new Knight(this, col, row, isWhite);
            case 'b':
                return new Bishop(this, col, row, isWhite);
            case 'q':
                return new Queen(this, col, row, isWhite);
            case 'k':
                return new ThreeChecksKing(this, col, row, isWhite);
            case 'p':
                return new Pawn(this, col, row, isWhite);
            default:
                throw new IllegalArgumentException("Invalid piece name: " + pieceCharacter);
        }
    }


    protected void checkChecksCount(Move move){
        Piece piece = findKing(move.piece.isWhite);
        ThreeChecksKing king = (ThreeChecksKing) piece;

        if(king.checksCount > 3)
            isGameOver = true;
    }

    protected void checksCountIncrement(Move move){
        System.out.println("entered checksCountIncrement");
        if(checkScanner.isKingChecked(move)) {
            System.out.println("check found");
            move.isKingChecked = true;
            Piece piece = findKing(!(move.piece.isWhite));
            ThreeChecksKing king = (ThreeChecksKing) piece;

            king.checksCount++;
            System.out.println(king.checksCount);
        }
    }

    public void InvokeThreeChecksChess(ThreeChecksKing king) {
        king.isChecked = true;
        king.checksCount++;
    }

}

 */