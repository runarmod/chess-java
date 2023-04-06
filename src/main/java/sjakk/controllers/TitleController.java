package sjakk.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import sjakk.utils.FENParser;

public class TitleController extends SceneSwitcher {

    @FXML
    private AnchorPane inputFENAnchor;

    @FXML
    private Button loadGameButton;

    @FXML
    private TextField loadGameField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inputFENAnchor.setVisible(false);
    }

    @FXML
    private void playChess() {
        String FENString = loadGameField.getText();
        insertPane("App.fxml", baseAnchor, new ChessGameController(FENString));
    }

    @FXML
    private void playFischerRandom() {
        String FENString = FENParser.generateFischerRandomFEN();
        insertPane("App.fxml", baseAnchor, new ChessGameController(FENString));
    }

    @FXML
    private void loadGameFromString() {
        inputFENAnchor.setVisible(true);
        loadGameButton.setVisible(false);
    }

    @FXML
    private void loadGameFromFile() {
        File file = FENParser.getFileFromChooser();
        try {
            String FENString = FENParser.readFENFromFile(file);
            insertPane("App.fxml", baseAnchor, new ChessGameController(FENString));
        } catch (FileNotFoundException e) {
            // TODO: Pop up with info about file not found
        }
    }
}
