package com.fst.gestionpresencepointage.dto;

import com.fst.gestionpresencepointage.models.Pointage;
import com.fst.gestionpresencepointage.models.Seance;
import lombok.Data;

import java.util.List;

@Data
public class DashboardEnseignantDto {

    private Long enseignantId;
    private String nomComplet;
    private String email;
    private String grade;
    private String specialite;
    private String departement;

    private long totalSeances;
    private long seancesAujourdHui;
    private long totalModules;
    private long totalPointages;

    private List<Seance> prochainesSeances;
    private List<Seance> seancesRecentes;
    private List<Pointage> pointagesRecents;
}
