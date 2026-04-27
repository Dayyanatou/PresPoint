package com.fst.gestionpresencepointage.service;

import com.fst.gestionpresencepointage.dto.PointageDto;
import com.fst.gestionpresencepointage.models.enums.StatutPointage;
import java.util.List;

public interface PointageService {

    PointageDto soumettre(PointageDto dto);          // enseignant soumet
    PointageDto confirmer(Long id, Long superviseurId); // superviseur confirme
    PointageDto valider(Long id, Long chefId);          // chef valide
    PointageDto rejeter(Long id, Long chefId);          // chef rejette

    PointageDto getById(Long id);
    List<PointageDto> listerParEnseignant(Long enseignantId);
    List<PointageDto> listerParStatut(StatutPointage statut);
    List<PointageDto> listerEnAttente();
    // Dans PointageService.java — ajouter cette ligne
    List<PointageDto> listerEnAttenteParDepartement(Long departementId);
}