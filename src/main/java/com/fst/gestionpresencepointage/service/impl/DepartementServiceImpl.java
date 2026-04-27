package com.fst.gestionpresencepointage.service.impl;

import com.fst.gestionpresencepointage.dto.DepartementDto;
import com.fst.gestionpresencepointage.models.Departement;
import com.fst.gestionpresencepointage.repository.DepartementRepository;
import com.fst.gestionpresencepointage.service.DepartementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartementServiceImpl implements DepartementService {

    private final DepartementRepository departementRepository;

    @Override
    public DepartementDto enregistrer(DepartementDto dto) {
        if (dto.getNom() == null || dto.getNom().trim().isEmpty()) {
            throw new RuntimeException("Le nom du département est obligatoire.");
        }
        String nomNormalise = dto.getNom().trim().toLowerCase();
        if (departementRepository.existsByNomIgnoreCase(nomNormalise)) {
            throw new RuntimeException("Ce département existe déjà.");
        }
        Departement departement = new Departement();
        departement.setNom(nomNormalise);
        return DepartementDto.fromEntity(departementRepository.save(departement));
    }

    @Override
    public List<DepartementDto> listerTous() {
        return departementRepository.findAll()
                .stream()
                .map(DepartementDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public DepartementDto rechercherParId(Long id) {
        return DepartementDto.fromEntity(
                departementRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Département introuvable."))
        );
    }

    @Override
    public DepartementDto rechercherParNom(String nom) {
        return DepartementDto.fromEntity(
                departementRepository.findByNomIgnoreCase(nom)
                        .orElseThrow(() -> new RuntimeException("Département introuvable."))
        );
    }

    @Override
    public DepartementDto modifier(Long id, DepartementDto dto) {
        Departement existant = departementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Département introuvable."));
        existant.setNom(dto.getNom().trim().toLowerCase());
        return DepartementDto.fromEntity(departementRepository.save(existant));
    }

    @Override
    public void supprimer(Long id) {
        if (!departementRepository.existsById(id)) {
            throw new RuntimeException("Département introuvable.");
        }
        departementRepository.deleteById(id);
    }

    @Override
    public Departement getDepartementParDefaut() {
        return departementRepository.findByNomIgnoreCase("informatique")
                .orElseThrow(() -> new RuntimeException(
                        "Le département par défaut 'informatique' n'existe pas en base."));
    }
}