package sjakk.controllers;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

/**
 * This class is the base class for all controllers which can switch to other
 * scenes. The method {@link #insertPane(String, AnchorPane, SceneSwitcher)} is
 * used to switch to the new scene.
 */
public abstract class SceneSwitcher implements Initializable {
    /**
     * The base anchor pane. This is the parent of all other panes.
     */
    @FXML
    protected AnchorPane baseAnchor;

    /**
     * Inserts the pane with the given FXML file name into the given parent pane,
     * and removes the previous anchorpane (screen).
     * 
     * @param fxmlFileName the name of the FXML file (e.g. "App.fxml")
     * @param parent       the parent pane
     * @param controller   the controller for the FXML file
     */
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
