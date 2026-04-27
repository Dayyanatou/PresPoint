package com.fst.gestionpresencepointage.service.impl;

import com.fst.gestionpresencepointage.dto.EnseignantDto;
import com.fst.gestionpresencepointage.models.Departement;
import com.fst.gestionpresencepointage.models.Enseignant;
import com.fst.gestionpresencepointage.repository.DepartementRepository;
import com.fst.gestionpresencepointage.repository.EnseignantRepository;
import com.fst.gestionpresencepointage.service.DepartementService;
import com.fst.gestionpresencepointage.service.EnseignantService;
import lombok.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Service
@RequiredArgsConstructor
@Transactional
// ↑ @Transactional = toutes les méthodes s'exécutent dans une transaction
// Si une erreur survient au milieu → tout est annulé automatiquement
// Comme un virement bancaire : soit tout passe, soit rien ne passe ✅
public class EnseignantServiceImpl implements EnseignantService {

    private final EnseignantRepository enseignantRepository;
    private final DepartementRepository departementRepository;
    private final DepartementService departementService;

    @Override
    public EnseignantDto enregistrer(EnseignantDto dto) {

        // ── Conversion DTO → Entité ───────────────────────────
        Enseignant enseignant = new Enseignant();

        // Champs hérités de Utilisateur (OBLIGATOIRES)
        enseignant.setNom(dto.getNom());
        enseignant.setPrenom(dto.getPrenom());
        enseignant.setEmail(dto.getEmail());
        enseignant.setUsername(dto.getUsername());       // ✅ Ajouté
        enseignant.setMotDePasse(dto.getMotDePasse());   // ✅ Ajouté
        enseignant.setRole(dto.getRole());               // ✅ Ajouté
        enseignant.setTelephone(dto.getTelephone());

        // Champs spécifiques à Enseignant
        enseignant.setGrade(dto.getGrade());
        enseignant.setSpecialite(dto.getSpecialite());
        enseignant.setStatutEnseignant(dto.getStatutEnseignant());
        enseignant.setTauxHoraire(dto.getTauxHoraire());

        // ── Gestion du Département ──────────────────────────
        if (dto.getDepartementId() == null) {
            enseignant.setDepartement(departementService.getDepartementParDefaut());
        } else {
            Departement departement = departementRepository
                    .findById(dto.getDepartementId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Département ID [" + dto.getDepartementId() + "] introuvable."
                    ));
            enseignant.setDepartement(departement);
        }

        return EnseignantDto.fromEntity(enseignantRepository.save(enseignant));
    }
    @Override
    public List<EnseignantDto> listerTous() {
        return enseignantRepository.findAll()
                .stream()
                .map(EnseignantDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public EnseignantDto rechercherParId(Long id) {
        return EnseignantDto.fromEntity(
                enseignantRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Enseignant introuvable."))
        );
    }

    @Override
    public List<EnseignantDto> listerParDepartement(Long departementId) {
        Departement departement = departementRepository.findById(departementId)
                .orElseThrow(() -> new RuntimeException("Département introuvable."));
        return enseignantRepository.findByDepartement(departement)
                .stream()
                .map(EnseignantDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnseignantDto> rechercherParGrade(String grade) {
        return enseignantRepository.findByGradeContainingIgnoreCase(grade)
                .stream()
                .map(EnseignantDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnseignantDto> rechercherParStatut(String statut) {
        return enseignantRepository.findByStatutEnseignantContainingIgnoreCase(statut)
                .stream()
                .map(EnseignantDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public EnseignantDto modifier(Long id, EnseignantDto dto) {
        Enseignant existant = enseignantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enseignant introuvable."));

        // Mise à jour des champs hérités (Utilisateur)
        existant.setNom(dto.getNom());
        existant.setPrenom(dto.getPrenom());
        existant.setEmail(dto.getEmail());
        existant.setTelephone(dto.getTelephone());

        // Mise à jour des champs spécifiques (Enseignant)
        existant.setGrade(dto.getGrade());
        existant.setStatutEnseignant(dto.getStatutEnseignant());
        existant.setTauxHoraire(dto.getTauxHoraire());
        existant.setSpecialite(dto.getSpecialite());

        return EnseignantDto.fromEntity(enseignantRepository.save(existant));
    }

    @Override
    public void supprimer(Long id) {
        if (!enseignantRepository.existsById(id)) {
            throw new RuntimeException("Enseignant introuvable.");
        }
        enseignantRepository.deleteById(id); // ← ne pas oublier cette ligne !
    }
}