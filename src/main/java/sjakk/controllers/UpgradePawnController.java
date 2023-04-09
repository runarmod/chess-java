package sjakk.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sjakk.pieces.Bishop;
import sjakk.pieces.Knight;
import sjakk.pieces.Piece;
import sjakk.pieces.Queen;
import sjakk.pieces.Rook;

public class UpgradePawnController implements Initializable {
    @FXML
    private ImageView queen, rook, bishop, knight;

    private static String upgradeChoice;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        queen.setOnMouseClicked(e -> choose("queen"));
        rook.setOnMouseClicked(e -> choose("rook"));
        bishop.setOnMouseClicked(e -> choose("bishop"));
        knight.setOnMouseClicked(e -> choose("knight"));
    }

    private void choose(String pieceType) {
        // Upgrade the pawn
        System.out.println("Upgrading pawn to " + pieceType);
        upgradeChoice = pieceType;

        // Close the window
        System.out.println("Closing window");
        Stage stage = (Stage) queen.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    public static String getUpgradeChoice() {
        return upgradeChoice;
    }
}
