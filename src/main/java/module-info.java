module com.adama_ui {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires java.prefs;

    opens com.adama_ui to javafx.fxml, com.fasterxml.jackson.databind;
    opens com.adama_ui.auth to javafx.fxml;

    exports com.adama_ui;
    exports com.adama_ui.util;
    exports com.adama_ui.auth;
    opens com.adama_ui.util to com.fasterxml.jackson.databind, javafx.fxml;
    exports com.adama_ui.Product;
    opens com.adama_ui.Product to com.fasterxml.jackson.databind, javafx.fxml;
    exports com.adama_ui.Order;
    opens com.adama_ui.Order to com.fasterxml.jackson.databind, javafx.fxml;
    exports com.adama_ui.User;
    opens com.adama_ui.User to com.fasterxml.jackson.databind, javafx.fxml;
    exports com.adama_ui.Message;
    opens com.adama_ui.Message to com.fasterxml.jackson.databind, javafx.fxml;
}