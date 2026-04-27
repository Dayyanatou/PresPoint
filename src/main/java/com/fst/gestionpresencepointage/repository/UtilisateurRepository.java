package com.fst.gestionpresencepointage.repository;


import com.fst.gestionpresencepointage.models.enums.RoleUtilisateur;
import com.fst.gestionpresencepointage.models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    //Rechercher un utilisateur par email
    Optional<Utilisateur> findByEmail(String email);
    Optional<Utilisateur> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);


    List<Utilisateur> findByRole(RoleUtilisateur role);

    List<Utilisateur> findByStatut(Boolean Statut);

    List<Utilisateur> findByNomContainingIgnoreCase(String motCle);

    List<Utilisateur> findByPrenomContainingIgnoreCase(String motCle);

}
