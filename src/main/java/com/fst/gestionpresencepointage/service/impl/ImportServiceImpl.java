package com.fst.gestionpresencepointage.service.impl;

import com.fst.gestionpresencepointage.dto.EtudiantDto;
import com.fst.gestionpresencepointage.models.Departement;
import com.fst.gestionpresencepointage.repository.DepartementRepository;
import com.fst.gestionpresencepointage.service.EtudiantService;
import com.fst.gestionpresencepointage.service.ImportService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportServiceImpl implements ImportService {

    private final EtudiantService etudiantService;
    private final DepartementRepository departementRepository;

    // =====================================================
    // MÉTHODE PRINCIPALE — détecte le format du fichier
    // =====================================================
    @Override
    public List<EtudiantDto> importerEtudiantsDepuisExcel(
            MultipartFile fichier,
            Long departementId,
            String niveau) {

        String nomFichier = fichier.getOriginalFilename();

        // Regarde l'extension et appelle la bonne méthode
        if (nomFichier != null && nomFichier.endsWith(".csv")) {
            // Fichier CSV → méthode CSV
            return importerDepuisCSV(fichier, departementId, niveau);

        } else if (nomFichier != null && nomFichier.endsWith(".xlsx")) {
            // Fichier Excel → méthode Excel
            return importerDepuisExcel(fichier, departementId, niveau);

        } else {
            // Autre format → erreur
            throw new RuntimeException(
                    "Format non supporté. Utilisez .xlsx ou .csv");
        }
    }

    // =====================================================
    // MÉTHODE PRIVÉE — lit un fichier Excel .xlsx
    // =====================================================
    private List<EtudiantDto> importerDepuisExcel(
            MultipartFile fichier,
            Long departementId,
            String niveau) {

        // Vérifier que le département existe
        departementRepository.findById(departementId)
                .orElseThrow(() -> new RuntimeException("Département introuvable."));

        List<EtudiantDto> etudiantsImportes = new ArrayList<>();
        List<String> erreurs = new ArrayList<>();

        try {
            // Ouvrir le fichier Excel
            Workbook workbook = new XSSFWorkbook(fichier.getInputStream());
            Sheet feuille = workbook.getSheetAt(0);

            // Parcourir les lignes — i=1 pour sauter l'en-tête
            for (int i = 1; i <= feuille.getLastRowNum(); i++) {
                Row ligne = feuille.getRow(i);
                if (ligne == null) continue;

                try {
                    String nom       = lireCellule(ligne.getCell(0));
                    String prenom    = lireCellule(ligne.getCell(1));
                    String email     = lireCellule(ligne.getCell(2));
                    String matricule = lireCellule(ligne.getCell(3));
                    String telephone = lireCellule(ligne.getCell(4));

                    if (nom.isEmpty() || prenom.isEmpty()
                            || email.isEmpty() || matricule.isEmpty()) {
                        erreurs.add("Ligne " + (i+1)
                                + " : champs obligatoires manquants.");
                        continue;
                    }

                    EtudiantDto dto = EtudiantDto.builder()
                            .nom(nom).prenom(prenom)
                            .email(email).matricule(matricule)
                            .telephone(telephone).niveau(niveau)
                            .departementId(departementId)
                            .motDePasse(matricule)
                            .statut(true)
                            .build();

                    etudiantsImportes.add(etudiantService.enregistrer(dto));

                } catch (RuntimeException e) {
                    erreurs.add("Ligne " + (i+1) + " : " + e.getMessage());
                }
            }
            workbook.close();

        } catch (IOException e) {
            throw new RuntimeException(
                    "Erreur lecture Excel : " + e.getMessage());
        }

        if (etudiantsImportes.isEmpty() && !erreurs.isEmpty()) {
            throw new RuntimeException(
                    "Aucun étudiant importé. Erreurs : "
                            + String.join(", ", erreurs));
        }

        return etudiantsImportes;
    }

    // =====================================================
    // MÉTHODE PRIVÉE — lit un fichier CSV .csv
    // =====================================================
    private List<EtudiantDto> importerDepuisCSV(
            MultipartFile fichier,
            Long departementId,
            String niveau) {

        departementRepository.findById(departementId)
                .orElseThrow(() -> new RuntimeException("Département introuvable."));

        List<EtudiantDto> etudiantsImportes = new ArrayList<>();

        try {
            // Lire le fichier texte ligne par ligne
            java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.InputStreamReader(
                            fichier.getInputStream(), "UTF-8"));

            String ligne;
            int numeroLigne = 0;

            while ((ligne = reader.readLine()) != null) {
                numeroLigne++;

                // Sauter la première ligne (en-tête)
                if (numeroLigne == 1) continue;
                if (ligne.trim().isEmpty()) continue;

                // Couper la ligne sur virgule OU point-virgule
                String[] colonnes = ligne.split(",|;");
                if (colonnes.length < 4) continue;

                String nom       = colonnes[0].trim();
                String prenom    = colonnes[1].trim();
                String email     = colonnes[2].trim();
                String matricule = colonnes[3].trim();
                String telephone = colonnes.length > 4
                        ? colonnes[4].trim() : "";

                if (nom.isEmpty() || prenom.isEmpty()
                        || email.isEmpty() || matricule.isEmpty()) continue;

                try {
                    EtudiantDto dto = EtudiantDto.builder()
                            .nom(nom).prenom(prenom)
                            .email(email).matricule(matricule)
                            .telephone(telephone).niveau(niveau)
                            .departementId(departementId)
                            .motDePasse(matricule)
                            .statut(true)
                            .build();

                    etudiantsImportes.add(etudiantService.enregistrer(dto));

                } catch (RuntimeException e) {
                    // Étudiant déjà existant → on continue
                }
            }
            reader.close();

        } catch (java.io.IOException e) {
            throw new RuntimeException(
                    "Erreur lecture CSV : " + e.getMessage());
        }

        return etudiantsImportes;
    }

    // =====================================================
    // MÉTHODE UTILITAIRE — lit une cellule Excel
    // =====================================================
    private String lireCellule(Cell cellule) {
        if (cellule == null) return "";

        return switch (cellule.getCellType()) {
            case STRING  -> cellule.getStringCellValue().trim();
            case NUMERIC -> String.valueOf(
                    (long) cellule.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cellule.getBooleanCellValue());
            default      -> "";
        };
    }
}