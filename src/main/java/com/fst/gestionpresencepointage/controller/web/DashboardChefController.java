package com.fst.gestionpresencepointage.controller.web;

import com.fst.gestionpresencepointage.dto.SeanceDto;
import com.fst.gestionpresencepointage.repository.EnseignantRepository;
import com.fst.gestionpresencepointage.repository.SalleRepository;
import com.fst.gestionpresencepointage.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/chef")
@RequiredArgsConstructor
public class DashboardChefController {

    private final EtudiantService etudiantService;
    private final PresenceService presenceService;
    private final SeanceService seanceService;
    private final PointageService pointageService;


    // Injecter SeanceService et les repositories nécessaires

    private final ModuleCoursService moduleCoursService;
    private final SalleRepository salleRepository;
    private final EnseignantRepository enseignantRepository;


    // ===== DASHBOARD PRINCIPAL =====
    // GET /chef/dashboard/1 (1 = ID du département)
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

    // GET /chef/departement/1/etudiants
    // ===== LISTE DES ÉTUDIANTS =====
    @GetMapping("/departement/{departementId}/etudiants")
    public String etudiants(
            @PathVariable Long departementId,
            @RequestParam(required = false) String niveau, // filtre optionnel
            Model model) {

        // Liste filtrée ou complète selon le niveau choisi
        var tousEtudiants = etudiantService.listerParDepartement(departementId);

        var etudiantsFiltres = (niveau != null && !niveau.isEmpty())
                ? tousEtudiants.stream()
                .filter(e -> niveau.equals(e.getNiveau()))
                .toList()
                : tousEtudiants;

        // Récupérer les niveaux distincts pour le filtre
        var niveauxDisponibles = tousEtudiants.stream()
                .map(e -> e.getNiveau())
                .filter(n -> n != null && !n.isEmpty())
                .distinct()
                .sorted()
                .toList();

        model.addAttribute("etudiants", etudiantsFiltres);
        model.addAttribute("niveauxDisponibles", niveauxDisponibles);
        model.addAttribute("niveauSelectionne", niveau);
        model.addAttribute("departementId", departementId);
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

    // ===== PAGE GESTION GROUPES =====
// GET /chef/departement/1/groupes?niveau=Info 1
    @GetMapping("/departement/{departementId}/groupes")
    public String pageGroupes(
            @PathVariable Long departementId,
            @RequestParam(required = false) String niveau,
            Model model) {

        // Tous les étudiants du département
        var tousEtudiants = etudiantService.listerParDepartement(departementId);

        // Niveaux disponibles pour le filtre
        var niveauxDisponibles = tousEtudiants.stream()
                .map(e -> e.getNiveau())
                .filter(n -> n != null && !n.isEmpty())
                .distinct().sorted().toList();

        // Liste filtrée par niveau si choisi
        var etudiantsFiltres = (niveau != null && !niveau.isEmpty())
                ? tousEtudiants.stream()
                .filter(e -> niveau.equals(e.getNiveau()))
                .toList()
                : tousEtudiants;

        model.addAttribute("etudiants", etudiantsFiltres);
        model.addAttribute("niveauxDisponibles", niveauxDisponibles);
        model.addAttribute("niveauSelectionne", niveau);
        model.addAttribute("departementId", departementId);

        return "chef/groupes";
    }

    // ===== ASSIGNER G1 =====
// POST /chef/departement/1/groupes/assigner
    @PostMapping("/departement/{departementId}/groupes/assigner")
    public String assignerGroupe(
            @PathVariable Long departementId,
            @RequestParam String groupe,
            @RequestParam String niveau,
            @RequestParam(required = false) List<Long> etudiantIds,
            @RequestParam(defaultValue = "false") boolean modeCorrection,
            RedirectAttributes redirectAttributes) {

        if (etudiantIds == null || etudiantIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("erreur",
                    "Aucun étudiant sélectionné.");
            return "redirect:/chef/departement/" + departementId
                    + "/groupes?niveau=" + niveau;
        }

        // Toujours assigner le groupe choisi aux cochés
        etudiantService.assignerGroupe(etudiantIds, groupe);

        int nbAutres = 0;

        if (!modeCorrection) {
            // Mode initial : les non cochés prennent l'autre groupe automatiquement
            String autreGroupe = groupe.equals("G1") ? "G2" : "G1";
            var tousEtudiantsDuNiveau = etudiantService
                    .listerParDepartementEtNiveau(departementId, niveau);
            var idsNonCoches = tousEtudiantsDuNiveau.stream()
                    .map(e -> e.getId())
                    .filter(id -> !etudiantIds.contains(id))
                    .toList();
            if (!idsNonCoches.isEmpty()) {
                etudiantService.assignerGroupe(idsNonCoches, autreGroupe);
                nbAutres = idsNonCoches.size();
            }

            redirectAttributes.addFlashAttribute("succes",
                    etudiantIds.size() + " étudiant(s) → " + groupe +
                            " | " + nbAutres + " étudiant(s) → " + autreGroupe);
        } else {
            // Mode correction : uniquement les cochés sont modifiés
            redirectAttributes.addFlashAttribute("succes",
                    etudiantIds.size() + " étudiant(s) réassigné(s) → " + groupe
                            + " (autres non modifiés).");
        }

        return "redirect:/chef/departement/" + departementId
                + "/groupes?niveau=" + niveau;
    }
    // ===== RÉINITIALISER GROUPES D'UN NIVEAU =====
// POST /chef/departement/1/groupes/reinitialiser
    @PostMapping("/departement/{departementId}/groupes/reinitialiser")
    public String reinitialiserGroupes(
            @PathVariable Long departementId,
            @RequestParam String niveau,
            RedirectAttributes redirectAttributes) {

        // Récupère tous les étudiants du niveau et remet groupe à null
        var etudiants = etudiantService.listerParDepartementEtNiveau(
                departementId, niveau);
        var ids = etudiants.stream()
                .map(e -> e.getId())
                .toList();
        etudiantService.assignerGroupe(ids, null);

        redirectAttributes.addFlashAttribute("succes",
                "Groupes réinitialisés pour le niveau " + niveau + ".");
        return "redirect:/chef/departement/" + departementId
                + "/groupes?niveau=" + niveau;
    }

    // ===== PAGE PLANNING =====
    @GetMapping("/departement/{departementId}/planning")
    public String pagePlanning(
            @PathVariable Long departementId,
            Model model) {

        model.addAttribute("departementId", departementId);
        model.addAttribute("planning",
                seanceService.getPlanningParDepartement(departementId));
        model.addAttribute("modules",
                moduleCoursService.listerParDepartement(departementId));
        model.addAttribute("salles",
                salleRepository.findAll());
        model.addAttribute("enseignants",
                enseignantRepository.findAll());
        model.addAttribute("jours", java.util.List.of(
                "LUNDI","MARDI","MERCREDI","JEUDI","VENDREDI","SAMEDI"));
        model.addAttribute("typesSeance",
                com.fst.gestionpresencepointage.models.enums.TypeSeance.values());

        return "chef/planning";
    }

    // ===== CRÉER SÉANCE PLANNING =====
    @PostMapping("/departement/{departementId}/planning/ajouter")
    public String ajouterSeancePlanning(
            @PathVariable Long departementId,
            @RequestParam Long moduleId,
            @RequestParam String jourSemaine,
            @RequestParam String heureDebut,
            @RequestParam String heureFin,
            @RequestParam(required = false) Long salleId,
            @RequestParam(required = false) Long enseignantId,
            @RequestParam String typeSeance,
            @RequestParam String dateFinSemestre,
            RedirectAttributes redirectAttributes) {

        try {
            SeanceDto dto = SeanceDto.builder()
                    .moduleId(moduleId)
                    .jourSemaine(jourSemaine)
                    .heureDebut(java.time.LocalTime.parse(heureDebut))
                    .heureFin(java.time.LocalTime.parse(heureFin))
                    .salleId(salleId)
                    .enseignantId(enseignantId)
                    .typeSeance(com.fst.gestionpresencepointage.models.enums
                            .TypeSeance.valueOf(typeSeance))
                    .dateFinSemestre(LocalDate.parse(dateFinSemestre))
                    .build();

            seanceService.creerSeancePlanifiee(dto);
            redirectAttributes.addFlashAttribute("succes",
                    "Séance ajoutée au planning avec succès.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        }

        return "redirect:/chef/departement/" + departementId + "/planning";
    }

    // ===== SUPPRIMER SÉANCE PLANNING =====
    @PostMapping("/departement/{departementId}/planning/{seanceId}/supprimer")
    public String supprimerSeancePlanning(
            @PathVariable Long departementId,
            @PathVariable Long seanceId,
            RedirectAttributes redirectAttributes) {

        try {
            seanceService.supprimer(seanceId);
            redirectAttributes.addFlashAttribute("succes", "Séance supprimée.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/chef/departement/" + departementId + "/planning";
    }
}