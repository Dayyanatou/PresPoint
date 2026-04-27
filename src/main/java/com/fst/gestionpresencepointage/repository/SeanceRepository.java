package com.fst.gestionpresencepointage.repository;

import com.fst.gestionpresencepointage.models.ModuleCours;
import com.fst.gestionpresencepointage.models.Seance;
import com.fst.gestionpresencepointage.models.Enseignant;
import com.fst.gestionpresencepointage.models.enums.TypeSeance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SeanceRepository extends JpaRepository<Seance, Long> {
    List<Seance> findByModuleCours(ModuleCours moduleCours);

    List<Seance> findByEnseignant(Enseignant enseignant);

    List<Seance> findByDateSeance(LocalDate dateSeance);

    List<Seance> findByDateSeanceBetween(LocalDate debut, LocalDate fin);

    List<Seance> findByTypeSeance(TypeSeance typeSeance);

    List<Seance> findByEnseignantAndDateSeance(Enseignant enseignant, LocalDate dateSeance);

    // Dans SeanceRepository.java — ajouter cette méthode
// Elle récupère les séances d'aujourd'hui pour un département entier
    // Séances d'un jour précis
    @Query("SELECT s FROM Seance s " +
            "JOIN FETCH s.moduleCours m " +
            "JOIN FETCH s.enseignant e " +  // On charge l'enseignant aussi !
            "JOIN FETCH s.salle sl " +      // Et la salle
            "WHERE s.dateSeance = :date " +
            "AND m.departement.id = :departementId " +
            "ORDER BY s.heureDebut ASC")
    List<Seance> findByDateAndDepartement(
            @Param("date") LocalDate date,
            @Param("departementId") Long departementId
    );

    // Séances de la semaine
    @Query("SELECT s FROM Seance s " +
            "JOIN FETCH s.moduleCours m " +
            "JOIN FETCH s.enseignant e " +
            "JOIN FETCH s.salle sl " +
            "WHERE s.dateSeance BETWEEN :debut AND :fin " +
            "AND m.departement.id = :departementId " +
            "ORDER BY s.dateSeance ASC, s.heureDebut ASC")
    List<Seance> findByPeriodeAndDepartement(
            @Param("debut") LocalDate debut,
            @Param("fin") LocalDate fin,
            @Param("departementId") Long departementId
    );
}
