package com.schedula.schedula.providers.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schedula.schedula.providers.models.entities.Providers;
import com.schedula.schedula.user.models.entities.User;

public interface ProvidersRepository extends JpaRepository<Providers, UUID> {
    List<Providers> findAllByUser(User user);
}
