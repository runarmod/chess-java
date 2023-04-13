package sjakk;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sjakk.controllers.TitleController;

import java.io.IOException;

public class App extends Application {
    private Scene titleScene;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Super chess");

        FXMLLoader titleLoader = new FXMLLoader(getClass().getResource("TitleScreen.fxml"));
        titleLoader.setController(new TitleController());
        titleScene = new Scene(titleLoader.load());
        titleScene.setFill(Color.TRANSPARENT);

        primaryStage.setScene(titleScene);
        primaryStage.setResizable(false);

        primaryStage.show();
    }

}
