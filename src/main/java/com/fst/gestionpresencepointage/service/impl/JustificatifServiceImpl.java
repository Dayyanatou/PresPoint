package com.fst.gestionpresencepointage.service.impl;

import com.fst.gestionpresencepointage.dto.JustificatifDto;
import com.fst.gestionpresencepointage.models.*;
import com.fst.gestionpresencepointage.models.enums.StatutJustificatif;
import com.fst.gestionpresencepointage.models.enums.StatutPresence;
import com.fst.gestionpresencepointage.repository.*;
import com.fst.gestionpresencepointage.service.JustificatifService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class JustificatifServiceImpl implements JustificatifService {

    private final JustificatifRepository justificatifRepository;
    private final EtudiantRepository etudiantRepository;
    private final PresenceRepository presenceRepository;
    private final ChefDepartementRepository chefDepartementRepository;

    // Lit le chemin depuis application.properties
    // "uploads/justificatifs" par défaut si non défini
    @Value("${app.upload.dir:uploads/justificatifs}")
    private String uploadDir;

    @Override
    public JustificatifDto soumettre(Long etudiantId,
                                     Long presenceId,
                                     String motif,
                                     String description,
                                     MultipartFile fichier) {

        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant introuvable."));

        Presence presence = presenceRepository.findById(presenceId)
                .orElseThrow(() -> new RuntimeException("Présence introuvable."));

        // Vérifier que la présence est bien ABSENT
        if (presence.getStatut() != StatutPresence.ABSENT) {
            throw new RuntimeException(
                    "Vous ne pouvez soumettre un justificatif que pour une absence.");
        }

        // Sauvegarder le fichier sur le serveur
        String cheminFichier = null;
        String nomOriginal = null;

        if (fichier != null && !fichier.isEmpty()) {
            try {
                // Créer le dossier s'il n'existe pas
                Path dossier = Paths.get(uploadDir);
                Files.createDirectories(dossier);

                // Générer un nom unique pour éviter les conflits
                // UUID = identifiant aléatoire unique
                // Ex : "a3f2b1c4_certificat_medical.pdf"
                nomOriginal = fichier.getOriginalFilename();
                String nomUnique = UUID.randomUUID().toString()
                        .substring(0, 8) + "_" + nomOriginal;

                // Copier le fichier dans le dossier
                Path destination = dossier.resolve(nomUnique);
                Files.copy(fichier.getInputStream(),
                        destination,
                        StandardCopyOption.REPLACE_EXISTING);

                cheminFichier = uploadDir + "/" + nomUnique;

            } catch (IOException e) {
                throw new RuntimeException(
                        "Erreur lors de la sauvegarde du fichier : " + e.getMessage());
            }
        }

        // Créer le justificatif en base
        Justificatif justificatif = Justificatif.builder()
                .etudiant(etudiant)
                .presence(presence)
                .motif(motif)
                .description(description)
                .cheminFichier(cheminFichier)
                .nomFichierOriginal(nomOriginal)
                .dateSoumission(LocalDateTime.now())
                .statut(StatutJustificatif.EN_ATTENTE)
                .build();

        return JustificatifDto.fromEntity(
                justificatifRepository.save(justificatif));
    }

    @Override
    public JustificatifDto accepter(Long justificatifId,
                                    Long chefId,
                                    String commentaire) {

        Justificatif j = justificatifRepository.findById(justificatifId)
                .orElseThrow(() -> new RuntimeException("Justificatif introuvable."));

        ChefDepartement chef = chefDepartementRepository.findById(chefId)
                .orElseThrow(() -> new RuntimeException("Chef introuvable."));

        // Mettre à jour le justificatif
        j.setStatut(StatutJustificatif.ACCEPTE);
        j.setChefDepartement(chef);
        j.setCommentaireChef(commentaire);

        // Mettre à jour automatiquement la présence → JUSTIFIE
        // C'est la logique métier : accepter = absence devient justifiée
        Presence presence = j.getPresence();
        presence.setStatut(StatutPresence.JUSTIFIE);
        presenceRepository.save(presence);

        return JustificatifDto.fromEntity(justificatifRepository.save(j));
    }

    @Override
    public JustificatifDto refuser(Long justificatifId,
                                   Long chefId,
                                   String commentaire) {

        Justificatif j = justificatifRepository.findById(justificatifId)
                .orElseThrow(() -> new RuntimeException("Justificatif introuvable."));

        ChefDepartement chef = chefDepartementRepository.findById(chefId)
                .orElseThrow(() -> new RuntimeException("Chef introuvable."));

        j.setStatut(StatutJustificatif.REFUSE);
        j.setChefDepartement(chef);
        j.setCommentaireChef(commentaire);

        // La présence reste ABSENT si refusé
        return JustificatifDto.fromEntity(justificatifRepository.save(j));
    }

    @Override
    public List<JustificatifDto> listerParEtudiant(Long etudiantId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant introuvable."));
        return justificatifRepository.findByEtudiant(etudiant)
                .stream()
                .map(JustificatifDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<JustificatifDto> listerEnAttenteParDepartement(Long departementId) {
        return justificatifRepository
                .findByDepartementAndStatut(
                        departementId, StatutJustificatif.EN_ATTENTE)
                .stream()
                .map(JustificatifDto::fromEntity)
                .collect(Collectors.toList());
    }
}