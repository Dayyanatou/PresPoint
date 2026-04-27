package com.fst.gestionpresencepointage.service.impl;

import com.fst.gestionpresencepointage.dto.UtilisateurDto;
import com.fst.gestionpresencepointage.models.*;
import com.fst.gestionpresencepointage.models.enums.RoleUtilisateur;
import com.fst.gestionpresencepointage.repository.*;
import com.fst.gestionpresencepointage.service.UtilisateurService;
import lombok.*;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final EtudiantRepository etudiantRepository;
    private final EnseignantRepository enseignantRepository;
    private final ChefDepartementRepository chefDepartementRepository;
    private final SuperviseurRepository superviseurRepository;
    private final DepartementRepository departementRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UtilisateurDto creerEtudiant(UtilisateurDto dto) {
        if (utilisateurRepository.existsByUsername(dto.getUsername()))
            throw new RuntimeException("Username déjà utilisé");

        Departement dept = departementRepository.findById(dto.getDepartementId() != null ? dto.getDepartementId() : 1L)
                .orElseThrow(() -> new RuntimeException("Département introuvable"));

        // Mot de passe = matricule par défaut
        String mdp = dto.getMatricule() != null ? dto.getMatricule() : dto.getUsername();

        Etudiant etudiant = Etudiant.builder()
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .email(dto.getEmail())
                .username(dto.getUsername())
                .motDePasse(passwordEncoder.encode(mdp))
                .telephone(dto.getTelephone())
                .role(RoleUtilisateur.ETUDIANT)
                .statut(true)
                .matricule(dto.getMatricule())
                .niveau(dto.getNiveau())
                .departement(dept)
                .build();

        etudiantRepository.save(etudiant);
        return UtilisateurDto.fromEntity(etudiant);
    }

    @Override
    public UtilisateurDto creerEnseignant(UtilisateurDto dto) {
        if (utilisateurRepository.existsByUsername(dto.getUsername()))
            throw new RuntimeException("Username déjà utilisé");

        // Mot de passe = email par défaut
        String mdp = dto.getEmail() != null ? dto.getEmail() : dto.getUsername();

        Enseignant enseignant = Enseignant.builder()
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .email(dto.getEmail())
                .username(dto.getUsername())
                .motDePasse(passwordEncoder.encode(mdp))
                .telephone(dto.getTelephone())
                .role(RoleUtilisateur.ENSEIGNANT)
                .statut(true)
                .grade(dto.getGrade())
                .specialite(dto.getSpecialite())
                .build();

        enseignantRepository.save(enseignant);
        return UtilisateurDto.fromEntity(enseignant);
    }

    @Override
    public UtilisateurDto creerChefDepartement(UtilisateurDto dto) {
        if (utilisateurRepository.existsByUsername(dto.getUsername()))
            throw new RuntimeException("Username déjà utilisé");

        String mdp = dto.getEmail() != null ? dto.getEmail() : dto.getUsername();

        ChefDepartement chef = ChefDepartement.builder()
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .email(dto.getEmail())
                .username(dto.getUsername())
                .motDePasse(passwordEncoder.encode(mdp))
                .telephone(dto.getTelephone())
                .role(RoleUtilisateur.CHEF_DEPARTEMENT)
                .statut(true)
                .build();

        chefDepartementRepository.save(chef);
        return UtilisateurDto.fromEntity(chef);
    }

    @Override
    public UtilisateurDto creerSuperviseur(UtilisateurDto dto) {
        if (utilisateurRepository.existsByUsername(dto.getUsername()))
            throw new RuntimeException("Username déjà utilisé");

        String mdp = dto.getEmail() != null ? dto.getEmail() : dto.getUsername();

        Superviseur superviseur = Superviseur.builder()
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .email(dto.getEmail())
                .username(dto.getUsername())
                .motDePasse(passwordEncoder.encode(mdp))
                .telephone(dto.getTelephone())
                .role(RoleUtilisateur.SUPERVISEUR)
                .statut(true)
                .build();

        superviseurRepository.save(superviseur);
        return UtilisateurDto.fromEntity(superviseur);
    }

    @Override
    public List<UtilisateurDto> listerTous() {
        return utilisateurRepository.findAll().stream()
                .map(UtilisateurDto::fromEntity)
                .toList();
    }

    /*@Override
    public UtilisateurDto getById(Long id) {
        return utilisateurRepository.findById(id)
                .map(UtilisateurDto::fromEntity)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
    }
*/
    @Override
    public void desactiver(Long id) {
        Utilisateur u = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        u.setStatut(false);
        utilisateurRepository.save(u);
    }

    @Override
    public void activer(Long id) {
        Utilisateur u = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        u.setStatut(true);
        utilisateurRepository.save(u);
    }

    /*@Override
    public void creerCompte(UtilisateurDto dto) {

        if (dto.getRole() == null) {
            throw new RuntimeException("Rôle obligatoire");
        }

        switch (dto.getRole()) {

            case ETUDIANT:

                Etudiant etudiant = new Etudiant();

                // 🔥 champs hérités
                etudiant.setNom(dto.getNom());
                etudiant.setPrenom(dto.getPrenom());
                etudiant.setEmail(dto.getEmail());
                etudiant.setUsername(dto.getUsername());
                etudiant.setMotDePasse(dto.getMotDePasse());
                etudiant.setRole(dto.getRole());
                etudiant.setStatut(dto.getStatut() != null ? dto.getStatut() : true);

                // 🔥 champs spécifiques
                etudiant.setMatricule(dto.getMatricule());
                etudiant.setNiveau(dto.getNiveau());

                etudiantRepository.save(etudiant);
                break;

            case ENSEIGNANT:

                Enseignant enseignant = new Enseignant();

                enseignant.setNom(dto.getNom());
                enseignant.setPrenom(dto.getPrenom());
                enseignant.setEmail(dto.getEmail());
                enseignant.setUsername(dto.getUsername());
                enseignant.setMotDePasse(dto.getMotDePasse());
                enseignant.setRole(dto.getRole());
                enseignant.setStatut(dto.getStatut() != null ? dto.getStatut() : true);

                enseignant.setGrade(dto.getGrade());
                enseignant.setSpecialite(dto.getSpecialite());

                enseignantRepository.save(enseignant);
                break;

            default:

                Utilisateur user = Utilisateur.builder()
                        .nom(dto.getNom())
                        .prenom(dto.getPrenom())
                        .email(dto.getEmail())
                        .username(dto.getUsername())
                        .motDePasse(dto.getMotDePasse())
                        .role(dto.getRole())
                        .statut(dto.getStatut() != null ? dto.getStatut() : true)
                        .build();

                utilisateurRepository.save(user);
        }
    }*/

    @Override
    public List<UtilisateurDto> listerComptes() {
        return utilisateurRepository.findAll()
                .stream()
                .map(UtilisateurDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public UtilisateurDto obtenirParId(Long id) {
        return utilisateurRepository.findById(id)
                .map(UtilisateurDto::fromEntity)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
    }

    @Override
    public UtilisateurDto getById(Long id) {

        return utilisateurRepository.findById(id)
                .map(UtilisateurDto::fromEntity)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
    }

    @Override
    public void activerDesactiverCompte(Long id) {
        Utilisateur user = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        user.setStatut(!user.getStatut());
    }
}
/*
package com.fst.gestionpresencepointage.service.impl;

import com.fst.gestionpresencepointage.dto.UtilisateurDto;
import com.fst.gestionpresencepointage.models.Utilisateur;
import com.fst.gestionpresencepointage.models.enums.RoleUtilisateur;
import com.fst.gestionpresencepointage.repository.UtilisateurRepository;
import com.fst.gestionpresencepointage.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    // ── Récupérer tous ────────────────────────────────────────
    @Override
    public List<UtilisateurDto> getTousLesUtilisateurs() {
        return utilisateurRepository.findAll()
                .stream()
                .map(UtilisateurDto::fromEntity)
                .collect(Collectors.toList());
    }

    // ── Récupérer par id ──────────────────────────────────────
    @Override
    public UtilisateurDto getById(Long id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id : " + id));
        return UtilisateurDto.fromEntity(utilisateur);
    }

    // ── Récupérer par rôle ────────────────────────────────────
    @Override
    public List<UtilisateurDto> getParRole(RoleUtilisateur role) {
        return utilisateurRepository.findByRole(role)
                .stream()
                .map(UtilisateurDto::fromEntity)
                .collect(Collectors.toList());
    }

    // ── Créer un utilisateur ──────────────────────────────────
    @Override
    public UtilisateurDto creer(UtilisateurDto dto) {

        // Règle 1 : email unique
        if (utilisateurRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Cet email est déjà utilisé.");
        }

        // Règle 2 : username unique
        if (utilisateurRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Ce nom d'utilisateur est déjà pris.");
        }

        // Conversion DTO → Entité
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(dto.getNom());
        utilisateur.setPrenom(dto.getPrenom());
        utilisateur.setEmail(dto.getEmail());
        utilisateur.setUsername(dto.getUsername());
        utilisateur.setTelephone(dto.getTelephone());
        utilisateur.setRole(dto.getRole());
        utilisateur.setStatut(true); // actif par défaut à la création

        return UtilisateurDto.fromEntity(utilisateurRepository.save(utilisateur));
    }

    // ── Modifier un utilisateur ───────────────────────────────
    @Override
    public UtilisateurDto modifier(Long id, UtilisateurDto dto) {
        Utilisateur existant = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé."));

        // On modifie seulement les champs autorisés
        existant.setNom(dto.getNom());
        existant.setPrenom(dto.getPrenom());
        existant.setTelephone(dto.getTelephone());
        // email, motDePasse et role ont leurs propres méthodes

        return UtilisateurDto.fromEntity(utilisateurRepository.save(existant));
    }

    // ── Activer / Désactiver ──────────────────────────────────
    @Override
    public UtilisateurDto changerStatut(Long id, Boolean statut) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé."));
        utilisateur.setStatut(statut);
        return UtilisateurDto.fromEntity(utilisateurRepository.save(utilisateur));
    }

    // ── Rechercher par nom ────────────────────────────────────
    @Override
    public List<UtilisateurDto> rechercherParNom(String motCle) {
        return utilisateurRepository.findByNomContainingIgnoreCase(motCle)
                .stream()
                .map(UtilisateurDto::fromEntity)
                .collect(Collectors.toList());
    }

    // ── Supprimer ─────────────────────────────────────────────
    @Override
    public void supprimer(Long id) {
        if (!utilisateurRepository.existsById(id)) {
            throw new RuntimeException("Utilisateur non trouvé.");
        }
        utilisateurRepository.deleteById(id);
    }
}*/
