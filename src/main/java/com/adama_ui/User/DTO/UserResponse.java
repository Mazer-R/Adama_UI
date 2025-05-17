package com.adama_ui.User.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

    private String username;
    private String firstName;
    private String lastName;
    private String role;
    private String department;

  }