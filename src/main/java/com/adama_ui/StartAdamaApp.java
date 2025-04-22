package com.adama_ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class StartAdamaApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                StartAdamaApp.class.getResource("/com/adama_ui/LoginToApp.fxml")
        );
        Scene scene = new Scene(loader.load(), 800, 600);
        stage.setScene(scene);
        stage.setTitle("ADAMA UI");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}