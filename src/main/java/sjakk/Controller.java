package sjakk;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import sjakk.pieces.Pawn;
import sjakk.pieces.Piece;

public class Controller {
    @FXML
    private TextArea moves;

    @FXML
    private GridPane grid;

    @FXML
    private Text player1time, player2time;

    private ChessBoard chessboard;

    @FXML
    private void handleRestartGame() {
        chessboard = new ChessBoard();
        // updateMoves();
        drawBoard();
    }

    private void updateMoves() {
        chessboard.setPosition(new Position(1, 1), new Pawn(new Position(1, 1), chessboard, PieceColor.WHITE));
        chessboard.move(chessboard.getPosition(new Position(1, 1)), new Position(1, 2));
        chessboard.move(chessboard.getPosition(new Position(1, 2)), new Position(1, 4));
        chessboard.move(chessboard.getPosition(new Position(1, 4)), new Position(5, 2));
        moves.setText(chessboard.getMoves());
    }

    private void drawBoard() {
        for (Piece piece : chessboard) {
            grid.add(piece.getImage(), piece.getX(), 7 - piece.getY());
        }
    }

}
