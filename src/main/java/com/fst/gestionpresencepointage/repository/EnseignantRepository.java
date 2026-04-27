package com.fst.gestionpresencepointage.repository;


import com.fst.gestionpresencepointage.models.Departement;
import com.fst.gestionpresencepointage.models.Enseignant;
import com.fst.gestionpresencepointage.models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {

    List<Enseignant> findByDepartement(Departement departement);

    List<Enseignant> findByGradeContainingIgnoreCase(String grade);

    List<Enseignant> findByStatutEnseignantContainingIgnoreCase(String statut);
// ✅ nom correct du champ dans Enseignant.java
}
