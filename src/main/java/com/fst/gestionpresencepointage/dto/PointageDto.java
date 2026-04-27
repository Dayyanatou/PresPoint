package com.fst.gestionpresencepointage.dto;

import com.fst.gestionpresencepointage.models.Pointage;
import com.fst.gestionpresencepointage.models.enums.StatutPointage;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class PointageDto {
    private Long id;
    private LocalDate date;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private Integer duree;
    private StatutPointage statut;
    private Long enseignantId;
    private String enseignantNom;
    private Long seanceId;
    private Long superviseurId; //pour confirmation
    private String seanceModuleIntitule;

    public static PointageDto fromEntity(Pointage pointage) {
        return PointageDto.builder()
                .id(pointage.getId())
                .date(pointage.getDate())
                .heureDebut(pointage.getHeureDebut())
                .heureFin(pointage.getHeureFin())
                .duree(pointage.getDuree())
                .statut(pointage.getStatut())
                .enseignantId(pointage.getEnseignant() != null
                        ? pointage.getEnseignant().getId() : null)
                .enseignantNom(pointage.getEnseignant() != null
                        ? pointage.getEnseignant().getNom() : null)
                .seanceId(pointage.getSeance() != null
                        ? pointage.getSeance().getId() : null)
                .superviseurId(pointage.getSuperviseur() != null
                        ? pointage.getSuperviseur().getId() : null)
                .seanceModuleIntitule(
                        pointage.getSeance() != null &&
                                pointage.getSeance().getModuleCours() != null
                                ? pointage.getSeance().getModuleCours().getIntitule()
                                : null
                )
                .build();
    }
}
