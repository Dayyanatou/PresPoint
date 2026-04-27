package com.fst.gestionpresencepointage.service;

import com.fst.gestionpresencepointage.models.Salle;

import java.util.List;
import java.util.Optional;

public interface SalleService {

    Salle enregistrer(Salle salle);

    List<Salle> listerToutes();

    Optional<Salle> rechercherParId(Long id);

    Optional<Salle> rechercherParNumero(String numero);

    List<Salle> listerSallesCompatibles(Integer capaciteMinimum);

    void supprimer(Long id);
}