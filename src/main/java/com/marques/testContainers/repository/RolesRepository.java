package com.marques.testContainers.repository;

import com.marques.testContainers.domain.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles, Long> {

    Optional<Roles> findByName(String name);

    boolean existsByName(String name);
}
