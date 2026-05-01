package com.fst.gestionpresencepointage.controller.web;

import com.fst.gestionpresencepointage.models.Departement;
import com.fst.gestionpresencepointage.models.ModuleCours;
import com.fst.gestionpresencepointage.models.enums.Semestre;
import com.fst.gestionpresencepointage.repository.DepartementRepository;
import com.fst.gestionpresencepointage.repository.EnseignantRepository;
import com.fst.gestionpresencepointage.service.ModuleCoursService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/chef")
@RequiredArgsConstructor
public class ModuleCoursController {

    private final ModuleCoursService moduleCoursService;
    private final DepartementRepository departementRepository;
    private final EnseignantRepository enseignantRepository;

    // ===== LISTE MODULES =====
    @GetMapping("/departement/{departementId}/modules")
    public String listeModules(
            @PathVariable Long departementId,
            @RequestParam(required = false) String niveau,
            Model model) {

        var tousModules = moduleCoursService.listerParDepartement(departementId);

        var modulesFiltres = (niveau != null && !niveau.isEmpty())
                ? tousModules.stream()
                .filter(m -> niveau.equals(m.getNiveau()))
                .toList()
                : tousModules;

        var niveauxDisponibles = tousModules.stream()
                .map(ModuleCours::getNiveau)
                .filter(n -> n != null && !n.isEmpty())
                .distinct().sorted().toList();

        model.addAttribute("modules", modulesFiltres);
        model.addAttribute("niveauxDisponibles", niveauxDisponibles);
        model.addAttribute("niveauSelectionne", niveau);
        model.addAttribute("departementId", departementId);
        return "chef/modules";
    }

    // ===== FORMULAIRE CRÉER =====
    @GetMapping("/departement/{departementId}/modules/nouveau")
    public String formNouveauModule(
            @PathVariable Long departementId,
            Model model) {

        model.addAttribute("departementId", departementId);
        model.addAttribute("enseignants",
                enseignantRepository.findAll());
        model.addAttribute("semestres", Semestre.values());
        model.addAttribute("departement",
                departementRepository.findById(departementId).orElseThrow());
        // Niveaux disponibles basés sur les étudiants existants
        model.addAttribute("niveaux", java.util.List.of(
                "Info 1","Info 2","Info 3",
                "Info 4 - Sécurité","Info 4 - Génie Logiciel",
                "Télécom 1","Télécom 2","Télécom 3","Télécom 4",
                "Master Pro","Master Recherche"
        ));
        return "chef/module-form";
    }

    // ===== TRAITEMENT CRÉER =====
    @PostMapping("/departement/{departementId}/modules/nouveau")
    public String creerModule(
            @PathVariable Long departementId,
            @RequestParam String code,
            @RequestParam String intitule,
            @RequestParam Integer volumeHoraire,
            @RequestParam Semestre semestre,
            @RequestParam String niveau,
            @RequestParam(required = false) String groupe,
            @RequestParam(required = false) Long enseignantId,
            RedirectAttributes redirectAttributes) {

        try {
            Departement dep = departementRepository.findById(departementId)
                    .orElseThrow(() -> new RuntimeException("Département introuvable."));

            ModuleCours module = ModuleCours.builder()
                    .code(code)
                    .intitule(intitule)
                    .volumeHoraire(volumeHoraire)
                    .semestre(semestre)
                    .niveau(niveau)
                    .groupe(groupe)
                    .departement(dep)
                    .build();

            // Assigner l'enseignant si fourni
            if (enseignantId != null) {
                enseignantRepository.findById(enseignantId)
                        .ifPresent(module::setEnseignantResponsable);
            }

            moduleCoursService.enregistrer(module);
            redirectAttributes.addFlashAttribute("succes",
                    "Module « " + intitule + " » créé avec succès.");

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        }

        return "redirect:/chef/departement/" + departementId + "/modules";
    }

    // ===== FORMULAIRE MODIFIER =====
    @GetMapping("/departement/{departementId}/modules/{moduleId}/modifier")
    public String formModifierModule(
            @PathVariable Long departementId,
            @PathVariable Long moduleId,
            Model model) {

        ModuleCours module = moduleCoursService.rechercherParId(moduleId)
                .orElseThrow(() -> new RuntimeException("Module introuvable."));

        model.addAttribute("module", module);
        model.addAttribute("departementId", departementId);
        model.addAttribute("enseignants", enseignantRepository.findAll());
        model.addAttribute("semestres", Semestre.values());
        model.addAttribute("niveaux", java.util.List.of(
                "Info 1","Info 2","Info 3",
                "Info 4 - Sécurité","Info 4 - Génie Logiciel",
                "Télécom 1","Télécom 2","Télécom 3","Télécom 4",
                "Master Pro","Master Recherche"
        ));
        return "chef/module-form";
    }

    // ===== TRAITEMENT MODIFIER =====
    @PostMapping("/departement/{departementId}/modules/{moduleId}/modifier")
    public String modifierModule(
            @PathVariable Long departementId,
            @PathVariable Long moduleId,
            @RequestParam String code,
            @RequestParam String intitule,
            @RequestParam Integer volumeHoraire,
            @RequestParam Semestre semestre,
            @RequestParam String niveau,
            @RequestParam(required = false) String groupe,
            @RequestParam(required = false) Long enseignantId,
            RedirectAttributes redirectAttributes) {

        try {
            Departement dep = departementRepository.findById(departementId)
                    .orElseThrow();

            ModuleCours dto = ModuleCours.builder()
                    .code(code).intitule(intitule)
                    .volumeHoraire(volumeHoraire)
                    .semestre(semestre).niveau(niveau)
                    .groupe(groupe).departement(dep).build();

            if (enseignantId != null) {
                enseignantRepository.findById(enseignantId)
                        .ifPresent(dto::setEnseignantResponsable);
            }

            moduleCoursService.modifier(moduleId, dto);
            redirectAttributes.addFlashAttribute("succes",
                    "Module modifié avec succès.");

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        }

        return "redirect:/chef/departement/" + departementId + "/modules";
    }

    // ===== SUPPRIMER =====
    @PostMapping("/departement/{departementId}/modules/{moduleId}/supprimer")
    public String supprimerModule(
            @PathVariable Long departementId,
            @PathVariable Long moduleId,
            RedirectAttributes redirectAttributes) {

        try {
            moduleCoursService.supprimer(moduleId);
            redirectAttributes.addFlashAttribute("succes", "Module supprimé.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/chef/departement/" + departementId + "/modules";
    }
}