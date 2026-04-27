package com.fst.gestionpresencepointage.controller.web;

import com.fst.gestionpresencepointage.config.JwtUtil;
import com.fst.gestionpresencepointage.models.Utilisateur;
import com.fst.gestionpresencepointage.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // Affiche la page login
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // Traitement du formulaire login
    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String motDePasse,
            Model model) {

        // Chercher l'utilisateur
        Optional<Utilisateur> optUser = utilisateurRepository.findByUsername(username);

        if (optUser.isEmpty()) {
            model.addAttribute("erreur", "Nom d'utilisateur introuvable.");
            return "login";
        }

        Utilisateur user = optUser.get();

        // Vérifier le mot de passe (BCrypt ou texte brut pour anciens comptes)
        boolean mdpOk = false;
        try {
            mdpOk = passwordEncoder.matches(motDePasse, user.getMotDePasse());
        } catch (Exception e) {
            mdpOk = motDePasse.equals(user.getMotDePasse());
        }
        if (!mdpOk && motDePasse.equals(user.getMotDePasse())) {
            mdpOk = true;
        }

        if (!mdpOk) {
            model.addAttribute("erreur", "Mot de passe incorrect.");
            return "login";
        }

        // Vérifier si compte actif
        if (Boolean.FALSE.equals(user.getStatut())) {
            model.addAttribute("erreur", "Compte désactivé. Contactez l'administrateur.");
            return "login";
        }

        // Vérifier si première connexion → forcer changement mot de passe
        if (user.isPremiereConnexion()) {
            return "redirect:/changer-mot-de-passe/" + user.getId();
        }

        // Rediriger selon le rôle
        String role = user.getRole().name();
        Long id = user.getId();

        return switch (role) {
            case "ETUDIANT"          -> "redirect:/etudiant/dashboard/" + id;
            case "ENSEIGNANT"        -> "redirect:/enseignant/dashboard/" + id;
            case "CHEF_DEPARTEMENT"  -> "redirect:/chef/dashboard/" + id;
            case "SUPERVISEUR"       -> "redirect:/superviseur/dashboard/" + id;
            case "ADMIN"             -> "redirect:/admin/comptes";
            default -> {
                model.addAttribute("erreur", "Rôle inconnu.");
                yield "login";
            }
        };
    }

    // Déconnexion
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login?deconnecte=true";
    }
}

/*
package com.fst.gestionpresencepointage.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // La vraie connexion viendra avec Spring Security
    // Pour l'instant redirection directe selon le rôle
    @PostMapping("/auth/connexion")
    public String connexion(
            @RequestParam String username,
            @RequestParam String motDePasse,
            RedirectAttributes redirectAttributes) {

        // Temporaire pour tester — à remplacer par Spring Security
        redirectAttributes.addFlashAttribute("erreur",
                "Connexion manuelle non encore implémentée. " +
                        "Utilisez les boutons de test rapide.");
        return "redirect:/login";
    }
}*/
