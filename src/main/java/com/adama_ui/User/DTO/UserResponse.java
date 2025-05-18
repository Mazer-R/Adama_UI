package com.adama_ui.User.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String role;
    private String managerUsername;
    private String department;
    @JsonIgnore
    private String created;
    @JsonIgnore
    private String lastModified;
    @JsonIgnore
    private String modifiedBy;

}