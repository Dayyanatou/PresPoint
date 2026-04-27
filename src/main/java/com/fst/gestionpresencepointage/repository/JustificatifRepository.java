package com.fst.gestionpresencepointage.repository;

import com.fst.gestionpresencepointage.models.Etudiant;
import com.fst.gestionpresencepointage.models.Justificatif;
import com.fst.gestionpresencepointage.models.enums.StatutJustificatif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface JustificatifRepository extends JpaRepository<Justificatif, Long> {

    // Tous les justificatifs d'un étudiant
    List<Justificatif> findByEtudiant(Etudiant etudiant);

    // Justificatifs d'un étudiant par statut
    List<Justificatif> findByEtudiantAndStatut(
            Etudiant etudiant, StatutJustificatif statut);

    // Tous les justificatifs EN_ATTENTE d'un département
    // Pour le chef de département
    @Query("SELECT j FROM Justificatif j " +
            "JOIN j.etudiant e " +
            "WHERE e.departement.id = :departementId " +
            "AND j.statut = :statut " +
            "ORDER BY j.dateSoumission DESC")
    List<Justificatif> findByDepartementAndStatut(
            @Param("departementId") Long departementId,
            @Param("statut") StatutJustificatif statut
    );
}