package com.fst.gestionpresencepointage.service.impl;

import com.fst.gestionpresencepointage.dto.SeanceDto;
import com.fst.gestionpresencepointage.models.*;
import com.fst.gestionpresencepointage.models.enums.TypeSeance;
import com.fst.gestionpresencepointage.repository.*;
import com.fst.gestionpresencepointage.service.SeanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SeanceServiceImpl implements SeanceService {

    private final SeanceRepository seanceRepository;
    private final EnseignantRepository enseignantRepository;
    private final ModuleCoursRepository moduleCoursRepository;
    private final SalleRepository salleRepository;

    @Override
    public SeanceDto creer(SeanceDto dto) {
        Seance seance = new Seance();
        seance.setDateSeance(dto.getDateSeance());
        seance.setHeureDebut(dto.getHeureDebut());
        seance.setHeureFin(dto.getHeureFin());
        seance.setTypeSeance(dto.getTypeSeance());

        // Enseignant obligatoire
        Enseignant enseignant = enseignantRepository.findById(dto.getEnseignantId())
                .orElseThrow(() -> new RuntimeException("Enseignant introuvable."));
        seance.setEnseignant(enseignant);

        // Module obligatoire
        ModuleCours module = moduleCoursRepository.findById(dto.getModuleId())
                .orElseThrow(() -> new RuntimeException("Module introuvable."));
        seance.setModuleCours(module);

        // Salle optionnelle
        if (dto.getSalleId() != null) {
            Salle salle = salleRepository.findById(dto.getSalleId())
                    .orElseThrow(() -> new RuntimeException("Salle introuvable."));
            seance.setSalle(salle);
        }

        return SeanceDto.fromEntity(seanceRepository.save(seance));
    }

    @Override
    public SeanceDto getById(Long id) {
        return SeanceDto.fromEntity(
                seanceRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Séance introuvable."))
        );
    }

    @Override
    public List<SeanceDto> listerTous() {
        return seanceRepository.findAll().stream()
                .map(SeanceDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<SeanceDto> listerParEnseignant(Long enseignantId) {
        Enseignant enseignant = enseignantRepository.findById(enseignantId)
                .orElseThrow(() -> new RuntimeException("Enseignant introuvable."));
        return seanceRepository.findByEnseignant(enseignant).stream()
                .map(SeanceDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<SeanceDto> listerParModule(Long moduleId) {
        ModuleCours module = moduleCoursRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module introuvable."));
        return seanceRepository.findByModuleCours(module).stream()
                .map(SeanceDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<SeanceDto> listerParDate(LocalDate date) {
        return seanceRepository.findByDateSeance(date).stream()
                .map(SeanceDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<SeanceDto> listerParPeriode(LocalDate debut, LocalDate fin) {
        return seanceRepository.findByDateSeanceBetween(debut, fin).stream()
                .map(SeanceDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<SeanceDto> listerParType(TypeSeance type) {
        return seanceRepository.findByTypeSeance(type).stream()
                .map(SeanceDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public SeanceDto modifier(Long id, SeanceDto dto) {
        Seance existante = seanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Séance introuvable."));
        existante.setDateSeance(dto.getDateSeance());
        existante.setHeureDebut(dto.getHeureDebut());
        existante.setHeureFin(dto.getHeureFin());
        existante.setTypeSeance(dto.getTypeSeance());
        return SeanceDto.fromEntity(seanceRepository.save(existante));
    }

    @Override
    public void supprimer(Long id) {
        if (!seanceRepository.existsById(id)) {
            throw new RuntimeException("Séance introuvable.");
        }
        seanceRepository.deleteById(id);
    }

    // À ajouter à la fin de SeanceServiceImpl.java

    @Override
    public List<SeanceDto> getSeancesAujourdhui(Long departementId) {
        // LocalDate.now() = la date d'aujourd'hui automatiquement
        LocalDate aujourdhui = LocalDate.now();
        return seanceRepository
                .findByDateAndDepartement(aujourdhui, departementId)
                .stream()
                .map(SeanceDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<SeanceDto> getSeancesDeLaSemaine(Long departementId) {
        // On calcule le lundi et vendredi de la semaine actuelle
        LocalDate aujourdhui = LocalDate.now();

        // DayOfWeek.MONDAY = lundi, on recule jusqu'au lundi
        LocalDate lundi = aujourdhui.with(
                java.time.DayOfWeek.MONDAY
        );
        // Vendredi = lundi + 4 jours
        LocalDate vendredi = lundi.plusDays(4);

        return seanceRepository
                .findByPeriodeAndDepartement(lundi, vendredi, departementId)
                .stream()
                .map(SeanceDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<SeanceDto> getPlanningParDepartement(Long departementId) {
        return seanceRepository.findPlanningByDepartement(departementId)
                .stream().map(SeanceDto::fromEntity).toList();
    }

    @Override
    public List<SeanceDto> getPlanningParEnseignant(Long enseignantId) {
        return seanceRepository
                .findByEnseignantIdAndRecurrenteTrue(enseignantId)
                .stream().map(SeanceDto::fromEntity).toList();
    }

    @Override
    public SeanceDto creerSeancePlanifiee(SeanceDto dto) {
        // Calcul automatique heure début à partir du jour
        Seance seance = new Seance();
        seance.setJourSemaine(dto.getJourSemaine());
        seance.setHeureDebut(dto.getHeureDebut());
        seance.setHeureFin(dto.getHeureFin());
        seance.setTypeSeance(dto.getTypeSeance());
        seance.setRecurrente(true);
        seance.setDateFinSemestre(dto.getDateFinSemestre());

        // Date de la prochaine occurrence du jour choisi
        seance.setDateSeance(
                prochainJour(dto.getJourSemaine()));

        ModuleCours module = moduleCoursRepository.findById(dto.getModuleId())
                .orElseThrow(() -> new RuntimeException("Module introuvable."));
        seance.setModuleCours(module);

        // Enseignant = enseignant responsable du module
        if (module.getEnseignantResponsable() != null) {
            seance.setEnseignant(module.getEnseignantResponsable());
        } else {
            // Sinon utiliser l'enseignantId fourni
            if (dto.getEnseignantId() != null) {
                enseignantRepository.findById(dto.getEnseignantId())
                        .ifPresent(seance::setEnseignant);
            }
        }

        if (dto.getSalleId() != null) {
            salleRepository.findById(dto.getSalleId())
                    .ifPresent(seance::setSalle);
        }

        return SeanceDto.fromEntity(seanceRepository.save(seance));
    }

    // Calcule la date de la prochaine occurrence d'un jour
    private LocalDate prochainJour(String jourSemaine) {
        java.time.DayOfWeek dow = switch (jourSemaine.toUpperCase()) {
            case "LUNDI"    -> java.time.DayOfWeek.MONDAY;
            case "MARDI"    -> java.time.DayOfWeek.TUESDAY;
            case "MERCREDI" -> java.time.DayOfWeek.WEDNESDAY;
            case "JEUDI"    -> java.time.DayOfWeek.THURSDAY;
            case "VENDREDI" -> java.time.DayOfWeek.FRIDAY;
            case "SAMEDI"   -> java.time.DayOfWeek.SATURDAY;
            default -> java.time.DayOfWeek.MONDAY;
        };
        LocalDate today = LocalDate.now();
        // Si aujourd'hui est le bon jour → aujourd'hui, sinon prochain
        if (today.getDayOfWeek() == dow) return today;
        return today.with(java.time.temporal.TemporalAdjusters.next(dow));
    }
}