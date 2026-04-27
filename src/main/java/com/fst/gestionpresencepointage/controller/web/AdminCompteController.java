package com.fst.gestionpresencepointage.controller.web;

import com.fst.gestionpresencepointage.dto.UtilisateurDto;
import com.fst.gestionpresencepointage.repository.DepartementRepository;
import com.fst.gestionpresencepointage.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/comptes")
@RequiredArgsConstructor
public class AdminCompteController {

    private final UtilisateurService utilisateurService;
    private final DepartementRepository departementRepository;

    // Page création de comptes
    @GetMapping
    public String pageCreation(Model model) {
        model.addAttribute("utilisateurs", utilisateurService.listerTous());
        model.addAttribute("departements", departementRepository.findAll());
        return "admin/comptes";
    }

    // Créer étudiant
    @PostMapping("/etudiant")
    public String creerEtudiant(@ModelAttribute UtilisateurDto dto) {
        try {
            utilisateurService.creerEtudiant(dto);
        } catch (Exception e) {
            return "redirect:/admin/comptes?erreur=" + e.getMessage();
        }
        return "redirect:/admin/comptes?succes=etudiant";
    }

    // Créer enseignant
    @PostMapping("/enseignant")
    public String creerEnseignant(@ModelAttribute UtilisateurDto dto) {
        try {
            utilisateurService.creerEnseignant(dto);
        } catch (Exception e) {
            return "redirect:/admin/comptes?erreur=" + e.getMessage();
        }
        return "redirect:/admin/comptes?succes=enseignant";
    }

    // Créer chef
    @PostMapping("/chef")
    public String creerChef(@ModelAttribute UtilisateurDto dto) {
        try {
            utilisateurService.creerChefDepartement(dto);
        } catch (Exception e) {
            return "redirect:/admin/comptes?erreur=" + e.getMessage();
        }
        return "redirect:/admin/comptes?succes=chef";
    }

    // Créer superviseur
    @PostMapping("/superviseur")
    public String creerSuperviseur(@ModelAttribute UtilisateurDto dto) {
        try {
            utilisateurService.creerSuperviseur(dto);
        } catch (Exception e) {
            return "redirect:/admin/comptes?erreur=" + e.getMessage();
        }
        return "redirect:/admin/comptes?succes=superviseur";
    }

    // Désactiver un compte
    @PostMapping("/{id}/desactiver")
    public String desactiver(@PathVariable Long id) {
        utilisateurService.desactiver(id);
        return "redirect:/admin/comptes";
    }

    // Activer un compte
    @PostMapping("/{id}/activer")
    public String activer(@PathVariable Long id) {
        utilisateurService.activer(id);
        return "redirect:/admin/comptes";
    }
}