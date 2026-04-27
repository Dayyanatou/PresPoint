package com.fst.gestionpresencepointage.repository;

import com.fst.gestionpresencepointage.models.ChefDepartement;
import com.fst.gestionpresencepointage.models.Departement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ChefDepartementRepository extends JpaRepository<ChefDepartement, Long> {

    // Trouver le chef d'un département
    Optional<ChefDepartement> findByDepartement(Departement departement);

    // Vérifier si un département a déjà un chef
    boolean existsByDepartement(Departement departement);
}