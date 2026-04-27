package com.fst.gestionpresencepointage.controller.api;

import com.fst.gestionpresencepointage.dto.UtilisateurDto;
import com.fst.gestionpresencepointage.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UtilisateurRestController {

    private final UtilisateurService utilisateurService;

    // Créer un étudiant
    @PostMapping("/etudiants")
    public ResponseEntity<UtilisateurDto> creerEtudiant(@RequestBody UtilisateurDto dto) {
        return ResponseEntity.ok(utilisateurService.creerEtudiant(dto));
    }

    // Créer un enseignant
    @PostMapping("/enseignants")
    public ResponseEntity<UtilisateurDto> creerEnseignant(@RequestBody UtilisateurDto dto) {
        return ResponseEntity.ok(utilisateurService.creerEnseignant(dto));
    }

    // Créer un chef de département
    @PostMapping("/chefs")
    public ResponseEntity<UtilisateurDto> creerChef(@RequestBody UtilisateurDto dto) {
        return ResponseEntity.ok(utilisateurService.creerChefDepartement(dto));
    }

    // Créer un superviseur
    @PostMapping("/superviseurs")
    public ResponseEntity<UtilisateurDto> creerSuperviseur(@RequestBody UtilisateurDto dto) {
        return ResponseEntity.ok(utilisateurService.creerSuperviseur(dto));
    }

    // Lister tous les utilisateurs
    @GetMapping
    public ResponseEntity<List<UtilisateurDto>> listerTous() {
        return ResponseEntity.ok(utilisateurService.listerTous());
    }

    // Désactiver un compte
    @PatchMapping("/{id}/desactiver")
    public ResponseEntity<Void> desactiver(@PathVariable Long id) {
        utilisateurService.desactiver(id);
        return ResponseEntity.ok().build();
    }

    // Activer un compte
    @PatchMapping("/{id}/activer")
    public ResponseEntity<Void> activer(@PathVariable Long id) {
        utilisateurService.activer(id);
        return ResponseEntity.ok().build();
    }
}