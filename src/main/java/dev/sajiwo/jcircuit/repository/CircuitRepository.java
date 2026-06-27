package dev.sajiwo.jcircuit.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.sajiwo.jcircuit.entity.Circuit;

@Repository
public interface CircuitRepository extends JpaRepository<Circuit, UUID> {
}
