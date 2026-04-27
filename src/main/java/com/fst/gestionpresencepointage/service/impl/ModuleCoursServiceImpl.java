package com.fst.gestionpresencepointage.service.impl;

import com.fst.gestionpresencepointage.models.Departement;
import com.fst.gestionpresencepointage.models.Enseignant;
import com.fst.gestionpresencepointage.models.ModuleCours;
import com.fst.gestionpresencepointage.models.Etudiant;
import com.fst.gestionpresencepointage.repository.DepartementRepository;
import com.fst.gestionpresencepointage.repository.EnseignantRepository;
import com.fst.gestionpresencepointage.repository.ModuleCoursRepository;
import com.fst.gestionpresencepointage.service.DepartementService;
import com.fst.gestionpresencepointage.service.ModuleCoursService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ModuleCoursServiceImpl implements ModuleCoursService {
    private final ModuleCoursRepository moduleCoursRepository;
    private final DepartementRepository departementRepository;
    private final EnseignantRepository enseignantRepository;
    private final DepartementService departementService;

    @Override
    public ModuleCours enregistrer(ModuleCours moduleCours) {
        if (moduleCours == null) {
            throw new RuntimeException("Le module ne doit pas être null.");
        }
        if (moduleCours.getCode() == null || moduleCours.getCode().trim().isEmpty()) {
            throw new RuntimeException("Le code du module est obligatoire.");
        }

        if (moduleCours.getIntitule() == null || moduleCours.getIntitule().trim().isEmpty()) {
            throw new RuntimeException("L'intitulé du module est obligatoire.");
        }

        String codeNormalise = moduleCours.getCode().trim().toUpperCase();
        moduleCours.setCode(codeNormalise);
        Optional<ModuleCours> moduleExistant = moduleCoursRepository.findByCode(codeNormalise);
        if (moduleExistant.isPresent()
                && (moduleCours.getId() == null || !moduleExistant.get().getId().equals(moduleCours.getId()))) {
            throw new RuntimeException("Un module avec ce code existe déjà.");
        }

        // Département par défaut = informatique
        if (moduleCours.getDepartement() == null || moduleCours.getDepartement().getId() == null) {
            moduleCours.setDepartement(departementService.getDepartementParDefaut());
        } else {
            Departement departement = departementRepository.findById(moduleCours.getDepartement().getId())
                    .orElseThrow(() -> new RuntimeException("Département introuvable."));
            moduleCours.setDepartement(departement);
        }

        // L'enseignant responsable peut être facultatif selon ton besoin métier
        if (moduleCours.getEnseignantResponsable() != null
                && moduleCours.getEnseignantResponsable().getId() != null) {
            Enseignant enseignant = enseignantRepository.findById(moduleCours.getEnseignantResponsable().getId())
                    .orElseThrow();
            moduleCours.setEnseignantResponsable(enseignant);
        }

        return moduleCoursRepository.save(moduleCours);
    }
    @Override
    @Transactional(readOnly = true)
    public List<ModuleCours> listerTous() {
        return moduleCoursRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ModuleCours> rechercherParId(Long id) {
        return moduleCoursRepository.findById(id);
    }
    @Override
    @Transactional(readOnly = true)
    public Optional<ModuleCours> rechercherParCode(String code) {
        return moduleCoursRepository.findByCode(code.trim().toUpperCase());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ModuleCours> listerParDepartement(Long departementId) {
        Departement departement = departementRepository.findById(departementId)
                .orElseThrow(() -> new RuntimeException("Département introuvable."));
        return moduleCoursRepository.findByDepartement(departement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ModuleCours> listerParEnseignant(Long enseignantId) {
        Enseignant enseignant = enseignantRepository.findById(enseignantId)
                .orElseThrow(() -> new RuntimeException("Enseignant introuvable."));
        return moduleCoursRepository.findByEnseignantResponsable(enseignant);
    }
    @Override
    @Transactional(readOnly = true)
    public List<ModuleCours> rechercherParIntitule(String motCle) {
        return moduleCoursRepository.findByIntituleContainingIgnoreCase(motCle);
    }

    @Override
    public void supprimer(Long id) {
        if (!moduleCoursRepository.existsById(id)) {
            throw new RuntimeException("Module introuvable.");
        }
        moduleCoursRepository.deleteById(id);
    }

    }
