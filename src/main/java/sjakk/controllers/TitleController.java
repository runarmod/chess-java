package sjakk.controllers;

import javafx.fxml.FXML;

public class TitleController extends SceneSwitcher {

    @FXML
    private void playChess() {
        insertPane("App.fxml", baseAnchor, new ChessGameController());
    }

}
