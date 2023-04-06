package sjakk.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import sjakk.ChessBoard;
import sjakk.Position;
import sjakk.pieces.Piece;
import sjakk.utils.FENParser;
import sjakk.utils.IllegalFENException;
import sjakk.utils.PopUp;

public class ChessGameController extends SceneSwitcher {
    @FXML
    private TextArea moves;

    @FXML
    private GridPane grid, backgroundBoard;

    @FXML
    private Text player1time, player2time;

    private ChessBoard chessboard;
    private String FENString = "";
    private boolean hasShownGameOver = false;

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
                chessboard = FENParser.getBoardFromFEN(FENString);
                System.out.println("Read FEN from string");
            } else {
                String sep = System.getProperty("file.separator");
                File inFile = new File(System.getProperty("user.dir") + sep + "games" + sep + "defaultStart.fen");
                String FENString = FENParser.readFENFromFile(inFile);
                chessboard = FENParser.getBoardFromFEN(FENString);

                System.out.println("Read FEN from file");
            }
        } catch (NullPointerException | IllegalFENException | FileNotFoundException e) {
            chessboard = FENParser.getBoardFromDefaultFEN();
            System.out.println("Could not read FEN, using default");
            System.out.println(e.getMessage());
        }

        drawBoard();
    }

    @FXML
    private void handleGridPaneClicked(MouseEvent event) {
        chessboard.positionPressed(new Position(event));
        drawBoard();
        showGameOver();
    }

    private void showGameOver() {
        if (hasShownGameOver)
            return;

        if (chessboard.getGameFinished()) {
            System.out.println(chessboard.getGameMessage());
            PopUp popUp = new PopUp("Game Over", true);
            popUp.addNode(new Text(chessboard.getGameMessage()));
            popUp.display();
            hasShownGameOver = true;
        }
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
    }

    @FXML
    private void toTitleScreen() {
        insertPane("TitleScreen.fxml", baseAnchor, new TitleController());
    }

    @FXML
    private void exportGame() {
        String position = chessboard.getFEN();
        PopUp popUp = new PopUp("Export game", true);

        TextField textField = new TextField(position);
        textField.setEditable(false);
        popUp.addNode(textField);

        Button toFileButton = new Button("Export to file");
        toFileButton.setOnAction(e -> {
            FENParser.saveToFile(position);
        });
        popUp.addNode(toFileButton);

        popUp.display();
    }
}
