package com.fst.gestionpresencepointage.dto;

import com.fst.gestionpresencepointage.models.ChefDepartement;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChefDepartementDto {

    // Champs hérités de Utilisateur
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String username;

    // Son département
    private Long departementId;
    private String departementNom;

    public static ChefDepartementDto fromEntity(ChefDepartement chef) {
        return ChefDepartementDto.builder()
                .id(chef.getId())
                .nom(chef.getNom())
                .prenom(chef.getPrenom())
                .email(chef.getEmail())
                .username(chef.getUsername())
                .departementId(chef.getDepartement() != null
                        ? chef.getDepartement().getId() : null)
                .departementNom(chef.getDepartement() != null
                        ? chef.getDepartement().getNom() : null)
                .build();
    }
}