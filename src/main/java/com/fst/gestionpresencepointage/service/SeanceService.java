package com.fst.gestionpresencepointage.service;

import com.fst.gestionpresencepointage.dto.SeanceDto;
import com.fst.gestionpresencepointage.models.enums.TypeSeance;
import java.time.LocalDate;
import java.util.List;

public interface SeanceService {

    SeanceDto creer(SeanceDto dto);
    SeanceDto getById(Long id);
    List<SeanceDto> listerTous();
    List<SeanceDto> listerParEnseignant(Long enseignantId);
    List<SeanceDto> listerParModule(Long moduleId);
    List<SeanceDto> listerParDate(LocalDate date);
    List<SeanceDto> listerParPeriode(LocalDate debut, LocalDate fin);
    List<SeanceDto> listerParType(TypeSeance type);
    SeanceDto modifier(Long id, SeanceDto dto);
    void supprimer(Long id);
    // Dans SeanceService.java — ajouter ces deux lignes
    List<SeanceDto> getSeancesAujourdhui(Long departementId);
    List<SeanceDto> getSeancesDeLaSemaine(Long departementId);
    List<SeanceDto> getPlanningParDepartement(Long departementId);
    List<SeanceDto> getPlanningParEnseignant(Long enseignantId);
    SeanceDto creerSeancePlanifiee(SeanceDto dto);
}