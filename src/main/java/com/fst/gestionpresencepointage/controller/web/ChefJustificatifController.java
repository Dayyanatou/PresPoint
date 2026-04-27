package com.fst.gestionpresencepointage.controller.web;

import com.fst.gestionpresencepointage.service.JustificatifService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/chef")
@RequiredArgsConstructor
public class ChefJustificatifController {

    private final JustificatifService justificatifService;

    // ===== PAGE JUSTIFICATIFS CHEF =====
    // GET /chef/departement/1/justificatifs
    @GetMapping("/departement/{departementId}/justificatifs")
    public String pageJustificatifs(
            @PathVariable Long departementId,
            Model model) {

        // Justificatifs EN_ATTENTE de ce département
        model.addAttribute("justificatifsEnAttente",
                justificatifService.listerEnAttenteParDepartement(departementId));

        // Pour garder le departementId accessible dans le template
        model.addAttribute("departementId", departementId);

        return "chef/justificatifs";
    }

    // ===== ACCEPTER UN JUSTIFICATIF =====
    // POST /chef/justificatifs/1/accepter
    @PostMapping("/justificatifs/{justificatifId}/accepter")
    public String accepter(
            @PathVariable Long justificatifId,
            @RequestParam Long chefId,
            @RequestParam Long departementId,
            @RequestParam(required = false) String commentaire,
            RedirectAttributes redirectAttributes) {

        try {
            justificatifService.accepter(justificatifId, chefId, commentaire);
            redirectAttributes.addFlashAttribute("succes",
                    "Justificatif accepté. La présence a été mise à jour.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        }

        return "redirect:/chef/departement/" + departementId + "/justificatifs";
    }

    // ===== REFUSER UN JUSTIFICATIF =====
    // POST /chef/justificatifs/1/refuser
    @PostMapping("/justificatifs/{justificatifId}/refuser")
    public String refuser(
            @PathVariable Long justificatifId,
            @RequestParam Long chefId,
            @RequestParam Long departementId,
            @RequestParam(required = false) String commentaire,
            RedirectAttributes redirectAttributes) {

        try {
            justificatifService.refuser(justificatifId, chefId, commentaire);
            redirectAttributes.addFlashAttribute("erreur",
                    "Justificatif refusé.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        }

        return "redirect:/chef/departement/" + departementId + "/justificatifs";
    }
}