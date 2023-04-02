package sjakk.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

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
    private void loadGame() {
        inputFENAnchor.setVisible(true);
        loadGameButton.setVisible(false);
    }
}
