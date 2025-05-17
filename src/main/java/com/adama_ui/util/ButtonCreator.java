package com.adama_ui.util;

import javafx.scene.control.ContentDisplay;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.Button;

import java.io.InputStream;
public class ButtonCreator {
    public void configureIconButton(Button button, String iconPath, int size) {
        try {
            InputStream stream = getClass().getResourceAsStream(iconPath);
            if (stream == null) {
                throw new IllegalArgumentException("Recurso no encontrado: " + iconPath);
            }

            Image icon = new Image(stream, size*2, size*2, true, true);
            ImageView iconView = new ImageView(icon);
            iconView.setFitWidth(size*1.5);
            iconView.setFitHeight(size*1.5);
            iconView.setPreserveRatio(true);
            button.setGraphic(iconView);
            button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        } catch (Exception e) {
            System.err.println("Error configurando botón: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
