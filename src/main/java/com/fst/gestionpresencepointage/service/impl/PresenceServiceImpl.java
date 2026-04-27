package com.fst.gestionpresencepointage.service.impl;

import com.fst.gestionpresencepointage.dto.PresenceDto;
import com.fst.gestionpresencepointage.models.*;
import com.fst.gestionpresencepointage.models.enums.StatutPresence;
import com.fst.gestionpresencepointage.repository.*;
import com.fst.gestionpresencepointage.service.PresenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import com.fst.gestionpresencepointage.dto.TauxPresenceDto;
import com.fst.gestionpresencepointage.models.ModuleCours;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
@Service
@RequiredArgsConstructor
@Transactional
public class PresenceServiceImpl implements PresenceService {

    private final PresenceRepository presenceRepository;
    private final EtudiantRepository etudiantRepository;
    private final SeanceRepository seanceRepository;

    @Override
    public PresenceDto enregistrer(PresenceDto dto) {
        // 1. On cherche l'étudiant avec un message GPS
        Etudiant etudiant = etudiantRepository.findById(dto.getEtudiantId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "ERREUR DATA : L'étudiant ID [" + dto.getEtudiantId() + "] n'existe pas."
                ));

        // 2. On cherche la séance avec un message GPS
        Seance seance = seanceRepository.findById(dto.getSeanceId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "ERREUR DATA : La séance ID [" + dto.getSeanceId() + "] n'existe pas."
                ));

        // Règle métier : doublon
        if (presenceRepository.existsByEtudiantAndSeance(etudiant, seance)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, // 400, car c'est une erreur de logique
                    "ERREUR MÉTIER : Cet étudiant est déjà marqué présent pour cette séance."
            );
        }

        Presence presence = new Presence();
        presence.setEtudiant(etudiant);
        presence.setSeance(seance);
        presence.setStatut(dto.getStatut());
        presence.setRemarque(dto.getRemarque());
        presence.setDatePresence(LocalDate.now());

        return PresenceDto.fromEntity(presenceRepository.save(presence));
    }

    @Override
    public PresenceDto getById(Long id) {
        return PresenceDto.fromEntity(
                presenceRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Présence introuvable."))
        );
    }

    // Applique la même logique pour listerParSeance (la méthode qui a planté tout à l'heure)
    @Override
    public List<PresenceDto> listerParSeance(Long seanceId) {
        Seance seance = seanceRepository.findById(seanceId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "ERREUR : Impossible de lister. La séance ID [" + seanceId + "] est introuvable."
                ));
        return presenceRepository.findBySeance(seance).stream()
                .map(PresenceDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<PresenceDto> listerParEtudiant(Long etudiantId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant introuvable."));
        return presenceRepository.findByEtudiant(etudiant).stream()
                .map(PresenceDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<PresenceDto> listerParSeanceEtStatut(Long seanceId, StatutPresence statut) {
        Seance seance = seanceRepository.findById(seanceId)
                .orElseThrow(() -> new RuntimeException("Séance introuvable."));
        return presenceRepository.findBySeanceAndStatut(seance, statut).stream()
                .map(PresenceDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public PresenceDto modifierStatut(Long id, StatutPresence nouveauStatut) {
        Presence presence = presenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Présence introuvable."));
        presence.setStatut(nouveauStatut);
        return PresenceDto.fromEntity(presenceRepository.save(presence));
    }

    @Override
    public void supprimer(Long id) {
        if (!presenceRepository.existsById(id)) {
            throw new RuntimeException("Présence introuvable.");
        }
        presenceRepository.deleteById(id);
    }

    // ── NOUVELLES MÉTHODES À AJOUTER dans PresenceServiceImpl ────────────────

    // Calcule le taux global : (nombre de PRESENT / total de présences) × 100
// Exemple : 42 présents / 52 total × 100 = 80.76%
    @Override
    public double calculerTauxGlobal(Long etudiantId) {

        // On récupère l'étudiant depuis la base
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant introuvable."));

        // On compte le total de lignes de présence de cet étudiant
        long total = presenceRepository.countByEtudiant(etudiant);

        // Si aucune séance enregistrée → taux = 0 (évite division par zéro)
        if (total == 0) return 0.0;

        // On compte combien de fois il était PRESENT
        long presents = presenceRepository.countByEtudiantAndStatut(
                etudiant, StatutPresence.PRESENT
        );

        // Calcul du pourcentage, arrondi à 1 décimale
        // Math.round(x * 10) / 10.0 = arrondi à 1 décimale
        return Math.round((presents * 100.0 / total) * 10) / 10.0;
    }

    // Calcule le taux de présence pour chaque module de l'étudiant
// Retourne une Map : nom du module → taux
// Exemple : { "Algorithmique" → 92.0, "Réseaux" → 70.0 }
    @Override
    public Map<String, Double> calculerTauxParModule(Long etudiantId) {

        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant introuvable."));

        // On récupère toutes les présences de cet étudiant
        List<Presence> toutesPresences = presenceRepository.findByEtudiant(etudiant);

        // On regroupe par module grâce aux streams Java
        // p.getSeance().getModuleCours() = le module de la séance de cette présence
        Map<ModuleCours, List<Presence>> parModule = toutesPresences.stream()
                .collect(Collectors.groupingBy(p -> p.getSeance().getModuleCours()));

        // Pour chaque module, on calcule le taux
        Map<String, Double> resultats = new LinkedHashMap<>();

        for (Map.Entry<ModuleCours, List<Presence>> entry : parModule.entrySet()) {

            String nomModule = entry.getKey().getIntitule(); // ex: "Algorithmique"
            List<Presence> presencesModule = entry.getValue();

            long total = presencesModule.size(); // total des séances de ce module
            long presents = presencesModule.stream()
                    .filter(p -> p.getStatut() == StatutPresence.PRESENT)
                    .count(); // combien fois PRESENT dans ce module

            double taux = total == 0 ? 0.0 :
                    Math.round((presents * 100.0 / total) * 10) / 10.0;

            resultats.put(nomModule, taux);
        }

        return resultats;
    }

    // Compte les absences non justifiées (statut = ABSENT, pas JUSTIFIE)
    @Override
    public long compterAbsencesNonJustifiees(Long etudiantId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant introuvable."));
        return presenceRepository.countByEtudiantAndStatut(
                etudiant, StatutPresence.ABSENT
        );
    }

    // Vérifie si l'étudiant est sous le seuil (ex: seuil = 75.0)
// Retourne true si son taux < seuil → déclenche l'alerte rouge
    @Override
    public boolean estSousSeuil(Long etudiantId, double seuilPourcentage) {
        return calculerTauxGlobal(etudiantId) < seuilPourcentage;
    }

    // Retourne uniquement les absences (statut ABSENT) d'un étudiant
    @Override
    public List<PresenceDto> listerAbsences(Long etudiantId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("Étudiant introuvable."));
        return presenceRepository
                .findByEtudiantAndStatut(etudiant, StatutPresence.ABSENT)
                .stream()
                .map(PresenceDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<PresenceDto> getAll() {
        return presenceRepository.findAll()
                .stream()
                .map(PresenceDto::fromEntity)
                .collect(Collectors.toList());
    }

    // Retourne tous les étudiants d'un département dont le taux est sous le seuil
// Sert au rapport du Chef de Département
    @Override
    public List<TauxPresenceDto> getEtudiantsSousSeuil(Long departementId, double seuil) {

        // On récupère toutes les présences du département
        List<Presence> toutesPresences =
                presenceRepository.findByDepartementId(departementId);

        // On regroupe par étudiant
        Map<Etudiant, List<Presence>> parEtudiant = toutesPresences.stream()
                .collect(Collectors.groupingBy(Presence::getEtudiant));

        List<TauxPresenceDto> resultat = new ArrayList<>();

        for (Map.Entry<Etudiant, List<Presence>> entry : parEtudiant.entrySet()) {

            Etudiant etudiant = entry.getKey();
            List<Presence> presences = entry.getValue();

            long total = presences.size();
            long presents = presences.stream()
                    .filter(p -> p.getStatut() == StatutPresence.PRESENT)
                    .count();

            double taux = total == 0 ? 0.0 :
                    Math.round((presents * 100.0 / total) * 10) / 10.0;

            // On n'inclut que les étudiants SOUS le seuil
            if (taux < seuil) {
                resultat.add(TauxPresenceDto.builder()
                        .etudiantId(etudiant.getId())
                        .nomComplet(etudiant.getNom() + " " + etudiant.getPrenom())
                        .matricule(etudiant.getMatricule())
                        .tauxGlobal(taux)
                        .totalSeances(total)
                        .totalPresences(presents)
                        .absencesNonJustifiees(
                                presences.stream()
                                        .filter(p -> p.getStatut() == StatutPresence.ABSENT)
                                        .count()
                        )
                        .build());
            }
        }

        // On trie du taux le plus bas au plus haut
        resultat.sort(Comparator.comparingDouble(TauxPresenceDto::getTauxGlobal));
        return resultat;
    }
}