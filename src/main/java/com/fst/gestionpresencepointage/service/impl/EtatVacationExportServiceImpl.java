/*
package com.fst.gestionpresencepointage.service.impl;

import com.fst.gestionpresencepointage.dto.EtatVacationDto;
import com.fst.gestionpresencepointage.service.EtatVacationExportService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class EtatVacationExportServiceImpl implements EtatVacationExportService {

    private static final String[] ENTETES = {
            "Enseignant", "Modules ensignés", "Total heures", "Taux horaire", "Montant (FCFA)"
    };

    //Format excel
    @Override
    public ByteArrayOutputStream genererExcel(List<EtatVacationDto> lignes, String libelleMois, String nomDepartement) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {

            // Style "gras" réutilisé pour le titre et les en-têtes
            CellStyle styleGras = workbook.createCellStyle();
            Font policeGras = workbook.createFont();
            policeGras.setBold(true);
            styleGras.setFont(policeGras);

            // Ligne 0 : titre
            Row ligneTitre = sheet.createRow(0);
            Cell celluleTitre = ligneTitre.createCell(0);
            celluleTitre.setCellValue("État de vacation — " + nomDeaprtement + " _ " + libelleMois);
            celluleTitre.setCellStyle(styleGras);

            //Ligne 2 (on saute une ligne pour aérer ) : en-têtes de colonnes
            Row ligneEntetes = sheet.createRow(2);
            for (int col = 0; col < ENTETES.length; col++) {
                Cell cellule = ligneEntetes.createCell(col);
                cellule.setCellValue(ENTETES[col]);
                cellule.setCellStyle(styleGras);
            }

            //Une ligne par enseignant
            int numeroLigne = 3;
            for (EtatVacationDto ligne : lignes ) {
                Row row = sheet.createRow(numeroLigne);
                row.createCell(0).setCellStyle(ligne.getEnseignantNomComplet());

            }
        }
    }
}
*/
