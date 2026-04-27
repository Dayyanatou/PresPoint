package com.fst.gestionpresencepointage.controller.api;

import com.fst.gestionpresencepointage.dto.PresenceDto;
import com.fst.gestionpresencepointage.models.enums.StatutPresence;
import com.fst.gestionpresencepointage.service.PresenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/presences")
@RequiredArgsConstructor
public class PresenceRestController {

    private final PresenceService presenceService;

    @GetMapping
    public ResponseEntity<List<PresenceDto>> getAll() {
        return ResponseEntity.ok(presenceService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PresenceDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(presenceService.getById(id));
    }

    @GetMapping("/seance/{seanceId}")
    public ResponseEntity<List<PresenceDto>> getParSeance(@PathVariable Long seanceId) {
        return ResponseEntity.ok(presenceService.listerParSeance(seanceId));
    }

    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<List<PresenceDto>> getParEtudiant(@PathVariable Long etudiantId) {
        return ResponseEntity.ok(presenceService.listerParEtudiant(etudiantId));
    }

    // GET /api/presences/seance/3/statut?statut=ABSENT
    @GetMapping("/seance/{seanceId}/statut")
    public ResponseEntity<List<PresenceDto>> getParSeanceEtStatut(
            @PathVariable Long seanceId,
            @RequestParam StatutPresence statut) {
        return ResponseEntity.ok(presenceService.listerParSeanceEtStatut(seanceId, statut));
    }

    @PostMapping
    public ResponseEntity<PresenceDto> enregistrer(@RequestBody PresenceDto dto) {
        return ResponseEntity.status(201).body(presenceService.enregistrer(dto));
    }

    // PATCH /api/presences/5/statut?nouveauStatut=JUSTIFIE
    @PatchMapping("/{id}/statut")
    public ResponseEntity<PresenceDto> modifierStatut(
            @PathVariable Long id,
            @RequestParam StatutPresence nouveauStatut) {
        return ResponseEntity.ok(presenceService.modifierStatut(id, nouveauStatut));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        presenceService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}