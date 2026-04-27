package com.fst.gestionpresencepointage.repository;

import com.fst.gestionpresencepointage.models.Salle;
import com.fst.gestionpresencepointage.models.enums.TypeSalle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SalleRepository extends JpaRepository<Salle, Long> {
    List<Salle> findByType(TypeSalle type);
    List<Salle> findByCapaciteGreaterThanEqual(Integer capacite);
    Optional<Salle> findByNumero(String numero);
}

