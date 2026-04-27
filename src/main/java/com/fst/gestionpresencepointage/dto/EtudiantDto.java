package com.fst.gestionpresencepointage.dto;

import com.fst.gestionpresencepointage.models.Etudiant;
import com.fst.gestionpresencepointage.models.enums.RoleUtilisateur;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EtudiantDto {

    private Long id;

    // Utilisateur
    private String nom;
    private String prenom;
    private String email;
    private String username;
    private String telephone;

    private String motDePasse;

    // ROLE + STATUT
    private RoleUtilisateur role;
    private Boolean statut;

    // Étudiant
    private String matricule;
    private String niveau;
    private String adresse;
    private String lieuNaissance;
    private LocalDate dateNaissance;
    private String personneAContacter;

    // Département
    private Long departementId;
    private String departementNom;

    // ENTITY → DTO
    public static EtudiantDto fromEntity(Etudiant etudiant) {
        if (etudiant == null) return null;

        return EtudiantDto.builder()
                .id(etudiant.getId())
                .nom(etudiant.getNom())
                .prenom(etudiant.getPrenom())
                .email(etudiant.getEmail())
                .username(etudiant.getUsername())
                .telephone(etudiant.getTelephone())

                .role(etudiant.getRole())
                .statut(etudiant.getStatut())

                .matricule(etudiant.getMatricule())
                .niveau(etudiant.getNiveau())
                .adresse(etudiant.getAdresse())
                .lieuNaissance(etudiant.getLieuNaissance())
                .dateNaissance(etudiant.getDateNaissance())
                .personneAContacter(etudiant.getPersonneAContacter())

                .departementId(etudiant.getDepartement() != null
                        ? etudiant.getDepartement().getId()
                        : null)

                .departementNom(etudiant.getDepartement() != null
                        ? etudiant.getDepartement().getNom()
                        : null)

                .build();
    }

    // DTO → ENTITY
    public Etudiant toEntity() {
        Etudiant etudiant = new Etudiant();

        etudiant.setId(this.id);
        etudiant.setNom(this.nom);
        etudiant.setPrenom(this.prenom);
        etudiant.setEmail(this.email);
        etudiant.setUsername(this.username);
        etudiant.setTelephone(this.telephone);

        etudiant.setMotDePasse(this.motDePasse);

        etudiant.setRole(this.role);
        etudiant.setStatut(this.statut != null ? this.statut : true);

        etudiant.setMatricule(this.matricule);
        etudiant.setNiveau(this.niveau);
        etudiant.setAdresse(this.adresse);
        etudiant.setLieuNaissance(this.lieuNaissance);
        etudiant.setDateNaissance(this.dateNaissance);
        etudiant.setPersonneAContacter(this.personneAContacter);

        return etudiant;
    }
}

/*
package com.fst.gestionpresencepointage.dto;

import com.fst.gestionpresencepointage.models.Etudiant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EtudiantDto {

    // Champs hérités d'Utilisateur
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String username;
    private String telephone;

    // Champs spécifiques à Etudiant
    private String matricule;
    private String niveau;
    private String adresse;
    private String lieuNaissance;
    private LocalDate dateNaissance;
    private String personneAContacter;

    // Infos du département
    private Long departementId;
    private String departementNom;

    public static EtudiantDto fromEntity(Etudiant etudiant) {
        return EtudiantDto.builder()
                .id(etudiant.getId())
                .nom(etudiant.getNom())
                .prenom(etudiant.getPrenom())
                .email(etudiant.getEmail())
                .username(etudiant.getUsername())
                .telephone(etudiant.getTelephone())
                .matricule(etudiant.getMatricule())
                .niveau(etudiant.getNiveau())
                .adresse(etudiant.getAdresse())
                .lieuNaissance(etudiant.getLieuNaissance())
                .dateNaissance(etudiant.getDateNaissance())
                .personneAContacter(etudiant.getPersonneAContacter())
                .departementId(etudiant.getDepartement() != null
                        ? etudiant.getDepartement().getId() : null)
                .departementNom(etudiant.getDepartement() != null
                        ? etudiant.getDepartement().getNom() : null)
                .build();
    }
}*/
