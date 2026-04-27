package com.fst.gestionpresencepointage.controller.api;

import com.fst.gestionpresencepointage.dto.DepartementDto;
import com.fst.gestionpresencepointage.service.DepartementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/departements")
@RequiredArgsConstructor
public class DepartementRestController {

    private final DepartementService departementService;

    @GetMapping
    public ResponseEntity<List<DepartementDto>> getTous() {
        return ResponseEntity.ok(departementService.listerTous());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartementDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(departementService.rechercherParId(id));
    }

    @PostMapping
    public ResponseEntity<DepartementDto> creer(@RequestBody DepartementDto dto) {
        return ResponseEntity.status(201).body(departementService.enregistrer(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartementDto> modifier(
            @PathVariable Long id,
            @RequestBody DepartementDto dto) {
        return ResponseEntity.ok(departementService.modifier(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        departementService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}


/*
package com.fst.gestionpresencepointage.controller.api;

import com.fst.gestionpresencepointage.dto.DepartementDto;
import com.fst.gestionpresencepointage.service.DepartementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departements")
@RequiredArgsConstructor
public class DepartementRestController {

    private final DepartementService departementService;

    // ✅ CREATE
    @PostMapping
    public DepartementDto creer(@RequestBody DepartementDto dto) {
        return departementService.enregistrer(dto);
    }

    // ✅ READ ALL
    @GetMapping
    public List<DepartementDto> getAll() {
        return departementService.listerTous();
    }

    // ✅ READ BY ID
    @GetMapping("/{id}")
    public DepartementDto getById(@PathVariable Long id) {
        return departementService.rechercherParId(id);
    }

    // ✅ READ BY NOM
    @GetMapping("/nom/{nom}")
    public DepartementDto getByNom(@PathVariable String nom) {
        return departementService.rechercherParNom(nom);
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public DepartementDto update(@PathVariable Long id,
                                 @RequestBody DepartementDto dto) {
        return departementService.modifier(id, dto);
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        departementService.supprimer(id);
    }
}*/
