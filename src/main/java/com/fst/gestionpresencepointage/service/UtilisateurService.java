package com.fst.gestionpresencepointage.service;

import com.fst.gestionpresencepointage.dto.UtilisateurDto;
import com.fst.gestionpresencepointage.models.Utilisateur;

import java.util.List;

public interface UtilisateurService {

    //void creerCompte(UtilisateurDto dto);
    //UtilisateurDto creerCompte(UtilisateurDto dto);

    List<UtilisateurDto> listerComptes();

    UtilisateurDto obtenirParId(Long id);

    void activerDesactiverCompte(Long id);

    //UtilisateurDto getById(Long id);
    UtilisateurDto creerEtudiant(UtilisateurDto dto);
    UtilisateurDto creerEnseignant(UtilisateurDto dto);
    UtilisateurDto creerChefDepartement(UtilisateurDto dto);
    UtilisateurDto creerSuperviseur(UtilisateurDto dto);
    List<UtilisateurDto> listerTous();
    UtilisateurDto getById(Long id);
    void desactiver(Long id);
    void activer(Long id);
    //Utilisateur enregistrer(Utilisateur utilisateur);
    //List<Utilisateur> listerTous();
    //Utilisateur rechercherParEmail(String email);
    //Utilisateur modifier(Long id, Utilisateur utilisateur);
    //void supprimer(Long id);

}

/*
package com.fst.gestionpresencepointage.service;

import com.fst.gestionpresencepointage.dto.UtilisateurDto;
import com.fst.gestionpresencepointage.models.enums.RoleUtilisateur;
import java.util.List;

public interface UtilisateurService {

    UtilisateurDto getById(Long id);

    List<UtilisateurDto> getTousLesUtilisateurs();

    List<UtilisateurDto> getParRole(RoleUtilisateur role);

    UtilisateurDto creer(UtilisateurDto dto);

    UtilisateurDto modifier(Long id, UtilisateurDto dto);

    UtilisateurDto changerStatut(Long id, Boolean statut);

    List<UtilisateurDto> rechercherParNom(String motCle);

    void supprimer(Long id);
}*/
