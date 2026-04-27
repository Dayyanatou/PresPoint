package com.fst.gestionpresencepointage.service;

import com.fst.gestionpresencepointage.models.ModuleCours;

import java.util.List;
import java.util.Optional;

public interface ModuleCoursService {
    ModuleCours enregistrer(ModuleCours moduleCours);

    List<ModuleCours> listerTous();

    Optional<ModuleCours> rechercherParId(Long id);

    Optional<ModuleCours> rechercherParCode(String code);

    List<ModuleCours> listerParDepartement(Long departementId);

    List<ModuleCours> listerParEnseignant(Long enseignantId);

    List<ModuleCours> rechercherParIntitule(String motCle);

    void supprimer(Long id);
}
