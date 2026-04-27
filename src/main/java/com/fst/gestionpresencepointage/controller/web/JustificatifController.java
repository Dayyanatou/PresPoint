package com.fst.gestionpresencepointage.controller.web;

import com.fst.gestionpresencepointage.service.JustificatifService;
import com.fst.gestionpresencepointage.service.PresenceService;
import com.fst.gestionpresencepointage.service.EtudiantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/etudiant")
@RequiredArgsConstructor
public class JustificatifController {

    private final JustificatifService justificatifService;
    private final PresenceService presenceService;
    private final EtudiantService etudiantService;

    // ===== PAGE JUSTIFICATIFS ÉTUDIANT =====
    // GET /etudiant/12/justificatifs
    @GetMapping("/{etudiantId}/justificatifs")
    public String pageJustificatifs(
            @PathVariable Long etudiantId,
            Model model) {

        // Infos étudiant pour la sidebar
        model.addAttribute("etudiant",
                etudiantService.rechercherParId(etudiantId));

        // Ses absences → uniquement les ABSENT
        // Ce sont les seules pour lesquelles on peut soumettre un justificatif
        model.addAttribute("absences",
                presenceService.listerAbsences(etudiantId));

        // Ses justificatifs déjà soumis
        model.addAttribute("justificatifs",
                justificatifService.listerParEtudiant(etudiantId));

        return "etudiants/justificatifs";
    }

    // ===== SOUMETTRE UN JUSTIFICATIF =====
    // POST /etudiant/12/justificatifs/soumettre
    @PostMapping("/{etudiantId}/justificatifs/soumettre")
    public String soumettre(
            @PathVariable Long etudiantId,
            @RequestParam Long presenceId,
            @RequestParam String motif,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) MultipartFile fichier,
            RedirectAttributes redirectAttributes) {

        try {
            justificatifService.soumettre(
                    etudiantId, presenceId, motif, description, fichier);

            // Message de succès affiché après la redirection
            redirectAttributes.addFlashAttribute("succes",
                    "Votre justificatif a été soumis avec succès !");

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        }

        // Retour à la page justificatif après soumission
        return "redirect:/etudiant/" + etudiantId + "/justificatifs";
    }
}