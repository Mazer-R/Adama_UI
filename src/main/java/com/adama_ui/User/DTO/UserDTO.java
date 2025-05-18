package com.adama_ui.User.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Data
public class UserDTO {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String role;
    private String department;


}