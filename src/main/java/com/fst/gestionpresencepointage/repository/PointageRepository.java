package com.fst.gestionpresencepointage.repository;

import com.fst.gestionpresencepointage.models.Pointage;
import com.fst.gestionpresencepointage.models.Seance;
import com.fst.gestionpresencepointage.models.Superviseur;
import com.fst.gestionpresencepointage.models.enums.StatutPointage;
import com.fst.gestionpresencepointage.models.Enseignant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PointageRepository extends JpaRepository<Pointage, Long> {
    List<Pointage> findByEnseignant(Enseignant enseignant);

    List<Pointage> findByEnseignantAndDate(Enseignant enseignant, LocalDate date);

    List<Pointage> findByStatut(StatutPointage statut);

    Optional<Pointage> findBySeance(Seance seance);

    boolean existsBySeance(Seance seance);

    List<Pointage> findBySuperviseur(Superviseur superviseur);
    // Dans PointageRepository.java — ajouter
// Récupère les pointages EN_ATTENTE pour un département précis
// Sert au dashboard Chef de Département
    @Query("SELECT p FROM Pointage p " +
            "JOIN p.seance s " +
            "JOIN s.moduleCours m " +
            "WHERE p.statut = :statut " +
            "AND m.departement.id = :departementId " +
            "ORDER BY p.date DESC")
    List<Pointage> findByStatutAndDepartement(
            @Param("statut") StatutPointage statut,
            @Param("departementId") Long departementId
    );
}
