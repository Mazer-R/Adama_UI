module com.adama_ui {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;

    opens com.adama_ui to javafx.fxml;
    exports com.adama_ui;
    exports com.adama_ui.util;
    opens com.adama_ui.util to javafx.fxml;
}
