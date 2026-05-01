package com.fst.gestionpresencepointage.service;

import com.fst.gestionpresencepointage.dto.EtudiantDto;
import com.fst.gestionpresencepointage.models.enums.RoleUtilisateur;

import java.util.List;

public interface EtudiantService {

    EtudiantDto enregistrer(EtudiantDto dto);
    // reçoit un DTO, retourne un DTO ✅

    List<EtudiantDto> listerTous();

    EtudiantDto rechercherParId(Long id);
    // pas Optional — on lance une erreur si non trouvé ✅

    EtudiantDto rechercherParMatricule(String matricule);

    List<EtudiantDto> listerParDepartement(Long departementId);

    EtudiantDto modifier(Long id, EtudiantDto dto);

    void supprimer(Long id);

    // Assigner un groupe à une liste d'étudiants
    void assignerGroupe(List<Long> etudiantIds, String groupe);

    // Lister par département ET niveau
    List<EtudiantDto> listerParDepartementEtNiveau(Long departementId, String niveau);
}