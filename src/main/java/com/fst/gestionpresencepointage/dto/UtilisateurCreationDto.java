package com.fst.gestionpresencepointage.dto;

import lombok.Data;

@Data
public class UtilisateurCreationDto {

    private String nom;
    private String prenom;
    private String email;
    private String nomUtilisateur;
    private String motDePasse;
    private String confirmationMotDePasse;
    private String telephone;
    private String role;
}
