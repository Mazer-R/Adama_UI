package com.adama_ui;

import com.adama_ui.style.AppTheme;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class HomeViewController {

    @FXML private VBox rootPane;

    @FXML
    public void initialize() {
        AppTheme.applyThemeTo(rootPane);
    }
}
