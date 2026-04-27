package com.fst.gestionpresencepointage.controller.api;

import com.fst.gestionpresencepointage.dto.PointageDto;
import com.fst.gestionpresencepointage.models.enums.StatutPointage;
import com.fst.gestionpresencepointage.service.PointageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pointages")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PointageRestController {

    private final PointageService pointageService;

    // ===== SOUMETTRE UN POINTAGE =====
    // POST /api/pointages
    // Appelé par l'enseignant depuis Flutter après sa séance
    // Body : { "seanceId": 1, "enseignantId": 2, "heureDebut": "08:00", ... }
    @PostMapping
    public ResponseEntity<PointageDto> soumettre(@RequestBody PointageDto dto) {
        PointageDto cree = pointageService.soumettre(dto);
        return new ResponseEntity<>(cree, HttpStatus.CREATED);
    }

    // ===== CONFIRMER PAR SUPERVISEUR =====
    // PATCH /api/pointages/1/confirmer?superviseurId=3
    // Le superviseur confirme qu'il a bien vu la séance se dérouler
    @PatchMapping("/{id}/confirmer")
    public ResponseEntity<PointageDto> confirmer(
            @PathVariable Long id,
            @RequestParam Long superviseurId) {
        return ResponseEntity.ok(pointageService.confirmer(id, superviseurId));
    }

    // ===== VALIDER PAR CHEF DE DÉPARTEMENT =====
    // PATCH /api/pointages/1/valider?chefId=5
    // Le chef valide définitivement → statut devient VALIDE
    @PatchMapping("/{id}/valider")
    public ResponseEntity<PointageDto> valider(
            @PathVariable Long id,
            @RequestParam Long chefId) {
        return ResponseEntity.ok(pointageService.valider(id, chefId));
    }

    // ===== REJETER PAR CHEF DE DÉPARTEMENT =====
    // PATCH /api/pointages/1/rejeter?chefId=5
    // Le chef rejette → statut devient REJETE
    @PatchMapping("/{id}/rejeter")
    public ResponseEntity<PointageDto> rejeter(
            @PathVariable Long id,
            @RequestParam Long chefId) {
        return ResponseEntity.ok(pointageService.rejeter(id, chefId));
    }

    // ===== READ BY ID =====
    // GET /api/pointages/1
    @GetMapping("/{id}")
    public ResponseEntity<PointageDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(pointageService.getById(id));
    }

    // ===== POINTAGES PAR ENSEIGNANT =====
    // GET /api/pointages/enseignant/2
    // L'enseignant voit l'historique de ses pointages
    @GetMapping("/enseignant/{enseignantId}")
    public ResponseEntity<List<PointageDto>> getParEnseignant(
            @PathVariable Long enseignantId) {
        return ResponseEntity.ok(pointageService.listerParEnseignant(enseignantId));
    }

    // ===== POINTAGES PAR STATUT =====
    // GET /api/pointages/statut/EN_ATTENTE
    // GET /api/pointages/statut/VALIDE
    // GET /api/pointages/statut/REJETE
    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<PointageDto>> getParStatut(
            @PathVariable StatutPointage statut) {
        return ResponseEntity.ok(pointageService.listerParStatut(statut));
    }

    // ===== TOUS LES POINTAGES EN ATTENTE =====
    // GET /api/pointages/en-attente
    // Vue globale pour le chef de département
    @GetMapping("/en-attente")
    public ResponseEntity<List<PointageDto>> getEnAttente() {
        return ResponseEntity.ok(pointageService.listerEnAttente());
    }

    // ===== POINTAGES EN ATTENTE PAR DÉPARTEMENT =====
    // GET /api/pointages/en-attente/departement/1
    // Le chef voit seulement les pointages de SON département
    @GetMapping("/en-attente/departement/{departementId}")
    public ResponseEntity<List<PointageDto>> getEnAttenteParDepartement(
            @PathVariable Long departementId) {
        return ResponseEntity.ok(
                pointageService.listerEnAttenteParDepartement(departementId)
        );
    }
}