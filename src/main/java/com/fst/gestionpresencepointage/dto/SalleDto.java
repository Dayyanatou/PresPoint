package com.fst.gestionpresencepointage.dto;

import com.fst.gestionpresencepointage.models.Salle;
import com.fst.gestionpresencepointage.models.enums.TypeSalle;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SalleDto {
    private Long id;
    private String numero;
    private Integer capacite;
    private TypeSalle type;

    public static SalleDto fromEntity(Salle salle) {
        return SalleDto.builder()
                .id(salle.getId())
                .numero(salle.getNumero())
                .capacite(salle.getCapacite())
                .type(salle.getType())
                .build();
    }
}
