package com.fst.gestionpresencepointage.controller.api;

import com.fst.gestionpresencepointage.dto.JustificatifDto;
import com.fst.gestionpresencepointage.service.JustificatifService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/justificatifs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Pour Flutter
public class JustificatifRestController {

    private final JustificatifService justificatifService;

    // ===== SOUMETTRE UN JUSTIFICATIF =====
    // POST /api/justificatifs/soumettre
    // Flutter envoie un formulaire multipart avec le fichier
    // @RequestParam : chaque champ du formulaire est un paramètre séparé
    @PostMapping("/soumettre")
    public ResponseEntity<JustificatifDto> soumettre(
            @RequestParam Long etudiantId,
            @RequestParam Long presenceId,
            @RequestParam String motif,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) MultipartFile fichier) {

        JustificatifDto result = justificatifService.soumettre(
                etudiantId, presenceId, motif, description, fichier);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    // ===== ACCEPTER UN JUSTIFICATIF =====
    // PATCH /api/justificatifs/1/accepter?chefId=5
    @PatchMapping("/{id}/accepter")
    public ResponseEntity<JustificatifDto> accepter(
            @PathVariable Long id,
            @RequestParam Long chefId,
            @RequestParam(required = false) String commentaire) {

        return ResponseEntity.ok(
                justificatifService.accepter(id, chefId, commentaire));
    }

    // ===== REFUSER UN JUSTIFICATIF =====
    // PATCH /api/justificatifs/1/refuser?chefId=5
    @PatchMapping("/{id}/refuser")
    public ResponseEntity<JustificatifDto> refuser(
            @PathVariable Long id,
            @RequestParam Long chefId,
            @RequestParam(required = false) String commentaire) {

        return ResponseEntity.ok(
                justificatifService.refuser(id, chefId, commentaire));
    }

    // ===== JUSTIFICATIFS D'UN ÉTUDIANT =====
    // GET /api/justificatifs/etudiant/12
    // Flutter : l'étudiant voit ses justificatifs
    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<List<JustificatifDto>> getParEtudiant(
            @PathVariable Long etudiantId) {

        return ResponseEntity.ok(
                justificatifService.listerParEtudiant(etudiantId));
    }

    // ===== JUSTIFICATIFS EN ATTENTE PAR DÉPARTEMENT =====
    // GET /api/justificatifs/departement/1/en-attente
    // Flutter : le chef voit les justificatifs à traiter
    @GetMapping("/departement/{departementId}/en-attente")
    public ResponseEntity<List<JustificatifDto>> getEnAttenteParDepartement(
            @PathVariable Long departementId) {

        return ResponseEntity.ok(
                justificatifService.listerEnAttenteParDepartement(departementId));
    }
}