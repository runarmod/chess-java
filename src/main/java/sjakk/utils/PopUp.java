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

public class PopUp {
    private Collection<Node> nodes = new ArrayList<>();
    private boolean extraCloseButton;
    private boolean closeable = true;
    private String windowTitle;

    public PopUp(String windowTitle, boolean extraCloseButton) {
        this.extraCloseButton = extraCloseButton;
        this.windowTitle = windowTitle;
    }

    public PopUp(String windowTitle, boolean extraCloseButton, boolean closeable) {
        this(windowTitle, extraCloseButton);
        this.closeable = closeable;
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void display() {
        Stage popupwindow = new Stage();
        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle(windowTitle);

        if (!closeable) {
            popupwindow.setOnCloseRequest(e -> e.consume());
        }

        if (extraCloseButton) {
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
