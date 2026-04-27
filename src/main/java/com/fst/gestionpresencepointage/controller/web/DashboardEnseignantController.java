package com.fst.gestionpresencepointage.controller.web;

import com.fst.gestionpresencepointage.dto.*;
import com.fst.gestionpresencepointage.models.enums.StatutPresence;
import com.fst.gestionpresencepointage.repository.*;
import com.fst.gestionpresencepointage.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/enseignant")
@RequiredArgsConstructor
public class DashboardEnseignantController {

    private final EnseignantRepository enseignantRepository;
    private final SeanceService seanceService;
    private final PointageService pointageService;
    private final PresenceService presenceService;

    // Dashboard principal
    @GetMapping("/dashboard/{enseignantId}")
    public String dashboard(@PathVariable Long enseignantId, Model model) {
        var enseignant = enseignantRepository.findById(enseignantId)
                .orElseThrow(() -> new RuntimeException("Enseignant introuvable"));

        List<SeanceDto> seancesAujourdhui = seanceService.getSeancesAujourdhui(
                enseignant.getDepartement() != null ? enseignant.getDepartement().getId() : 1L);

        // Filtrer seulement les séances de cet enseignant
        List<SeanceDto> mesSeancesAujourdhui = seancesAujourdhui.stream()
                .filter(s -> enseignantId.equals(s.getEnseignantId()))
                .toList();

        List<SeanceDto> seancesSemaine = seanceService.getSeancesDeLaSemaine(
                enseignant.getDepartement() != null ? enseignant.getDepartement().getId() : 1L);

        List<SeanceDto> mesSeancesSemaine = seancesSemaine.stream()
                .filter(s -> enseignantId.equals(s.getEnseignantId()))
                .toList();

        model.addAttribute("enseignant", EnseignantDto.fromEntity(enseignant));
        model.addAttribute("seancesAujourdhui", mesSeancesAujourdhui);
        model.addAttribute("seancesSemaine", mesSeancesSemaine);
        model.addAttribute("nbSeancesAujourdhui", mesSeancesAujourdhui.size());
        model.addAttribute("nbSeancesSemaine", mesSeancesSemaine.size());
        return "enseignant/dashboard";
    }

    // Page faire l'appel (lister les étudiants d'une séance)
    @GetMapping("/seance/{seanceId}/appel/{enseignantId}")
    public String appel(@PathVariable Long seanceId,
                        @PathVariable Long enseignantId,
                        Model model) {
        var enseignant = enseignantRepository.findById(enseignantId)
                .orElseThrow(() -> new RuntimeException("Enseignant introuvable"));

        List<PresenceDto> presences = presenceService.listerParSeance(seanceId);

        model.addAttribute("enseignant", EnseignantDto.fromEntity(enseignant));
        model.addAttribute("presences", presences);
        model.addAttribute("seanceId", seanceId);
        return "enseignant/appel";
    }

    // Soumettre l'appel (marquer présences)
    @PostMapping("/seance/{seanceId}/appel/{enseignantId}")
    public String soumettreAppel(@PathVariable Long seanceId,
                                 @PathVariable Long enseignantId,
                                 @RequestParam java.util.Map<String, String> params) {
        // params contient : presenceId_X = PRESENT/ABSENT
        params.forEach((key, valeur) -> {
            if (key.startsWith("statut_")) {
                Long presenceId = Long.parseLong(key.replace("statut_", ""));
                StatutPresence statut = StatutPresence.valueOf(valeur);
                presenceService.modifierStatut(presenceId, statut);
            }
        });
        return "redirect:/enseignant/dashboard/" + enseignantId;
    }

    // Page pointage d'une séance
    @GetMapping("/seance/{seanceId}/pointage/{enseignantId}")
    public String pointage(@PathVariable Long seanceId,
                           @PathVariable Long enseignantId,
                           Model model) {
        var enseignant = enseignantRepository.findById(enseignantId)
                .orElseThrow(() -> new RuntimeException("Enseignant introuvable"));

        model.addAttribute("enseignant", EnseignantDto.fromEntity(enseignant));
        model.addAttribute("seanceId", seanceId);
        return "enseignant/pointage";
    }

    // Soumettre le pointage
    @PostMapping("/seance/{seanceId}/pointage/{enseignantId}")
    public String soumettrePointage(@PathVariable Long seanceId,
                                    @PathVariable Long enseignantId,
                                    @RequestParam String heureDebut,
                                    @RequestParam String heureFin) {
        PointageDto dto = PointageDto.builder()
                .seanceId(seanceId)
                .enseignantId(enseignantId)
                .date(LocalDate.now())
                .heureDebut(java.time.LocalTime.parse(heureDebut))
                .heureFin(java.time.LocalTime.parse(heureFin))
                .build();
        pointageService.soumettre(dto);
        return "redirect:/enseignant/dashboard/" + enseignantId;
    }

    // Mes séances (toutes)
    @GetMapping("/seances/{enseignantId}")
    public String mesSeances(@PathVariable Long enseignantId, Model model) {
        var enseignant = enseignantRepository.findById(enseignantId)
                .orElseThrow(() -> new RuntimeException("Enseignant introuvable"));

        List<SeanceDto> toutesSeances = seanceService.getSeancesDeLaSemaine(
                enseignant.getDepartement() != null ? enseignant.getDepartement().getId() : 1L);

        List<SeanceDto> mesSeances = toutesSeances.stream()
                .filter(s -> enseignantId.equals(s.getEnseignantId()))
                .toList();

        model.addAttribute("enseignant", EnseignantDto.fromEntity(enseignant));
        model.addAttribute("seances", mesSeances);
        return "enseignant/seances";
    }
}