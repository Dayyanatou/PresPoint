package com.fst.gestionpresencepointage.dto;

import com.fst.gestionpresencepointage.models.Departement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartementDto {

    private Long id;
    private String nom;

    public static DepartementDto fromEntity(Departement departement) {
        return DepartementDto.builder()
                .id(departement.getId())
                .nom(departement.getNom())
                .build();
    }
}