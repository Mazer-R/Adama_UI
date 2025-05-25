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
        cell.setStyle("-fx-background-color: #1e1e1e; -fx-padding: 10; -fx-border-color: gray; -fx-border-radius: 5;");

        Label name = new Label(user.getFirstName() + " " + user.getLastName());
        name.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        name.setPrefWidth(200);

        Label username = new Label("@" + user.getUsername());
        username.setStyle("-fx-text-fill: white;");
        username.setPrefWidth(150);

        Label role = new Label(user.getRole());
        role.setStyle("-fx-text-fill: white;");
        role.setPrefWidth(120);

        Label department = new Label(user.getDepartment() != null ? user.getDepartment() : "Sin departamento");
        department.setStyle("-fx-text-fill: white;");
        department.setPrefWidth(150);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button viewButton = new Button("Ver detalles");
        viewButton.setStyle("-fx-background-color: #00c000; -fx-text-fill: white;");
        viewButton.setOnAction(e -> viewManager.loadWithUser("/com/adama_ui/User/UserDetails.fxml", user));

        cell.getChildren().addAll(name, username, role, department, spacer, viewButton);
        return cell;
    }
}