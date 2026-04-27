package com.fst.gestionpresencepointage.controller.api;

import com.fst.gestionpresencepointage.dto.SeanceDto;
import com.fst.gestionpresencepointage.models.enums.TypeSeance;
import com.fst.gestionpresencepointage.service.SeanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/seances")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Autorise Flutter à appeler cette API
public class SeanceRestController {

    private final SeanceService seanceService;

    // ===== CREATE =====
    // POST /api/seances
    // Body JSON : { "dateSeance": "2026-04-21", "heureDebut": "08:00", ... }
    @PostMapping
    public ResponseEntity<SeanceDto> creer(@RequestBody SeanceDto dto) {
        SeanceDto creee = seanceService.creer(dto);
        return new ResponseEntity<>(creee, HttpStatus.CREATED);
    }

    // ===== READ BY ID =====
    // GET /api/seances/1
    @GetMapping("/{id}")
    public ResponseEntity<SeanceDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(seanceService.getById(id));
    }

    // ===== READ ALL =====
    // GET /api/seances
    @GetMapping
    public ResponseEntity<List<SeanceDto>> getAll() {
        return ResponseEntity.ok(seanceService.listerTous());
    }

    // ===== FILTER PAR ENSEIGNANT =====
    // GET /api/seances/enseignant/3
    // Flutter : l'enseignant connecté voit ses séances
    @GetMapping("/enseignant/{enseignantId}")
    public ResponseEntity<List<SeanceDto>> getParEnseignant(
            @PathVariable Long enseignantId) {
        return ResponseEntity.ok(seanceService.listerParEnseignant(enseignantId));
    }

    // ===== FILTER PAR MODULE =====
    // GET /api/seances/module/2
    @GetMapping("/module/{moduleId}")
    public ResponseEntity<List<SeanceDto>> getParModule(
            @PathVariable Long moduleId) {
        return ResponseEntity.ok(seanceService.listerParModule(moduleId));
    }

    // ===== FILTER PAR DATE =====
    // GET /api/seances/date/2026-04-21
    // @DateTimeFormat : dit à Spring comment lire la date dans l'URL
    @GetMapping("/date/{date}")
    public ResponseEntity<List<SeanceDto>> getParDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(seanceService.listerParDate(date));
    }

    // ===== FILTER PAR PÉRIODE =====
    // GET /api/seances/periode?debut=2026-04-01&fin=2026-04-30
    @GetMapping("/periode")
    public ResponseEntity<List<SeanceDto>> getParPeriode(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(seanceService.listerParPeriode(debut, fin));
    }

    // ===== FILTER PAR TYPE =====
    // GET /api/seances/type/COURS  ou  /type/TD  ou  /type/TP
    @GetMapping("/type/{type}")
    public ResponseEntity<List<SeanceDto>> getParType(
            @PathVariable TypeSeance type) {
        return ResponseEntity.ok(seanceService.listerParType(type));
    }

    // ===== SÉANCES DU JOUR PAR DÉPARTEMENT =====
    // GET /api/seances/aujourd-hui/departement/1
    // Utilisé dans le dashboard Flutter de l'enseignant
    @GetMapping("/aujourd-hui/departement/{departementId}")
    public ResponseEntity<List<SeanceDto>> getAujourdhui(
            @PathVariable Long departementId) {
        return ResponseEntity.ok(seanceService.getSeancesAujourdhui(departementId));
    }

    // ===== SÉANCES DE LA SEMAINE PAR DÉPARTEMENT =====
    // GET /api/seances/semaine/departement/1
    @GetMapping("/semaine/departement/{departementId}")
    public ResponseEntity<List<SeanceDto>> getSemaine(
            @PathVariable Long departementId) {
        return ResponseEntity.ok(seanceService.getSeancesDeLaSemaine(departementId));
    }

    // ===== UPDATE =====
    // PUT /api/seances/1
    @PutMapping("/{id}")
    public ResponseEntity<SeanceDto> modifier(
            @PathVariable Long id,
            @RequestBody SeanceDto dto) {
        return ResponseEntity.ok(seanceService.modifier(id, dto));
    }

    // ===== DELETE =====
    // DELETE /api/seances/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        seanceService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}