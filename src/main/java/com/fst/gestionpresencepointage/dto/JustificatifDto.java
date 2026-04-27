package com.fst.gestionpresencepointage.dto;

import com.fst.gestionpresencepointage.models.Justificatif;
import com.fst.gestionpresencepointage.models.enums.StatutJustificatif;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JustificatifDto {

    private Long id;
    private String motif;
    private String description;
    private String cheminFichier;
    private String nomFichierOriginal;
    private LocalDateTime dateSoumission;
    private StatutJustificatif statut;
    private String commentaireChef;

    // Infos étudiant
    private Long etudiantId;
    private String etudiantNom;
    private String etudiantMatricule;

    // Infos présence concernée
    private Long presenceId;
    private String presenceSeanceModuleNom;
    private java.time.LocalDate presenceDateSeance;

    public static JustificatifDto fromEntity(Justificatif j) {
        return JustificatifDto.builder()
                .id(j.getId())
                .motif(j.getMotif())
                .description(j.getDescription())
                .cheminFichier(j.getCheminFichier())
                .nomFichierOriginal(j.getNomFichierOriginal())
                .dateSoumission(j.getDateSoumission())
                .statut(j.getStatut())
                .commentaireChef(j.getCommentaireChef())

                .etudiantId(j.getEtudiant() != null
                        ? j.getEtudiant().getId() : null)
                .etudiantNom(j.getEtudiant() != null
                        ? j.getEtudiant().getNom() + " " + j.getEtudiant().getPrenom()
                        : null)
                .etudiantMatricule(j.getEtudiant() != null
                        ? j.getEtudiant().getMatricule() : null)

                .presenceId(j.getPresence() != null
                        ? j.getPresence().getId() : null)
                .presenceSeanceModuleNom(j.getPresence() != null
                        && j.getPresence().getSeance() != null
                        && j.getPresence().getSeance().getModuleCours() != null
                        ? j.getPresence().getSeance().getModuleCours().getIntitule()
                        : null)
                .presenceDateSeance(j.getPresence() != null
                        && j.getPresence().getSeance() != null
                        ? j.getPresence().getSeance().getDateSeance() : null)

                .build();
    }
}