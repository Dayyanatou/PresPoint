package com.fst.gestionpresencepointage.controller.web;

import com.fst.gestionpresencepointage.dto.EtudiantDto;
import com.fst.gestionpresencepointage.service.DepartementService;
import com.fst.gestionpresencepointage.service.ImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/chef")
@RequiredArgsConstructor
public class ImportController {

    private final ImportService importService;
    private final DepartementService departementService;

    // ===== PAGE IMPORT =====
    // GET /chef/departement/1/import
    @GetMapping("/departement/{departementId}/import")
    public String pageImport(
            @PathVariable Long departementId,
            Model model) {

        model.addAttribute("departementId", departementId);
        model.addAttribute("departements",
                departementService.listerTous());

        return "chef/import";
    }

    // ===== TRAITEMENT IMPORT =====
    // POST /chef/departement/1/import
    @PostMapping("/departement/{departementId}/import")
    public String traiterImport(
            @PathVariable Long departementId,
            @RequestParam MultipartFile fichier,
            @RequestParam String niveau,
            RedirectAttributes redirectAttributes) {

        // Vérifier que le fichier n'est pas vide
        if (fichier.isEmpty()) {
            redirectAttributes.addFlashAttribute("erreur",
                    "Veuillez sélectionner un fichier Excel.");
            return "redirect:/chef/departement/" + departementId + "/import";
        }

        // Vérifier l'extension du fichier
        String nomFichier = fichier.getOriginalFilename();
        // ✅ APRÈS
        if (nomFichier == null ||
                (!nomFichier.endsWith(".xlsx") && !nomFichier.endsWith(".csv"))) {
            redirectAttributes.addFlashAttribute("erreur",
                    "Le fichier doit être au format .xlsx ou .csv");
            return "redirect:/chef/departement/" + departementId + "/import";
        }

        try {
            List<EtudiantDto> importes = importService
                    .importerEtudiantsDepuisExcel(fichier, departementId, niveau);

            // Message de succès avec le nombre d'étudiants importés
            redirectAttributes.addFlashAttribute("succes",
                    importes.size() + " étudiant(s) importé(s) avec succès ! " +
                            "Mot de passe temporaire = matricule de chaque étudiant.");

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        }

        return "redirect:/chef/departement/" + departementId + "/import";
    }
}