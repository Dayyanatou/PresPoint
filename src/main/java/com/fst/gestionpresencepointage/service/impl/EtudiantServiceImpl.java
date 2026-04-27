package com.fst.gestionpresencepointage.service.impl;

import com.fst.gestionpresencepointage.dto.EtudiantDto;
import com.fst.gestionpresencepointage.models.Departement;
import com.fst.gestionpresencepointage.models.Etudiant;
import com.fst.gestionpresencepointage.models.enums.RoleUtilisateur;
import com.fst.gestionpresencepointage.repository.DepartementRepository;
import com.fst.gestionpresencepointage.repository.EtudiantRepository;
import com.fst.gestionpresencepointage.service.DepartementService;
import com.fst.gestionpresencepointage.service.EtudiantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EtudiantServiceImpl implements EtudiantService {

    // On connecte le service à la base de données via le repository
    private final EtudiantRepository etudiantRepository;

    private final DepartementRepository departementRepository;

    private final DepartementService departementService;

    // =========================
    // AJOUTER UN ETUDIANT
    // =========================
    @Override
    public EtudiantDto enregistrer(EtudiantDto dto) {

        // 1. Vérifications
        if (dto.getMatricule() == null || dto.getMatricule().trim().isEmpty()) {
            throw new RuntimeException("Le matricule est obligatoire.");
        }

        // Vérification doublon
        if (etudiantRepository.existsByMatricule(dto.getMatricule())) {
            throw new RuntimeException("Un étudiant avec le matricule "
                    + dto.getMatricule() + " existe déjà.");
        }

        // 2. Création de l'entité
        Etudiant etudiant = new Etudiant();

        // 3. Copie des données
        etudiant.setNom(dto.getNom());
        etudiant.setPrenom(dto.getPrenom());
        etudiant.setEmail(dto.getEmail());
        etudiant.setUsername(dto.getUsername());
        etudiant.setTelephone(dto.getTelephone());
        etudiant.setMatricule(dto.getMatricule());
        etudiant.setNiveau(dto.getNiveau());
        etudiant.setAdresse(dto.getAdresse());
        etudiant.setLieuNaissance(dto.getLieuNaissance());
        etudiant.setDateNaissance(dto.getDateNaissance());
        etudiant.setPersonneAContacter(dto.getPersonneAContacter());

        // === AJOUT IMPORTANT ===
        etudiant.setRole(RoleUtilisateur.ETUDIANT);   // Toujours ETUDIANT pour cette entité

        // Gestion du mot de passe (solution temporaire puisque tu n'as pas encore la sécurité)
        if (dto.getMotDePasse() != null && !dto.getMotDePasse().trim().isEmpty()) {
            etudiant.setMotDePasse(dto.getMotDePasse());   // On le prend tel quel pour l'instant
        } else {
            // Mot de passe par défaut si l'utilisateur n'en met pas
            etudiant.setMotDePasse("password123");   // ← À changer plus tard
        }
        // =======================

        // 4. Gestion du département
        // Gestion du département - Correction importante
        if (dto.getDepartementId() == null || dto.getDepartementId() == 0) {
            throw new RuntimeException("Veuillez choisir un département.");
        }

        Departement dep = departementRepository.findById(dto.getDepartementId())
                .orElseThrow(() -> new RuntimeException("Département introuvable."));
        etudiant.setDepartement(dep);

        // 5. Sauvegarde
        Etudiant saved = etudiantRepository.save(etudiant);

        return EtudiantDto.fromEntity(saved);
    }
    // =========================
    // LISTE DES ETUDIANTS
    // =========================
    @Override
    public List<EtudiantDto> listerTous() {

        return etudiantRepository.findAll()
                .stream() // transforme la liste en flux
                .map(EtudiantDto::fromEntity) // conversion ENTITY → DTO
                .toList(); // reconstruire la liste
    }

    // =========================
    // RECHERCHE PAR ID
    // =========================
    @Override
    public EtudiantDto rechercherParId(Long id) {

        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Étudiant introuvable."));

        return EtudiantDto.fromEntity(etudiant);
    }

    // =========================
    // RECHERCHE PAR MATRICULE
    // =========================
    @Override
    public EtudiantDto rechercherParMatricule(String matricule) {

        Etudiant etudiant = etudiantRepository.findByMatricule(matricule)
                .orElseThrow(() -> new RuntimeException("Matricule introuvable."));

        return EtudiantDto.fromEntity(etudiant);
    }

    // =========================
    // FILTRE PAR DEPARTEMENT
    // =========================
    @Override
    public List<EtudiantDto> listerParDepartement(Long departementId) {

        Departement dep = departementRepository.findById(departementId)
                .orElseThrow(() -> new RuntimeException("Département introuvable."));

        return etudiantRepository.findByDepartement(dep)
                .stream()
                .map(EtudiantDto::fromEntity)
                .toList();
    }

    // =========================
    // MODIFIER UN ETUDIANT
    // =========================
    @Override
    public EtudiantDto modifier(Long id, EtudiantDto dto) {

        // 1️⃣ récupérer l’étudiant existant
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Étudiant introuvable."));

        // 2️⃣ modifier les champs
        etudiant.setNom(dto.getNom());
        etudiant.setPrenom(dto.getPrenom());
        etudiant.setEmail(dto.getEmail());
        etudiant.setNiveau(dto.getNiveau());
        etudiant.setAdresse(dto.getAdresse());
        etudiant.setLieuNaissance(dto.getLieuNaissance());
        etudiant.setTelephone(dto.getTelephone());

        // 3️⃣ sauvegarder
        Etudiant updated = etudiantRepository.save(etudiant);

        return EtudiantDto.fromEntity(updated);
    }

    // =========================
    // SUPPRESSION
    // =========================
    @Override
    public void supprimer(Long id) {

        if (!etudiantRepository.existsById(id)) {
            throw new RuntimeException("Étudiant introuvable.");
        }

        etudiantRepository.deleteById(id);
    }
}
