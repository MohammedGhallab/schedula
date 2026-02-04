package com.schedula.schedula.user.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.schedula.schedula.user.models.entities.User;
import com.schedula.schedula.user.repositories.Projection.UserLoginProjection;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<UserLoginProjection> findByEmail(String email);

    Optional<User> findByEmailAndActive(String email, boolean active);
}
