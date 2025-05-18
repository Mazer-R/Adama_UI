package com.adama_ui.Message.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageResponse {
    private String id;
    private String senderUsername;
    private String receiverUsername;
    private String subject;
    private String content;
    private boolean isRead;
}
