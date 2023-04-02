package sjakk.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import sjakk.App;
import sjakk.ChessBoard;
import sjakk.Position;
import sjakk.pieces.Piece;
import sjakk.utils.FENParser;
import sjakk.utils.IllegalFENException;

public class ChessGameController extends SceneSwitcher {
    @FXML
    private TextArea moves;

    @FXML
    private GridPane grid, backgroundBoard;

    @FXML
    private Text player1time, player2time;

    private ChessBoard chessboard;
    private String FENString = "";

    public ChessGameController() {
    }

    public ChessGameController(String FENString) {
        this.FENString = FENString;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        handleRestartGame();
    }

    @FXML
    private void handleRestartGame() {
        try {
            if (FENString.length() > 0) {
                chessboard = new FENParser().readFEN(FENString);
                System.out.println("Read FEN from string");
            } else {
                chessboard = new FENParser(App.class.getResourceAsStream("defaultStart.fen")).readFENFromStream();
                System.out.println("Read FEN from file");
            }
        } catch (NullPointerException | IllegalFENException e) {
            chessboard = new FENParser().useDefaultFEN();
            System.out.println("Could not read FEN, using default");
            System.out.println(e.getMessage());
            // TODO: Add error message to screen
        }

        drawBoard();
    }

    @FXML
    private void handleGridPaneClicked(MouseEvent event) {
        chessboard.positionPressed(new Position(event));
        System.out.println(new Position(event));
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
        moves.setText("    WHITE | BLACK\n" + chessboard.getMoves());
        System.out.println(chessboard.getCastlingRights());
    }

    @FXML
    private void toTitleScreen() {
        insertPane("TitleScreen.fxml", baseAnchor, new TitleController());
    }

}
