package com.adama_ui.util;

import javafx.scene.control.ContentDisplay;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.Button;

import java.io.InputStream;

public class ButtonCreator {

    private void buttonConfig(Button button, String iconPath, Boolean isLong) {
        try {
            InputStream stream = getClass().getResourceAsStream(iconPath);
            if (stream == null) {
                throw new IllegalArgumentException("Recurso no encontrado: " + iconPath);
            }
            int size = 40;
            Image icon = new Image(stream, size * 2, size * 2, true, true);
            ImageView iconView = new ImageView(icon);
            iconView.setFitWidth(size * 1.5);
            iconView.setFitHeight(size * 1.5);
            iconView.setPreserveRatio(true);
            button.setGraphic(iconView);
            if (isLong) {
                button.setMinSize(70, 30);
            } else {
                button.setMinSize(70, 70);
            }
            button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        } catch (Exception e) {
            System.err.println("Error configurando botón: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void configureIconButton(Button button, String iconPath) {
        buttonConfig(button, iconPath, false);
    }

    public void configureLongButton(Button button, String iconPath) {
        buttonConfig(button, iconPath, true);

    }
}
