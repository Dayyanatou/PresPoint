package com.fst.gestionpresencepointage.service.impl;

import com.fst.gestionpresencepointage.models.Salle;
import com.fst.gestionpresencepointage.repository.SalleRepository;
import com.fst.gestionpresencepointage.service.SalleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SalleServiceImpl implements SalleService {

    private final SalleRepository salleRepository;

    @Override
    public Salle enregistrer(Salle salle) {

        if (salle == null) {
            throw new RuntimeException("La salle ne doit pas être null.");
        }

        if (salle.getNumero() == null || salle.getNumero().trim().isEmpty()) {
            throw new RuntimeException("Le numéro de salle est obligatoire.");
        }

        if (salle.getCapacite() == null || salle.getCapacite() <= 0) {
            throw new RuntimeException("La capacité doit être supérieure à 0.");
        }

        String numeroNormalise = salle.getNumero().trim().toUpperCase();
        salle.setNumero(numeroNormalise);

        Optional<Salle> salleExistante = salleRepository.findByNumero(numeroNormalise);

        if (salleExistante.isPresent()
                && (salle.getId() == null || !salleExistante.get().getId().equals(salle.getId()))) {
            throw new RuntimeException("Une salle avec ce numéro existe déjà.");
        }

        return salleRepository.save(salle);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Salle> listerToutes() {
        return salleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Salle> rechercherParId(Long id) {
        return salleRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Salle> rechercherParNumero(String numero) {
        return salleRepository.findByNumero(numero.trim().toUpperCase());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Salle> listerSallesCompatibles(Integer capaciteMinimum) {
        return salleRepository.findByCapaciteGreaterThanEqual(capaciteMinimum);
    }

    @Override
    public void supprimer(Long id) {
        if (!salleRepository.existsById(id)) {
            throw new RuntimeException("Salle introuvable.");
        }
        salleRepository.deleteById(id);
    }
}