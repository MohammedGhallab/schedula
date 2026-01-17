package com.schedula.schedula.user.repositories.Projection;

public interface UserLoginProjection {
    String getPassword();
    String getName();
    String getEmail();
    String getRole();
    Boolean getActive();
}
