package com.fst.gestionpresencepointage.controller.api;

import com.fst.gestionpresencepointage.dto.EtudiantDto;
import com.fst.gestionpresencepointage.service.ImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ImportRestController {

    private final ImportService importService;

    // POST /api/import/etudiants
    // Flutter envoie un formulaire multipart
    @PostMapping("/etudiants")
    public ResponseEntity<List<EtudiantDto>> importerEtudiants(
            @RequestParam MultipartFile fichier,
            @RequestParam Long departementId,
            @RequestParam String niveau) {

        List<EtudiantDto> importes = importService
                .importerEtudiantsDepuisExcel(fichier, departementId, niveau);

        return new ResponseEntity<>(importes, HttpStatus.CREATED);
    }
}