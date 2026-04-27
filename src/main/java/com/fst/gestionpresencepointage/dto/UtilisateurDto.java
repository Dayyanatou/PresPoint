package com.fst.gestionpresencepointage.dto;

import com.fst.gestionpresencepointage.models.Utilisateur;
import com.fst.gestionpresencepointage.models.enums.RoleUtilisateur;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurDto {

    private Long id;

    private String nom;
    private String prenom;

    private String email;
    private String username;
    private String telephone;

    private Boolean statut;
    private RoleUtilisateur role;

    private String motDePasse;

    private LocalDateTime dateCreation;

    // 🔥 étudiant
    private String matricule;
    private String niveau;

    // 🔥 enseignant
    private String grade;
    private String specialite;

    private Long departementId;

    // ⚠️ PAS de departement ici (héritage oblige)
    // réservé aux enfants (Etudiant/Enseignant)

    public static UtilisateurDto fromEntity(Utilisateur utilisateur) {
        if (utilisateur == null) return null;

        return UtilisateurDto.builder()
                .id(utilisateur.getId())
                .nom(utilisateur.getNom())
                .prenom(utilisateur.getPrenom())
                .email(utilisateur.getEmail())
                .username(utilisateur.getUsername())
                .telephone(utilisateur.getTelephone())
                .statut(utilisateur.getStatut())
                .role(utilisateur.getRole())
                .dateCreation(utilisateur.getDateCreation())
                .build();
    }
}

/*
package com.fst.gestionpresencepointage.dto;

import com.fst.gestionpresencepointage.models.Utilisateur;
import com.fst.gestionpresencepointage.models.enums.RoleUtilisateur;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class UtilisateurDto {

    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String username;
    private String telephone;
    private Boolean statut;
    private RoleUtilisateur role;
    private LocalDateTime dateCreation;


    public static UtilisateurDto fromEntity(Utilisateur utilisateur) {
        return UtilisateurDto.builder()
                .id(utilisateur.getId())
                .nom(utilisateur.getNom())
                .prenom(utilisateur.getPrenom())
                .email(utilisateur.getEmail())
                .username(utilisateur.getUsername())
                .telephone(utilisateur.getTelephone())
                .statut(utilisateur.getStatut())
                .role(utilisateur.getRole())
                .dateCreation(utilisateur.getDateCreation())
                .build();
    }
}*/
