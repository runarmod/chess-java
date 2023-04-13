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

/**
 * This class is the controller for the title scene. It handles the buttons for
 * playing a new game, loading a game from a FEN string, loading a game from a
 * file, and playing Fischer Random.
 * 
 * @see SceneSwitcher
 */
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

    /**
     * This method is called when the user clicks the "Play Chess" button. It will
     * load the FEN-string and create the board-scene.
     */
    @FXML
    private void playChess() {
        String FENString = loadGameField.getText();
        insertPane("App.fxml", baseAnchor, new ChessGameController(FENString));
    }

    /**
     * This method is called when the user clicks the "Play Fischer Random" button.
     * It creates the board-scene with a random piece-setup.
     */
    @FXML
    private void playFischerRandom() {
        String FENString = FENParser.generateFischerRandomFEN();
        insertPane("App.fxml", baseAnchor, new ChessGameController(FENString));
    }

    /**
     * This method is called when the user clicks the "Load Game from String"
     * button. It will show the inputfields to input the FEN-string.
     */
    @FXML
    private void loadGameFromString() {
        loadGameBoxWide.setVisible(true);
        inputFENAnchor.setVisible(true);
        loadGameButton.setVisible(false);
    }

    /**
     * This method is called when the user clicks the "Load Game from File" button.
     * It will open a file-explorer where the user can select the wanted file. After
     * it is selected, the chessboard with that position will load.
     */
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
