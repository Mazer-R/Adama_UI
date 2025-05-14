package com.adama_ui;

import com.adama_ui.util.SolicitudStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.io.InputStream;
import java.util.List;

public class SolicitudHistoryController {

    @FXML
    private ListView<SolicitudRequest> listViewSolicitudes;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @FXML
    public void initialize() {
        try {
            InputStream input = getClass().getResourceAsStream("/solicitud.json");
            List<SolicitudRequest> solicitudes = objectMapper.readValue(input, new TypeReference<>() {});

            listViewSolicitudes.getItems().setAll(solicitudes);
            listViewSolicitudes.setCellFactory(list -> new ListCell<>() {
                @Override
                protected void updateItem(SolicitudRequest item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        HBox cell = new HBox(10);
                        cell.setStyle("-fx-background-color: #2e2e2e; -fx-padding: 10; -fx-border-color: gray;");

                        Label detail = new Label(item.getDetailOrder());
                        detail.setStyle("-fx-text-fill: white;");
                        detail.setWrapText(true);
                        detail.setMaxWidth(400);

                        Region spacer = new Region();
                        HBox.setHgrow(spacer, Priority.ALWAYS);

                        Label estado = new Label(item.getStatus().getLabel());
                        estado.setStyle("-fx-background-color: " + item.getStatus().getColorHex() + "; -fx-text-fill: black; -fx-padding: 5 10; -fx-background-radius: 10;");

                        cell.getChildren().addAll(detail, spacer, estado);
                        setGraphic(cell);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
