package com.fst.gestionpresencepointage.controller.api;

import com.fst.gestionpresencepointage.dto.EnseignantDto;
import com.fst.gestionpresencepointage.service.EnseignantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enseignants")
@RequiredArgsConstructor
public class EnseignantRestController {

    private final EnseignantService enseignantService;

    // ✅ CREATE
    @PostMapping
    public EnseignantDto creer(@RequestBody EnseignantDto dto) {
        return enseignantService.enregistrer(dto);
    }

    // ✅ READ ALL
    @GetMapping
    public List<EnseignantDto> getAll() {
        return enseignantService.listerTous();
    }

    // ✅ READ BY ID
    @GetMapping("/{id}")
    public EnseignantDto getById(@PathVariable Long id) {
        return enseignantService.rechercherParId(id);
    }

    // ✅ FILTER PAR DEPARTEMENT
    @GetMapping("/departement/{id}")
    public List<EnseignantDto> getByDepartement(@PathVariable Long id) {
        return enseignantService.listerParDepartement(id);
    }

    // ✅ FILTER PAR GRADE
    @GetMapping("/grade")
    public List<EnseignantDto> getByGrade(@RequestParam String grade) {
        return enseignantService.rechercherParGrade(grade);
    }

    // ✅ FILTER PAR STATUT
    @GetMapping("/statut")
    public List<EnseignantDto> getByStatut(@RequestParam String statut) {
        return enseignantService.rechercherParStatut(statut);
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public EnseignantDto update(@PathVariable Long id,
                                @RequestBody EnseignantDto dto) {
        return enseignantService.modifier(id, dto);
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        enseignantService.supprimer(id);
    }
}