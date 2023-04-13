package sjakk.utils;

import java.util.ArrayList;
import java.util.Collection;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * A class for creating pop-up windows. It is possible to add any number of
 * nodes to the window, and it is possible to decide whether or not it should be
 * closeable.
 */
public class PopUp {
    private Collection<Node> nodes = new ArrayList<>();
    private boolean closeable = true;
    private String windowTitle;

    /**
     * Creates a new PopUp window. It will have the given title, and can be chosen
     * to be closable or not. If it is closable, it is will have an extra
     * closebutton at the bottom of the popup.
     * 
     * @param windowTitle The title of the window
     * @param closable    Whether or not to add a close button
     */
    public PopUp(String windowTitle, boolean closeable) {
        this.closeable = closeable;
        this.windowTitle = windowTitle;
    }

    /**
     * Adds a node to the window.
     * 
     * @param node The node to add
     */
    public void addNode(Node node) {
        nodes.add(node);
    }

    /**
     * Displays the window.
     */
    public void display() {
        Stage popupwindow = new Stage();
        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle(windowTitle);

        if (!closeable) {
            popupwindow.setOnCloseRequest(e -> e.consume());
        } else {
            Button close = new Button("Close");
            close.setOnAction(e -> popupwindow.close());
            nodes.add(close);
        }

        VBox layout = new VBox(10);
        layout.getChildren().addAll(nodes);
        layout.setAlignment(Pos.CENTER);

        Scene scene1 = new Scene(layout, 400, 250);
        popupwindow.setScene(scene1);
        popupwindow.showAndWait();
    }
}
