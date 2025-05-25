package com.adama_ui.style.renderer;

import com.adama_ui.User.DTO.UserResponse;
import com.adama_ui.util.ViewManager;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class UserCellRenderer {

    private static final ViewManager viewManager = ViewManager.getInstance();

    public static Node render(UserResponse user) {
        HBox cell = new HBox(10);
        cell.getStyleClass().add("custom-list-cell");  // Aplica estilo de fondo, borde, padding, hover

        Label name = new Label(user.getFirstName() + " " + user.getLastName());
        name.getStyleClass().add("product-name");  // Texto en negrita y color adecuado
        name.setPrefWidth(200);

        Label username = new Label("@" + user.getUsername());
        username.getStyleClass().add("product-info"); // Color de texto según tema
        username.setPrefWidth(150);

        Label role = new Label(user.getRole());
        role.getStyleClass().add("product-info");
        role.setPrefWidth(120);

        Label department = new Label(user.getDepartment() != null ? user.getDepartment() : "Sin departamento");
        department.getStyleClass().add("product-info");
        department.setPrefWidth(150);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button viewButton = new Button("Ver detalles");
        viewButton.getStyleClass().addAll("button", "action-button"); // Usa tus estilos de botón (verde)
        viewButton.setOnAction(e -> viewManager.loadWithUser("/com/adama_ui/User/UserDetails.fxml", user));

        cell.getChildren().addAll(name, username, role, department, spacer, viewButton);
        return cell;
    }
}