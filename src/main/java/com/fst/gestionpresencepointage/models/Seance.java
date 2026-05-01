package com.fst.gestionpresencepointage.models;

import com.fst.gestionpresencepointage.models.enums.StatutSeance;
import com.fst.gestionpresencepointage.models.enums.TypeSeance;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "seances")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Date de la séance
    @Column(nullable = false)
    private LocalDate dateSeance;

    //Heure de début prévue
    private LocalTime heureDebut;
    //Heure de fin prévue
    private LocalTime heureFin;


    @Enumerated(EnumType.STRING)
    private StatutSeance statut; // PLANIFIEE, TERMINEE

    //Durée effective réellement constatée
    private Integer dureeEffective;
    // Tu peux modifier ton @PrePersist pour inclure le statut par défaut
    @PrePersist
    public void prePersist() {
        // Calcul de la durée
        if (heureDebut != null && heureFin != null) {
            this.dureeEffective = (int) java.time.Duration
                    .between(heureDebut, heureFin)
                    .toMinutes();
        }
        // Initialisation du statut si nul
        if (this.statut == null) {
            this.statut = StatutSeance.PLANIFIEE;
        }
    }
    //Type de séance : cours, TD, TP, etc.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TypeSeance typeSeance;


    // Jour de la semaine fixe : LUNDI, MARDI, MERCREDI, JEUDI, VENDREDI, SAMEDI
    @Column(length = 20)
    private String jourSemaine; // "LUNDI", "MARDI"...

    // Date de fin du semestre (fin de la récurrence)
    private LocalDate dateFinSemestre;

    // Indique si c'est une séance récurrente du planning
    @Column(nullable = false)
    @Builder.Default
    private boolean recurrente = false;

    //Plusieurs séances appartiennent à un même module
    @ManyToOne
    @JoinColumn(name = "module_id", nullable = false)
    private ModuleCours moduleCours;

    //Plusieurs séances peuvent être assurées par un même enseignant
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enseignant_id", nullable = false)
    private Enseignant enseignant;

    //Plusieurs séances peuvent se dérouler dans une même salle
    @ManyToOne
    @JoinColumn(name = "salle_id")
    private Salle salle;

    //Une séance génère plusieurs enregistrements de présence
    @OneToMany(mappedBy = "seance")
    @Builder.Default
    private List<Presence> presences = new ArrayList<>();

    /**
     * Une séance possède un pointage principal.
     * On choisit ici une relation 1 ↔ 1 pour simplifier.
     */
    @ToString.Exclude
    @OneToOne(mappedBy = "seance")
    private Pointage pointage;


}
