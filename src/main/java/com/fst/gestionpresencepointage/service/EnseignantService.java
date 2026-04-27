package com.fst.gestionpresencepointage.service;

import com.fst.gestionpresencepointage.dto.EnseignantDto;
import java.util.List;

public interface EnseignantService {

    EnseignantDto enregistrer(EnseignantDto dto);

    List<EnseignantDto> listerTous();

    EnseignantDto rechercherParId(Long id);

    List<EnseignantDto> listerParDepartement(Long departementId);

    List<EnseignantDto> rechercherParGrade(String grade);

    List<EnseignantDto> rechercherParStatut(String statut);

    EnseignantDto modifier(Long id, EnseignantDto dto);

    void supprimer(Long id);
}