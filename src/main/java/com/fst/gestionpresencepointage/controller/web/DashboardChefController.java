package com.fst.gestionpresencepointage.controller.web;

import com.fst.gestionpresencepointage.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/chef")
@RequiredArgsConstructor
public class DashboardChefController {

    private final EtudiantService etudiantService;
    private final PresenceService presenceService;
    private final SeanceService seanceService;
    private final PointageService pointageService;

    // ===== DASHBOARD PRINCIPAL =====
    // GET /chef/dashboard/1   (1 = ID du département)
    @GetMapping("/dashboard/{departementId}")
    public String dashboard(@PathVariable Long departementId, Model model) {

        // Nombre total d'étudiants du département
        model.addAttribute("totalEtudiants",
                etudiantService.listerParDepartement(departementId).size());

        // Pointages EN_ATTENTE de confirmation
        model.addAttribute("pointagesEnAttente",
                pointageService.listerEnAttenteParDepartement(departementId));

        // Nombre de pointages en attente (pour le badge rouge)
        model.addAttribute("nbPointagesEnAttente",
                pointageService.listerEnAttenteParDepartement(departementId).size());

        // Étudiants sous le seuil de 75%
        model.addAttribute("etudiantsSousSeuil",
                presenceService.getEtudiantsSousSeuil(departementId, 75.0));

        // Nombre d'étudiants sous le seuil (pour le badge)
        model.addAttribute("nbSousSeuil",
                presenceService.getEtudiantsSousSeuil(departementId, 75.0).size());

        // Séances du jour
        model.addAttribute("seancesAujourdhui",
                seanceService.getSeancesAujourdhui(departementId));

        return "chef/dashboard";
    }

    // ===== CONFIRMER UN POINTAGE =====
    // POST /chef/pointages/1/valider?chefId=5
    // Soumis depuis le formulaire HTML du dashboard
    @PostMapping("/pointages/{pointageId}/valider")
    public String validerPointage(
            @PathVariable Long pointageId,
            @RequestParam Long chefId,
            @RequestParam Long departementId) {

        pointageService.valider(pointageId, chefId);

        // Après validation → retour au dashboard
        return "redirect:/chef/dashboard/" + departementId;
    }

    // ===== REJETER UN POINTAGE =====
    // POST /chef/pointages/1/rejeter?chefId=5
    @PostMapping("/pointages/{pointageId}/rejeter")
    public String rejeterPointage(
            @PathVariable Long pointageId,
            @RequestParam Long chefId,
            @RequestParam Long departementId) {

        pointageService.rejeter(pointageId, chefId);
        return "redirect:/chef/dashboard/" + departementId;
    }

    // ===== LISTE DES ÉTUDIANTS =====
    // GET /chef/departement/1/etudiants
    @GetMapping("/departement/{departementId}/etudiants")
    public String etudiants(@PathVariable Long departementId, Model model) {

        model.addAttribute("etudiants",
                etudiantService.listerParDepartement(departementId));

        model.addAttribute("etudiantsSousSeuil",
                presenceService.getEtudiantsSousSeuil(departementId, 75.0));

        return "chef/etudiants";
    }

    // ===== STATISTIQUES GLOBALES =====
    // GET /chef/departement/1/statistiques
    @GetMapping("/departement/{departementId}/statistiques")
    public String statistiques(@PathVariable Long departementId, Model model) {

        model.addAttribute("etudiantsSousSeuil",
                presenceService.getEtudiantsSousSeuil(departementId, 75.0));

        model.addAttribute("seancesSemaine",
                seanceService.getSeancesDeLaSemaine(departementId));

        model.addAttribute("pointagesValides",
                pointageService.listerParStatut(
                        com.fst.gestionpresencepointage.models.enums.StatutPointage.VALIDE
                )
        );

        return "chef/statistiques";
    }
}