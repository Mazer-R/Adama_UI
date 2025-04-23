module com.example.adama_ui {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;

    opens com.example.adama_ui to javafx.fxml;
    exports com.example.adama_ui;
}
