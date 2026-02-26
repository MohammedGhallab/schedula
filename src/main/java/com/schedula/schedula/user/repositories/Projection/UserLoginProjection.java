package com.schedula.schedula.user.repositories.Projection;

import java.util.UUID;

public interface UserLoginProjection {
    UUID getId();

    String getPassword();

    String getName();

    String getEmail();

    String getRole();

    Boolean getActive();
}
