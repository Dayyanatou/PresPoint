package com.fst.gestionpresencepointage.service;

import com.fst.gestionpresencepointage.dto.PresenceDto;
import com.fst.gestionpresencepointage.dto.TauxPresenceDto;
import com.fst.gestionpresencepointage.models.enums.StatutPresence;

import java.util.List;
import java.util.Map;

public interface PresenceService {

    // ── Méthodes existantes ──────────────────────────────────────────────
    PresenceDto enregistrer(PresenceDto dto);
    PresenceDto getById(Long id);
    List<PresenceDto> listerParSeance(Long seanceId);
    List<PresenceDto> listerParEtudiant(Long etudiantId);
    List<PresenceDto> listerParSeanceEtStatut(Long seanceId, StatutPresence statut);
    PresenceDto modifierStatut(Long id, StatutPresence nouveauStatut);
    void supprimer(Long id);

    // ── Nouvelles méthodes pour les dashboards ───────────────────────────

    // Retourne le taux global de présence d'un étudiant
    // Exemple : 81.5  (= 81.5%)
    double calculerTauxGlobal(Long etudiantId);

    // Retourne le taux de présence PAR MODULE pour un étudiant
    // Exemple : { "Algorithmique" -> 92.0, "Réseaux" -> 70.0, ... }
    Map<String, Double> calculerTauxParModule(Long etudiantId);

    // Retourne le nombre d'absences non justifiées
    long compterAbsencesNonJustifiees(Long etudiantId);

    // Retourne vrai si l'étudiant est sous le seuil (ex: 75%)
    boolean estSousSeuil(Long etudiantId, double seuilPourcentage);

    // Retourne les présences avec statut ABSENT pour un étudiant
    // Sert à afficher la liste des absences dans le dashboard
    List<PresenceDto> listerAbsences(Long etudiantId);

    // Retourne tous les étudiants sous le seuil dans un département
    // Sert au rapport du Chef de Département
    List<TauxPresenceDto> getEtudiantsSousSeuil(Long departementId, double seuil);
    List<PresenceDto> getAll();
}

/*
package com.fst.gestionpresencepointage.service;

import com.fst.gestionpresencepointage.dto.PresenceDto;
import com.fst.gestionpresencepointage.models.enums.StatutPresence;
import java.util.List;

public interface PresenceService {

    PresenceDto enregistrer(PresenceDto dto);
    PresenceDto getById(Long id);
    List<PresenceDto> listerParSeance(Long seanceId);
    List<PresenceDto> listerParEtudiant(Long etudiantId);
    List<PresenceDto> listerParSeanceEtStatut(Long seanceId, StatutPresence statut);
    PresenceDto modifierStatut(Long id, StatutPresence nouveauStatut);
    void supprimer(Long id);
}*/
