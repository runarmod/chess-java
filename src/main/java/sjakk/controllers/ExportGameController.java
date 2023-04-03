package sjakk.controllers;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sjakk.utils.FENParser;

public class ExportGameController {
    private String inputValue;

    public ExportGameController(String inputValue) {
        this.inputValue = inputValue;
    }

    public void display() {
        Stage popupwindow = new Stage();
        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("Export game");

        TextField textField = new TextField(inputValue);
        textField.setEditable(false);

        Button toFileButton = new Button("Export to file");
        toFileButton.setOnAction(e -> {
            FENParser.saveToFile(inputValue);
        });

        Button close = new Button("Close");
        close.setOnAction(e -> popupwindow.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(textField, toFileButton, close);
        layout.setAlignment(Pos.CENTER);

        Scene scene1 = new Scene(layout, 400, 250);
        popupwindow.setScene(scene1);
        popupwindow.showAndWait();
    }
}
