package sjakk.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import sjakk.App;
import sjakk.ChessBoard;
import sjakk.Position;
import sjakk.pieces.Piece;
import sjakk.utils.FENParser;

public class ChessGameController extends SceneSwitcher implements Initializable {
    @FXML
    private TextArea moves;

    @FXML
    private GridPane grid, backgroundBoard;

    @FXML
    private AnchorPane gridPane;

    @FXML
    private Text player1time, player2time;

    private ChessBoard chessboard;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        handleRestartGame();

        gridPane.setOnMousePressed(event -> {
            chessboard.positionPressed(new Position(event));
            System.out.println(new Position(event));
        });
    }

    @FXML
    private void handleRestartGame() {
        try {
            chessboard = new FENParser(App.class.getResourceAsStream("defaultStart.fen")).readFENFromStream();
            System.out.println("Read FEN from file");
        } catch (NullPointerException e) {
            chessboard = new FENParser().readFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
            System.out.println("Read FEN from string");
        }
        drawBoard();
    }

    @FXML
    private void drawBoard() {
        Node gridLines = grid.getChildren().get(0);
        grid.getChildren().clear();
        grid.getChildren().add(0, gridLines);

        for (Piece piece : chessboard) {
            grid.add(piece.getImage(), piece.getX(), 7 - piece.getY());
        }

        regenerateBackgroundBoard();
        colorBackgrounds();
        updateMoves();
    }

    private void regenerateBackgroundBoard() {
        if (backgroundBoard.getChildren().size() != 64) {
            backgroundBoard.getChildren().clear();
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    backgroundBoard.add(new Rectangle(50, 50), j, i);
                }
            }
        }
    }

    private void colorBackgrounds() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ((Rectangle) backgroundBoard.getChildren().get((7 - i) * 8 + j))
                        .setFill(chessboard.getGridBackgroundColor(j, i));
            }
        }
    }

    private void updateMoves() {
        moves.setText(chessboard.getMoves());
    }

    @FXML
    private void toTitleScreen() {
        insertPane("TitleScreen.fxml", baseAnchor, new TitleController());
    }

}
