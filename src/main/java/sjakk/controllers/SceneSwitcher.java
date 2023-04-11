package sjakk.controllers;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

public abstract class SceneSwitcher implements Initializable {
    @FXML
    protected AnchorPane baseAnchor;

    protected void insertPane(String fxmlFileName, AnchorPane parent, SceneSwitcher controller) {
        try {
            parent.getChildren().clear();
            URL fxmlUrl = getClass().getResource("/sjakk/" + fxmlFileName);

            FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
            fxmlLoader.setController(controller);

            Parent pane = fxmlLoader.load();
            parent.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading FXML file: " + fxmlFileName);
            System.exit(1);
            return;
        }
    }
}
