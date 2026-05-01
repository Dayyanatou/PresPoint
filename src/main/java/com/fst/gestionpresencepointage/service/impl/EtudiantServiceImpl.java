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

        // 1. Vérification matricule obligatoire
        if (dto.getMatricule() == null || dto.getMatricule().trim().isEmpty()) {
            throw new RuntimeException("Le matricule est obligatoire.");
        }

        // 2. Vérification doublon matricule
        if (etudiantRepository.existsByMatricule(dto.getMatricule())) {
            // Si doublon → on ignore silencieusement (import en masse)
            return rechercherParMatricule(dto.getMatricule());
        }

        // 3. Création entité
        Etudiant etudiant = new Etudiant();

        etudiant.setNom(dto.getNom());
        etudiant.setPrenom(dto.getPrenom());

        // Email : null autorisé à l'import
        etudiant.setEmail(dto.getEmail());

        // Username = matricule (ex: INF2504001)
        String username = dto.getMatricule().replace(" ", "");
        etudiant.setUsername(username);

        etudiant.setTelephone(dto.getTelephone());
        etudiant.setMatricule(dto.getMatricule());
        etudiant.setNiveau(dto.getNiveau());
        etudiant.setGroupe(dto.getGroupe()); // null à l'import
        etudiant.setAdresse(dto.getAdresse());
        etudiant.setLieuNaissance(dto.getLieuNaissance());
        etudiant.setDateNaissance(dto.getDateNaissance());
        etudiant.setPersonneAContacter(dto.getPersonneAContacter());

        // Rôle
        etudiant.setRole(RoleUtilisateur.ETUDIANT);

        // Statut actif
        etudiant.setStatut(dto.getStatut() != null ? dto.getStatut() : true);

        // Première connexion = true → forcera changement mot de passe
        etudiant.setPremiereConnexion(
                dto.getPremiereConnexion() != null ? dto.getPremiereConnexion() : true);

        // Mot de passe = matricule (sans espace)
        // L'étudiant devra le changer à la première connexion
        String motDePasse = username; // ex: INF2504001
        etudiant.setMotDePasse(motDePasse);

        // 4. Département obligatoire
        if (dto.getDepartementId() == null) {
            throw new RuntimeException("Veuillez choisir un département.");
        }
        Departement dep = departementRepository.findById(dto.getDepartementId())
                .orElseThrow(() -> new RuntimeException("Département introuvable."));
        etudiant.setDepartement(dep);

        // 5. Sauvegarde
        Etudiant saved = etudiantRepository.save(etudiant);
        return EtudiantDto.fromEntity(saved);
    }    // =========================
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

    // =========================
// ASSIGNER GROUPE G1 / G2
// =========================
    @Override
    public void assignerGroupe(List<Long> etudiantIds, String groupe) {
        List<Etudiant> etudiants = etudiantRepository.findAllById(etudiantIds);
        etudiants.forEach(e -> e.setGroupe(groupe));
        etudiantRepository.saveAll(etudiants);
    }

    // =========================
// LISTER PAR DEPARTEMENT + NIVEAU
// =========================
    @Override
    public List<EtudiantDto> listerParDepartementEtNiveau(Long departementId, String niveau) {
        Departement dep = departementRepository.findById(departementId)
                .orElseThrow(() -> new RuntimeException("Département introuvable."));

        return etudiantRepository.findByDepartement(dep)
                .stream()
                .filter(e -> niveau.equals(e.getNiveau()))
                .map(EtudiantDto::fromEntity)
                .toList();
    }
}
