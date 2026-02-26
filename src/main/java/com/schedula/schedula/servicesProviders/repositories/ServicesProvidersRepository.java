package com.schedula.schedula.servicesProviders.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.schedula.schedula.providers.models.entities.Providers;

import com.schedula.schedula.servicesProviders.models.entities.ServicesProviders;

@Repository
public interface ServicesProvidersRepository extends JpaRepository<ServicesProviders, UUID> {
    @EntityGraph(attributePaths = { "providers" })
    List<ServicesProviders> findByProviders(Providers providers);
}
