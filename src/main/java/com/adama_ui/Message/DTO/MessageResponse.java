package com.adama_ui.Message.DTO;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageResponse {
    private String id;
    private String senderUsername;
    private String subject;
    private String content;
    private boolean isRead;

}