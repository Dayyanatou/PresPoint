package com.fst.gestionpresencepointage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Ce DTO transporte : nom étudiant + son taux de présence
// Il est utilisé dans le dashboard Chef de Département
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TauxPresenceDto {

    // Informations de l'étudiant
    private Long etudiantId;
    private String nomComplet;    // "Aïcha Salami"
    private String matricule;     // "2204017"
    private String niveau;        // "Bachelor 3"

    // Taux de présence global (ex: 62.5)
    private double tauxGlobal;

    // Nombre total de séances
    private long totalSeances;

    // Nombre de présences
    private long totalPresences;

    // Nombre d'absences non justifiées
    private long absencesNonJustifiees;

    // Module où le taux est le plus bas
    private String moduleCritique;
}