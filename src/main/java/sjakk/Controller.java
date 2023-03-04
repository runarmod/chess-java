package sjakk;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import sjakk.pieces.Pawn;

public class Controller {
    @FXML
    private TextArea moves;

    private ChessBoard chessboard;

    @FXML
    private void handleRestartGame() {
        chessboard = new ChessBoard();
        updateMoves();
    }

    private void updateMoves() {
        chessboard.setPosition(new Position(1, 1), new Pawn(new Position(1, 1), chessboard, Color.WHITE));
        chessboard.move(chessboard.getPosition(new Position(1, 1)), new Position(1, 2));
        chessboard.move(chessboard.getPosition(new Position(1, 2)), new Position(1, 4));
        chessboard.move(chessboard.getPosition(new Position(1, 4)), new Position(5, 2));
        moves.setText(chessboard.getMoves());
    }

}
