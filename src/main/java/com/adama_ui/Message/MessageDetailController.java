package com.adama_ui.Message;

import com.adama_ui.Message.DTO.MessageResponse;
import com.adama_ui.util.Reloadable;
import com.adama_ui.style.AppTheme;
import com.adama_ui.util.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class MessageDetailController implements Reloadable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField fieldSender;
    @FXML
    private TextField fieldSubject;
    @FXML
    private TextArea fieldContent;

    private StackPane contentArea;
    ViewManager viewManager = ViewManager.getInstance();

    @FXML
    public void initialize() {
        AppTheme.applyThemeTo(rootPane);
        updateFields();
    }

    @Override
    public void onReload() {
        updateFields();
    }

    public void setContentArea(StackPane area) {
        this.contentArea = area;
    }


    public void setMessage(MessageResponse message) {
        viewManager.setCurrentMessage(message);
        updateFields();
    }

    private void updateFields() {
        MessageResponse msg = viewManager.getCurrentMessage();
        if (msg != null && fieldSender != null) {
            fieldSender.setText(msg.getSenderUsername());
            fieldSubject.setText(msg.getSubject());
            fieldContent.setText(msg.getContent());
            MessagesMainViewController.setShowingDetail(true);
        } else {
            System.err.println("❌ No hay mensaje disponible en updateFields()");
        }
    }


}