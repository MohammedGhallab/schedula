package com.schedula.schedula.user.models.dto;

import lombok.Data;

@Data
public class LoginResponse {

    private String token;
    private String type = "Bearer";
    private String userName;
    private String name;
    private String email;
    private String role;
    private Boolean active;

}
