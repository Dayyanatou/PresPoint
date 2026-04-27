package com.fst.gestionpresencepointage.repository;

import com.fst.gestionpresencepointage.models.Presence;
import com.fst.gestionpresencepointage.models.Seance;
import com.fst.gestionpresencepointage.models.enums.StatutPresence;
import com.fst.gestionpresencepointage.models.Etudiant;
import com.fst.gestionpresencepointage.models.ModuleCours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PresenceRepository extends JpaRepository<Presence, Long> {

    // ── Requêtes existantes ──────────────────────────────────────────────

    List<Presence> findByEtudiant(Etudiant etudiant);
    List<Presence> findBySeance(Seance seance);
    List<Presence> findByEtudiantAndStatut(Etudiant etudiant, StatutPresence statut);
    Optional<Presence> findByEtudiantAndSeance(Etudiant etudiant, Seance seance);
    boolean existsByEtudiantAndSeance(Etudiant etudiant, Seance seance);
    List<Presence> findBySeanceAndStatut(Seance seance, StatutPresence statut);

    // ── Nouvelles requêtes pour les dashboards ───────────────────────────

    // Compte combien de présences un étudiant a avec un statut donné
    // Exemple : countByEtudiantAndStatut(etudiant, PRESENT) → 42
    long countByEtudiantAndStatut(Etudiant etudiant, StatutPresence statut);

    // Compte toutes les présences d'un étudiant (peu importe le statut)
    // Sert à calculer le taux : 42 présents / 52 total = 81%
    long countByEtudiant(Etudiant etudiant);

    // Récupère toutes les présences d'un étudiant pour un module précis
    // @Query = requête SQL personnalisée (JOIN sur seance → module)
    // p = presence, s = seance, m = module
    @Query("SELECT p FROM Presence p " +
            "JOIN p.seance s " +
            "WHERE p.etudiant = :etudiant " +
            "AND s.moduleCours = :module")
    List<Presence> findByEtudiantAndModule(
            @Param("etudiant") Etudiant etudiant,
            @Param("module") ModuleCours module
    );

    // Compte les présences d'un étudiant dans un module avec un statut donné
    // Sert à calculer le taux PAR MODULE
    @Query("SELECT COUNT(p) FROM Presence p " +
            "JOIN p.seance s " +
            "WHERE p.etudiant = :etudiant " +
            "AND s.moduleCours = :module " +
            "AND p.statut = :statut")
    long countByEtudiantAndModuleAndStatut(
            @Param("etudiant") Etudiant etudiant,
            @Param("module") ModuleCours module,
            @Param("statut") StatutPresence statut
    );

    // Récupère les présences d'un département entier
    // Sert aux statistiques globales du Chef de Département
    @Query("SELECT p FROM Presence p " +
            "JOIN p.seance s " +
            "JOIN s.moduleCours m " +
            "WHERE m.departement.id = :departementId")
    List<Presence> findByDepartementId(@Param("departementId") Long departementId);
}



/*
package com.fst.gestionpresencepointage.repository;

import com.fst.gestionpresencepointage.models.Presence;
import com.fst.gestionpresencepointage.models.Seance;
import com.fst.gestionpresencepointage.models.enums.StatutPresence;
import com.fst.gestionpresencepointage.models.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PresenceRepository extends JpaRepository<Presence, Long> {

    List<Presence> findByEtudiant(Etudiant etudiant);

    List<Presence> findBySeance(Seance seance);

    List<Presence> findByEtudiantAndStatut(Etudiant etudiant, StatutPresence statut);

    Optional<Presence> findByEtudiantAndSeance(Etudiant etudiant, Seance seance);

    boolean existsByEtudiantAndSeance(Etudiant etudiant, Seance seance);

    List<Presence> findBySeanceAndStatut(Seance seance, StatutPresence statut);
}
*/
