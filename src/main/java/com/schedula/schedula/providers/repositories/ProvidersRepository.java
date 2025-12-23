package com.schedula.schedula.providers.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schedula.schedula.providers.models.entities.Providers;

public interface ProvidersRepository extends JpaRepository<Providers, UUID> {

}
