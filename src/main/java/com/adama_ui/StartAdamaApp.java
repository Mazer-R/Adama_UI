package com.adama_ui;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartAdamaApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // ✅ Usar ViewManager para cargar el login
            Parent root = ViewManager.loadViewForScene("/com/adama_ui/LoginToApp.fxml");

            Scene scene = new Scene(root);

            primaryStage.setTitle("Login - Adama");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);

            // ✅ Mostrar pantalla completa al abrir
            primaryStage.setMaximized(true);

            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Error al iniciar la aplicación:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
