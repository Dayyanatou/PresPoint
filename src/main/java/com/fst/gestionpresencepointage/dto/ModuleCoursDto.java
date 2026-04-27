package com.fst.gestionpresencepointage.dto;

import com.fst.gestionpresencepointage.models.ModuleCours;
import com.fst.gestionpresencepointage.models.enums.Semestre;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModuleCoursDto {
    private Long id;
    private String code;
    private String intitule;
    private Semestre semestre;
    private Long departementId;
    private String departementNom;
    private Long enseignantResponsableId;
    private String enseignantResponsableNom;

    public static ModuleCoursDto fromEntity(ModuleCours module) {
        return ModuleCoursDto.builder()
                .id(module.getId())
                .code(module.getCode())
                .intitule(module.getIntitule())
                .semestre(module.getSemestre())
                .departementId(module.getDepartement() != null
                        ? module.getDepartement().getId() : null)
                .departementNom(module.getDepartement() != null
                        ? module.getDepartement().getNom() : null)
                .enseignantResponsableId(module.getEnseignantResponsable() != null
                        ? module.getEnseignantResponsable().getId() : null)
                .enseignantResponsableNom(module.getEnseignantResponsable() != null
                        ? module.getEnseignantResponsable().getNom() : null)
                .build();
    }
}
