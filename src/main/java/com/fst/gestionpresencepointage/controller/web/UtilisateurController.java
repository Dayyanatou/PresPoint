package com.fst.gestionpresencepointage.controller.web;

import com.fst.gestionpresencepointage.dto.UtilisateurDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Controller
@RequestMapping("/web/utilisateurs")
@RequiredArgsConstructor
public class UtilisateurController {

    @GetMapping("/form")
    public String afficherFormulaire(Model model) {

        model.addAttribute("utilisateur", new UtilisateurDto());
        model.addAttribute("roles", Arrays.asList("ETUDIANT", "ENSEIGNANT", "ADMIN"));

        return "utilisateurs/form"; // 📁 templates/utilisateurs/form.html
    }

    @PostMapping("/save")
    public String enregistrer(@ModelAttribute UtilisateurDto dto) {

        System.out.println("Utilisateur reçu : " + dto);

        return "redirect:/web/utilisateurs/form?success";
    }
}