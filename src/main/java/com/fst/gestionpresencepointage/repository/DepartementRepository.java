package com.fst.gestionpresencepointage.repository;

import com.fst.gestionpresencepointage.models.Departement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartementRepository extends JpaRepository<Departement, Long> {
    Optional<Departement> findByNomIgnoreCase(String nom);

    boolean existsByNomIgnoreCase(String nom);

    List<Departement> findByNomContainingIgnoreCase(String motCle);
}
