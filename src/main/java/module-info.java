module com.adama_ui {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.net.http;

    opens com.adama_ui to javafx.fxml;
    exports com.adama_ui;
}
