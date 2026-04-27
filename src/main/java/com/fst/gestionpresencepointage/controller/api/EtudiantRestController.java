package com.fst.gestionpresencepointage.controller.api;

import com.fst.gestionpresencepointage.dto.EtudiantDto;
import com.fst.gestionpresencepointage.service.EtudiantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/etudiants")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")   // Pour permettre les tests depuis Postman
public class EtudiantRestController {

    private final EtudiantService etudiantService;

    // ==================== CREATE ====================
    @PostMapping
    public ResponseEntity<EtudiantDto> create(@RequestBody EtudiantDto dto) {
        EtudiantDto saved = etudiantService.enregistrer(dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // ==================== READ ALL ====================
    @GetMapping
    public ResponseEntity<List<EtudiantDto>> getAll() {
        List<EtudiantDto> etudiants = etudiantService.listerTous();
        return ResponseEntity.ok(etudiants);
    }

    // ==================== READ BY ID ====================
    @GetMapping("/{id}")
    public ResponseEntity<EtudiantDto> getById(@PathVariable Long id) {
        EtudiantDto etudiant = etudiantService.rechercherParId(id);
        return ResponseEntity.ok(etudiant);
    }

    // ==================== READ BY MATRICULE ====================
    @GetMapping("/matricule/{matricule}")
    public ResponseEntity<EtudiantDto> getByMatricule(@PathVariable String matricule) {
        EtudiantDto etudiant = etudiantService.rechercherParMatricule(matricule);
        return ResponseEntity.ok(etudiant);
    }

    // ==================== UPDATE ====================
    @PutMapping("/{id}")
    public ResponseEntity<EtudiantDto> update(@PathVariable Long id, @RequestBody EtudiantDto dto) {
        EtudiantDto updated = etudiantService.modifier(id, dto);
        return ResponseEntity.ok(updated);
    }

    // ==================== DELETE ====================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        etudiantService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}