package com.fst.gestionpresencepointage.controller.web;

import com.fst.gestionpresencepointage.service.EtudiantService;
import com.fst.gestionpresencepointage.service.PresenceService;
import com.fst.gestionpresencepointage.service.SeanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/etudiant")
@RequiredArgsConstructor
public class DashboardEtudiantController {

    private final EtudiantService etudiantService;
    private final PresenceService presenceService;
    private final SeanceService seanceService;

    // ===== DASHBOARD PRINCIPAL =====
    // GET /etudiant/dashboard/1
    // Le "1" = l'ID de l'étudiant connecté
    // (plus tard remplacé par l'utilisateur de la session)
    @GetMapping("/dashboard/{etudiantId}")
    public String dashboard(@PathVariable Long etudiantId, Model model) {

        // Informations de l'étudiant
        // model.addAttribute("nom", valeur) → dans le HTML : ${nom}
        model.addAttribute("etudiant",
                etudiantService.rechercherParId(etudiantId));

        // Taux global : ex 81.5
        model.addAttribute("tauxGlobal",
                presenceService.calculerTauxGlobal(etudiantId));

        // Vrai/faux : est-ce qu'il est sous le seuil de 75% ?
        model.addAttribute("sousSeuil",
                presenceService.estSousSeuil(etudiantId, 75.0));

        // Nombre d'absences non justifiées
        model.addAttribute("absencesNonJustifiees",
                presenceService.compterAbsencesNonJustifiees(etudiantId));

        // Taux par module : { "Algorithmique" → 92.0, "Réseaux" → 70.0 }
        model.addAttribute("tauxParModule",
                presenceService.calculerTauxParModule(etudiantId));

        // Séances du département aujourd'hui
        // On récupère le département depuis l'étudiant
        Long departementId = etudiantService
                .rechercherParId(etudiantId).getDepartementId();

        model.addAttribute("seancesAujourdhui",
                seanceService.getSeancesAujourdhui(departementId));

        // Retourne le fichier templates/etudiant/dashboard.html
        return "etudiants/dashboard";
    }

    // ===== MES PRÉSENCES =====
    // GET /etudiant/1/presences
    @GetMapping("/{etudiantId}/presences")
    public String presences(@PathVariable Long etudiantId, Model model) {

        model.addAttribute("etudiant",
                etudiantService.rechercherParId(etudiantId));

        // Liste complète de toutes les présences
        model.addAttribute("presences",
                presenceService.listerParEtudiant(etudiantId));

        // Uniquement les absences
        model.addAttribute("absences",
                presenceService.listerAbsences(etudiantId));

        model.addAttribute("tauxGlobal",
                presenceService.calculerTauxGlobal(etudiantId));

        return "etudiants/presences";
    }

    // ===== EMPLOI DU TEMPS =====
    // GET /etudiant/1/emploi-du-temps
    @GetMapping("/{etudiantId}/emploi-du-temps")
    public String emploiDuTemps(@PathVariable Long etudiantId, Model model) {

        model.addAttribute("etudiant",
                etudiantService.rechercherParId(etudiantId));

        Long departementId = etudiantService
                .rechercherParId(etudiantId).getDepartementId();

        // Toutes les séances de la semaine
        model.addAttribute("seancesSemaine",
                seanceService.getSeancesDeLaSemaine(departementId));

        return "etudiants/emploi-du-temps";
    }

    // ===== STATISTIQUES =====
    // GET /etudiant/1/statistiques
    @GetMapping("/{etudiantId}/statistiques")
    public String statistiques(@PathVariable Long etudiantId, Model model) {

        model.addAttribute("etudiant",
                etudiantService.rechercherParId(etudiantId));

        model.addAttribute("tauxGlobal",
                presenceService.calculerTauxGlobal(etudiantId));

        model.addAttribute("tauxParModule",
                presenceService.calculerTauxParModule(etudiantId));

        model.addAttribute("absencesNonJustifiees",
                presenceService.compterAbsencesNonJustifiees(etudiantId));

        model.addAttribute("presences",
                presenceService.listerParEtudiant(etudiantId));

        return "etudiants/statistiques";
    }
}
