package sjakk.controllers;

import java.net.URL;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

public class SceneSwitcher {
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
            // AnchorPane.setBottomAnchor(pane, 0d);
            // AnchorPane.setTopAnchor(pane, 0d);
            // AnchorPane.setLeftAnchor(pane, 0d);
            // AnchorPane.setRightAnchor(pane, 0d);
        } catch (Exception e) {
            System.out.println("Error loading FXML file: " + fxmlFileName);
            System.exit(1);
            return;
        }
    }
}
