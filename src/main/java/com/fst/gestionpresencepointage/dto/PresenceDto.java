package com.fst.gestionpresencepointage.dto;

import com.fst.gestionpresencepointage.models.Presence;
import com.fst.gestionpresencepointage.models.enums.StatutPresence;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PresenceDto {
    private Long id;
    private StatutPresence statut;
    private String remarque;

    // Infos étudiant — déjà là ✅
    private Long etudiantId;
    private String etudiantMatricule;
    private String etudiantNom;

    // Infos séance — déjà là ✅
    private Long seanceId;
    private String seanceResume;

    // ── Champs à ajouter ──────────────────────
    // Date de la séance (pour afficher dans le tableau)
    private java.time.LocalDate datePresence;

    // Nom du module de la séance
    private String seanceModuleNom;

    // Heure début et fin de la séance
    private java.time.LocalTime seanceHeureDebut;
    private java.time.LocalTime seanceHeureFin;

    // Nom de la salle
    private String seanceSalleNom;

    // Nom de l'enseignant
    private String seanceEnseignantNom;
    // ──────────────────────────────────────────

    public static PresenceDto fromEntity(Presence presence) {
        return PresenceDto.builder()
                .id(presence.getId())
                .statut(presence.getStatut())
                .remarque(presence.getRemarque())

                .etudiantId(presence.getEtudiant() != null
                        ? presence.getEtudiant().getId() : null)
                .etudiantMatricule(presence.getEtudiant() != null
                        ? presence.getEtudiant().getMatricule() : null)
                .etudiantNom(presence.getEtudiant() != null
                        ? presence.getEtudiant().getNom() : null)

                .seanceId(presence.getSeance() != null
                        ? presence.getSeance().getId() : null)

                // ── Nouveaux champs ──────────────────────
                // On récupère la date depuis la séance
                .datePresence(presence.getSeance() != null
                        ? presence.getSeance().getDateSeance() : null)

                // Nom du module via séance → moduleCours
                .seanceModuleNom(presence.getSeance() != null
                        && presence.getSeance().getModuleCours() != null
                        ? presence.getSeance().getModuleCours().getIntitule() : null)

                .seanceHeureDebut(presence.getSeance() != null
                        ? presence.getSeance().getHeureDebut() : null)

                .seanceHeureFin(presence.getSeance() != null
                        ? presence.getSeance().getHeureFin() : null)

                // Numéro de la salle
                .seanceSalleNom(presence.getSeance() != null
                        && presence.getSeance().getSalle() != null
                        ? presence.getSeance().getSalle().getNumero() : null)

                // Nom de l'enseignant
                .seanceEnseignantNom(presence.getSeance() != null
                        && presence.getSeance().getEnseignant() != null
                        ? presence.getSeance().getEnseignant().getNom() : null)
                // ────────────────────────────────────────

                .build();
    }
}