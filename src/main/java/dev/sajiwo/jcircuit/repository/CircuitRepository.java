package dev.sajiwo.jcircuit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.sajiwo.jcircuit.entity.Circuit;

@Repository
public interface CircuitRepository extends JpaRepository<Circuit, String> {

  List<Circuit> findByTargetPath(String circuitKey);

  Optional<Circuit> findByTargetPathAndTargetDomain(String targetPath, String targetDomain);
}
