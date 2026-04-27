package com.fst.gestionpresencepointage.controller.web;

import com.fst.gestionpresencepointage.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
public class ControllerWeb {
    @Controller
// ↑ PAS @RestController — retourne des pages HTML et non du JSON
    @RequiredArgsConstructor
    public class WebController {

        private final EtudiantService etudiantService;
        private final EnseignantService enseignantService;
        private final DepartementService departementService;
        private final SeanceService seanceService;
        private final PresenceService presenceService;

        // ── Page d'accueil ────────────────────────────────────────
        @GetMapping("/")
        public String accueil() {
            return "index";
            // ↑ retourne le fichier templates/index.html
        }

        // ── Dashboard ─────────────────────────────────────────────
        @GetMapping("/dashboard")
        public String dashboard(Model model) {
            // Model = un sac qu'on remplit de données
            // Thymeleaf les récupère dans le HTML avec th:text="${...}"
            model.addAttribute("totalEtudiants",
                    etudiantService.listerTous().size());
            model.addAttribute("totalEnseignants",
                    enseignantService.listerTous().size());
            model.addAttribute("totalDepartements",
                    departementService.listerTous().size());
            model.addAttribute("seancesAujourdhui",
                    seanceService.listerParDate(java.time.LocalDate.now()));
            return "dashboard";
        }

        // ── Étudiants ─────────────────────────────────────────────
        @GetMapping("/etudiants")
        public String listeEtudiants(Model model) {
            model.addAttribute("etudiants", etudiantService.listerTous());
            model.addAttribute("departements", departementService.listerTous());
            return "etudiants/liste";
        }

        @GetMapping("/etudiants/nouveau")
        public String formulaireEtudiant(Model model) {
            model.addAttribute("departements", departementService.listerTous());
            return "etudiants/formulaire";
        }

        // ── Enseignants ───────────────────────────────────────────
        @GetMapping("/enseignants")
        public String listeEnseignants(Model model) {
            model.addAttribute("enseignants", enseignantService.listerTous());
            return "enseignants/liste";
        }

        @GetMapping("/enseignants/nouveau")
        public String formulaireEnseignant(Model model) {
            model.addAttribute("departements", departementService.listerTous());
            return "enseignants/formulaire";
        }

        // ── Séances ───────────────────────────────────────────────
        @GetMapping("/seances")
        public String listeSeances(Model model) {
            model.addAttribute("seances", seanceService.listerTous());
            return "seances/liste";
        }

        // ── Présences ─────────────────────────────────────────────
        @GetMapping("/presences/seance/{seanceId}")
        public String presencesParSeance(@PathVariable Long seanceId, Model model) {
            model.addAttribute("presences",
                    presenceService.listerParSeance(seanceId));
            model.addAttribute("seanceId", seanceId);
            return "presences/liste";
        }
    }
}