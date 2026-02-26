package com.schedula.schedula.providers.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.schedula.schedula.providers.models.entities.Providers;
import com.schedula.schedula.user.models.entities.User;

public interface ProvidersRepository extends JpaRepository<Providers, UUID> {
    // @EntityGraph(attributePaths = {"appointments", "appointments.payment"})
    List<Providers> findAllByUser(User user);

    @EntityGraph(attributePaths = { "services" })
    Page<Providers> findAll(Pageable page);
}
