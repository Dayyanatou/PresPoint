package com.fst.gestionpresencepointage.service.impl;

import com.fst.gestionpresencepointage.models.Departement;
import com.fst.gestionpresencepointage.models.Enseignant;
import com.fst.gestionpresencepointage.models.ModuleCours;
import com.fst.gestionpresencepointage.repository.DepartementRepository;
import com.fst.gestionpresencepointage.repository.EnseignantRepository;
import com.fst.gestionpresencepointage.repository.ModuleCoursRepository;
import com.fst.gestionpresencepointage.service.ModuleCoursService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModuleCoursServiceImpl implements ModuleCoursService {

    private final ModuleCoursRepository moduleCoursRepository;
    private final DepartementRepository departementRepository;
    private final EnseignantRepository enseignantRepository;

    @Override
    public ModuleCours enregistrer(ModuleCours moduleCours) {
        if (moduleCoursRepository.existsByCode(moduleCours.getCode())) {
            throw new RuntimeException("Un module avec ce code existe déjà.");
        }
        return moduleCoursRepository.save(moduleCours);
    }

    @Override
    public ModuleCours modifier(Long id, ModuleCours dto) {
        ModuleCours existant = moduleCoursRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Module introuvable."));
        existant.setIntitule(dto.getIntitule());
        existant.setCode(dto.getCode());
        existant.setVolumeHoraire(dto.getVolumeHoraire());
        existant.setSemestre(dto.getSemestre());
        existant.setNiveau(dto.getNiveau());
        existant.setGroupe(dto.getGroupe());
        existant.setEnseignantResponsable(dto.getEnseignantResponsable());
        return moduleCoursRepository.save(existant);
    }

    @Override
    public List<ModuleCours> listerTous() {
        return moduleCoursRepository.findAll();
    }

    @Override
    public Optional<ModuleCours> rechercherParId(Long id) {
        return moduleCoursRepository.findById(id);
    }

    @Override
    public Optional<ModuleCours> rechercherParCode(String code) {
        return moduleCoursRepository.findByCode(code);
    }

    @Override
    public List<ModuleCours> listerParDepartement(Long departementId) {
        Departement dep = departementRepository.findById(departementId)
                .orElseThrow(() -> new RuntimeException("Département introuvable."));
        return moduleCoursRepository.findByDepartement(dep);
    }

    @Override
    public List<ModuleCours> listerParDepartementEtNiveau(
            Long departementId, String niveau) {
        Departement dep = departementRepository.findById(departementId)
                .orElseThrow(() -> new RuntimeException("Département introuvable."));
        return moduleCoursRepository.findByDepartementAndNiveau(dep, niveau);
    }

    @Override
    public List<ModuleCours> listerParEnseignant(Long enseignantId) {
        Enseignant enseignant = enseignantRepository.findById(enseignantId)
                .orElseThrow(() -> new RuntimeException("Enseignant introuvable."));
        return moduleCoursRepository.findByEnseignantResponsable(enseignant);
    }

    @Override
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