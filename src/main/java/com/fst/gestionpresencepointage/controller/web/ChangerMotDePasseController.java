package com.fst.gestionpresencepointage.controller.web;

import com.fst.gestionpresencepointage.models.Utilisateur;
import com.fst.gestionpresencepointage.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ChangerMotDePasseController {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/changer-mot-de-passe/{id}")
    public String page(@PathVariable Long id, Model model) {
        utilisateurRepository.findById(id).ifPresent(u -> model.addAttribute("userId", id));
        return "changer-mot-de-passe";
    }

    @PostMapping("/changer-mot-de-passe/{id}")
    public String changer(
            @PathVariable Long id,
            @RequestParam String nouveauMotDePasse,
            @RequestParam String confirmation,
            Model model) {

        if (!nouveauMotDePasse.equals(confirmation)) {
            model.addAttribute("userId", id);
            model.addAttribute("erreur", "Les mots de passe ne correspondent pas.");
            return "changer-mot-de-passe";
        }

        if (nouveauMotDePasse.length() < 6) {
            model.addAttribute("userId", id);
            model.addAttribute("erreur", "Le mot de passe doit faire au moins 6 caractères.");
            return "changer-mot-de-passe";
        }

        Utilisateur user = utilisateurRepository.findById(id).orElseThrow();
        user.setMotDePasse(passwordEncoder.encode(nouveauMotDePasse));
        user.setPremiereConnexion(false); // ← ne plus forcer le changement
        utilisateurRepository.save(user);

        // Rediriger vers login avec message de succès
        return "redirect:/login?mdpChange=true";
    }
}