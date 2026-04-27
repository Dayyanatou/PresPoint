package com.fst.gestionpresencepointage.controller.web;

import com.fst.gestionpresencepointage.dto.UtilisateurDto;
import com.fst.gestionpresencepointage.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UtilisateurService utilisateurService;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/utilisateurs/choix-role")
    public String choixRole() {
        return "admin/choix-role";
    }

    // ✅ Corrigé : UtilisateurDto au lieu de Utilisateur
    @GetMapping("/utilisateurs/edit/{id}")  // ✅ aussi corrigé : /admin/admin/ → /admin/
    public String editUser(@PathVariable Long id, Model model) {
        UtilisateurDto user = utilisateurService.getById(id);
        model.addAttribute("utilisateur", user);
        return "admin/edit-user";
    }
}