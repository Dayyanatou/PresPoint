package com.fst.gestionpresencepointage.dto;

import com.fst.gestionpresencepointage.models.Enseignant;
import com.fst.gestionpresencepointage.models.enums.RoleUtilisateur;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnseignantDto {

    // Champs hérités de Utilisateur
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String username;
    private String motDePasse;
    private String telephone;
    private RoleUtilisateur role;

    // Champs spécifiques à Enseignant
    private String grade;
    private String statutEnseignant;
    private String specialite;
    private String profil;
    private Integer tauxHoraire;

    // Infos du département
    private Long departementId;
    private String departementNom;

    // Conversion Enseignant → EnseignantDto
    public static EnseignantDto fromEntity(Enseignant enseignant) {
        return EnseignantDto.builder()
                .id(enseignant.getId())
                .nom(enseignant.getNom())           // hérité de Utilisateur ✅
                .prenom(enseignant.getPrenom())     // hérité de Utilisateur ✅
                .email(enseignant.getEmail())       // hérité de Utilisateur ✅
                .username(enseignant.getUsername()) // hérité de Utilisateur ✅
                .motDePasse(enseignant.getMotDePasse())// hérité de Utilisateur ✅
                .telephone(enseignant.getTelephone())
                .role(enseignant.getRole())
                .grade(enseignant.getGrade())
                .statutEnseignant(enseignant.getStatutEnseignant())
                .specialite(enseignant.getSpecialite())
                .profil(enseignant.getProfil())
                .tauxHoraire(enseignant.getTauxHoraire())
                .departementId(enseignant.getDepartement() != null
                        ? enseignant.getDepartement().getId() : null)
                .departementNom(enseignant.getDepartement() != null
                        ? enseignant.getDepartement().getNom() : null)
                .build();
    }
}