package sjakk;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import sjakk.pieces.Piece;

public class Controller implements Initializable {
    @FXML
    private TextArea moves;

    @FXML
    private GridPane grid;

    @FXML
    private AnchorPane gridPane;

    @FXML
    private Text player1time, player2time;

    private ChessBoard chessboard;

    private Position getGridPosition(MouseEvent event) {
        return new Position((int) event.getX() / 50, 7 - (int) event.getY() / 50);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        handleRestartGame();

        gridPane.setOnMousePressed(event -> {
            chessboard.positionPressed(getGridPosition(event));
            System.out.println(getGridPosition(event));
        });
    };

    @FXML
    private void handleRestartGame() {
        chessboard = new ChessBoard();
        chessboard.initializeDefaultSetup();
        drawBoard();
    }

    private void updateMoves() {
        moves.setText(chessboard.getMoves());
    }

    @FXML
    private void drawBoard() {
        Node gridLines = grid.getChildren().get(0);
        grid.getChildren().clear();
        grid.getChildren().add(0, gridLines);

        for (Piece piece : chessboard) {
            grid.add(piece.getImage(), piece.getX(), 7 - piece.getY());
        }

        updateMoves();
    }

}
