package com.adama_ui.Message;

import com.adama_ui.Message.DTO.MessageResponse;
import com.adama_ui.Reloadable;
import com.adama_ui.style.AppTheme;
import com.adama_ui.util.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class MessageDetailController implements Reloadable {

    @FXML private AnchorPane rootPane;
    @FXML private TextField fieldSender;
    @FXML private TextField fieldSubject;
    @FXML private TextArea fieldContent;

    private StackPane contentArea;

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

    // 🟢 Nuevo método clave para asegurar la carga explícita
    public void setMessage(MessageResponse message) {
        ViewManager.setCurrentMessage(message);
        updateFields();
    }

    private void updateFields() {
        MessageResponse msg = ViewManager.getCurrentMessage();
        if (msg != null && fieldSender != null) {
            fieldSender.setText(msg.getSenderUsername());
            fieldSubject.setText(msg.getSubject());
            fieldContent.setText(msg.getContent());
            MessagesMainViewController.setShowingDetail(true);
        } else {
            System.err.println("❌ No hay mensaje disponible en updateFields()");
        }
    }

    @FXML
    private void onBack() {
        MessagesMainViewController.setShowingDetail(false);
        ViewManager.loadInto("/com/adama_ui/Message/InboxMessagesView.fxml", contentArea, () -> {
            var ctrl = ViewManager.getCurrentControllerAs(InboxMessagesViewController.class);
            if (ctrl != null) ctrl.setContentArea(contentArea);
        });
    }
}