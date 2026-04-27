package com.fst.gestionpresencepointage.service;

import com.fst.gestionpresencepointage.dto.DepartementDto;
import com.fst.gestionpresencepointage.models.Departement;
import java.util.List;

public interface DepartementService {

    DepartementDto enregistrer(DepartementDto dto);
    // ↑ reçoit et retourne un DTO

    List<DepartementDto> listerTous();

    DepartementDto rechercherParId(Long id);

    DepartementDto rechercherParNom(String nom);

    DepartementDto modifier(Long id, DepartementDto dto);

    void supprimer(Long id);

    Departement getDepartementParDefaut();
    // ↑ retourne une entité — utilisé en interne par les autres services
    // pas un DTO car on assigne directement à etudiant.setDepartement(...)
}