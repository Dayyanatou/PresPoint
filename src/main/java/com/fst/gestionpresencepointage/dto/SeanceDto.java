package com.fst.gestionpresencepointage.dto;

import com.fst.gestionpresencepointage.models.Seance;
import com.fst.gestionpresencepointage.models.enums.TypeSeance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeanceDto {
    private Long id;
    private LocalDate dateSeance;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private TypeSeance typeSeance;
    private Integer dureeEffective;

    private Long moduleId;
    private String moduleIntitule;

    private Long enseignantId;
    private String enseignantNom;

    private Long salleId;
    private String salleNumero;

    public static SeanceDto fromEntity(Seance seance) {
        return SeanceDto.builder()
                .id(seance.getId())
                .dateSeance(seance.getDateSeance())
                .heureDebut(seance.getHeureDebut())
                .heureFin(seance.getHeureFin())
                .dureeEffective(seance.getDureeEffective())
                .typeSeance(seance.getTypeSeance())
                .moduleId(seance.getModuleCours() != null
                        ? seance.getModuleCours().getId() : null)
                .moduleIntitule(seance.getModuleCours() != null
                        ? seance.getModuleCours().getIntitule() : null)
                .enseignantId(seance.getEnseignant() != null
                        ? seance.getEnseignant().getId() : null)
                .enseignantNom(seance.getEnseignant() != null
                        ? seance.getEnseignant().getNom() : null)
                .salleId(seance.getSalle() != null
                        ? seance.getSalle().getId() : null)
                .salleNumero(seance.getSalle() != null
                        ? seance.getSalle().getNumero() : null)
                .build();
    }
}
