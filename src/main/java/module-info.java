module com.adama_ui {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires java.prefs;

    opens com.adama_ui to javafx.fxml, com.fasterxml.jackson.databind;
    opens com.adama_ui.util to javafx.fxml;
    opens com.adama_ui.auth to javafx.fxml;

    exports com.adama_ui;
    exports com.adama_ui.util;
    exports com.adama_ui.auth;
}