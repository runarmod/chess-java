package sjakk.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import sjakk.utils.FENParser;
import sjakk.utils.PopUp;

public class TitleController extends SceneSwitcher {

    @FXML
    private AnchorPane inputFENAnchor;

    @FXML
    private Button loadGameButton;

    @FXML
    private TextField loadGameField;

    @FXML
    private Rectangle loadGameBoxWide;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inputFENAnchor.setVisible(false);
        loadGameBoxWide.setVisible(false);
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
        loadGameBoxWide.setVisible(true);
        inputFENAnchor.setVisible(true);
        loadGameButton.setVisible(false);
    }

    @FXML
    private void loadGameFromFile() {
        File file = FENParser.getFileFromChooser();
        if (file == null) {
            // Did not select a file
            return;
        }
        try {
            String FENString = FENParser.readFENFromFile(file);
            insertPane("App.fxml", baseAnchor, new ChessGameController(FENString));
        } catch (FileNotFoundException e) {
            PopUp popup = new PopUp("Did not find file", true);
            popup.addNode(new Text("Did not find file"));
            popup.display();
        }
    }
}
