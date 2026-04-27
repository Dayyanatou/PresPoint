package com.fst.gestionpresencepointage.repository;

import com.fst.gestionpresencepointage.models.Departement;
import com.fst.gestionpresencepointage.models.Etudiant;
import com.fst.gestionpresencepointage.models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    Optional<Etudiant> findByMatricule(String matricule);

    boolean existsByMatricule(String matricule);

    //Optional<Etudiant> findByUtilisateur(Utilisateur utilisateur);

    List<Etudiant> findByDepartement(Departement departement);


    List<Etudiant> findByNiveau(String niveau);

    List<Etudiant> findByMatriculeContainingIgnoreCase(String motCle);
}
