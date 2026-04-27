package com.fst.gestionpresencepointage.models;

import com.fst.gestionpresencepointage.models.enums.RoleUtilisateur;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "utilisateurs",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_utilisateur_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_utilisateur_username", columnNames = "username")
        },
        indexes = {
                @Index(name = "idx_utilisateur_email", columnList = "email"),
                @Index(name = "idx_utilisateur_role", columnList = "role")
        }
)
@Inheritance(strategy = InheritanceType.JOINED)
// JOINED = une table utilisateurs + une table par classe fille
// Les champs communs restent dans utilisateurs ✅

@EntityListeners(AuditingEntityListener.class)
// Permet à @CreatedDate de fonctionner automatiquement

@Data
@SuperBuilder       // obligatoire car cette classe a des enfants
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom ne doit pas être nulle.")//personnalisation de message de validation
    @Column(nullable = false, length = 100)
    private String nom;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String prenom;

    @Email
    @NotBlank
    @Column(nullable = false, length = 150, unique = true)
    private String email;

    @NotBlank
    @Column(name = "username", nullable = false, length = 100, unique = true)
    private String username;

    @Column(length = 20)
    private String telephone;
    // String et non Integer — un numéro de téléphone n'est pas un chiffre

    @NotBlank
    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;
    // updatable = false → ne peut jamais être modifiée après création ✅

    @Builder.Default
    @Column(nullable = false)
    private Boolean statut = true;
    // true = actif par défaut

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private RoleUtilisateur role;

    // Dans la classe Utilisateur — ajouter ce champ
    @Column(name = "premiere_connexion", nullable = false)
    private boolean premiereConnexion = true;
    // RoleUtilisateur = enum à créer dans le dossier models/enums/

    /*@PrePersist
    public void avantCreation() {
        if (dateCreation == null) {
            dateCreation = LocalDateTime.now();
        }
        if (statut == null) {
            statut = true;
        }
    }*/
}