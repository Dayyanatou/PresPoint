package com.fst.gestionpresencepointage.controller.api;

import com.fst.gestionpresencepointage.config.JwtUtil;
import com.fst.gestionpresencepointage.models.Utilisateur;
import com.fst.gestionpresencepointage.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthRestController {

    private final UtilisateurRepository utilisateurRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String motDePasse = body.get("motDePasse");

        Optional<Utilisateur> optUser = utilisateurRepository.findByUsername(username);

        if (optUser.isEmpty()) {
            return ResponseEntity.status(401)
                    .body(Map.of("erreur", "Utilisateur introuvable"));
        }

        Utilisateur user = optUser.get();

        // Vérifie le mot de passe
        boolean valide = passwordEncoder.matches(motDePasse, user.getMotDePasse())
                || motDePasse.equals(user.getMotDePasse()); // compatibilité anciens comptes non chiffrés

        if (!valide) {
            return ResponseEntity.status(401)
                    .body(Map.of("erreur", "Mot de passe incorrect"));
        }

        if (user.getStatut() == null || !user.getStatut()) {
            return ResponseEntity.status(403)
                    .body(Map.of("erreur", "Compte désactivé"));
        }

        String token = jwtUtil.genererToken(
                user.getId(),
                user.getUsername(),
                user.getRole().name()
        );

        return ResponseEntity.ok(Map.of(
                "token", token,
                "userId", user.getId(),
                "username", user.getUsername(),
                "nom", user.getNom(),
                "prenom", user.getPrenom(),
                "role", user.getRole().name()
        ));
    }
}