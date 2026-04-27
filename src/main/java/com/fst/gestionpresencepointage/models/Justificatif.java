package com.fst.gestionpresencepointage.models;

import com.fst.gestionpresencepointage.models.enums.StatutJustificatif;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "justificatifs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Justificatif {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Motif de l'absence : "Maladie", "Urgence familiale", etc.
    @Column(nullable = false, length = 100)
    private String motif;

    // Description optionnelle écrite par l'étudiant
    @Column(length = 500)
    private String description;

    // Chemin du fichier sur le serveur
    // Exemple : "uploads/justificatifs/certificat_12_2026.pdf"
    @Column(length = 255)
    private String cheminFichier;

    // Nom original du fichier (ex: "certificat_medical.pdf")
    @Column(length = 255)
    private String nomFichierOriginal;

    // Date où l'étudiant a soumis le justificatif
    @Column(nullable = false)
    private LocalDateTime dateSoumission;

    // Statut : EN_ATTENTE → ACCEPTE ou REFUSE
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutJustificatif statut;

    // Commentaire du chef lors de la validation/refus
    @Column(length = 255)
    private String commentaireChef;

    // L'étudiant qui soumet le justificatif
    @ManyToOne
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;

    // La présence concernée (séance où l'étudiant était absent)
    @ManyToOne
    @JoinColumn(name = "presence_id", nullable = false)
    private Presence presence;

    // Qui a traité le justificatif (chef de département)
    @ManyToOne
    @JoinColumn(name = "chef_departement_id")
    private ChefDepartement chefDepartement;
}