package com.fst.gestionpresencepointage.service;

import com.fst.gestionpresencepointage.dto.EtudiantDto;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface ImportService {

    // Importe les étudiants depuis un fichier Excel
    // Retourne la liste des étudiants créés avec succès
    List<EtudiantDto> importerEtudiantsDepuisExcel(
            MultipartFile fichier,
            Long departementId,
            String niveau
    );
}