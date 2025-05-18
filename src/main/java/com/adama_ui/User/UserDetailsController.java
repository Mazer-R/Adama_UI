package com.adama_ui.User;

import com.adama_ui.User.DTO.UserResponse;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class UserDetailsController {
    @FXML private Label lblUsername;
    @FXML private Label lblFirstName;
    @FXML private Label lblRole;

    public void showUserDetails(UserResponse user) {
        lblUsername.setText(user.getUsername());
        lblFirstName.setText(user.getFirstName());
        lblRole.setText(user.getRole());
    }
}
