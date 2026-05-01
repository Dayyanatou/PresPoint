package com.fst.gestionpresencepointage.models;

import com.fst.gestionpresencepointage.models.enums.Semestre;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "modules_cours")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModuleCours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Code unique du module.

    @Column(nullable = false, unique = true, length = 30)
    private String code;

    //Intitulé du module.
    @Column(nullable = false, length = 150)
    private String intitule;

    //Volume horaire global.
    private Integer volumeHoraire;

    //Coefficient du module.
    //private Integer coefficient;

    //Semestre d'appartenance.
    private Semestre semestre;

    @Column(length = 50)
    private String niveau; // "Info 1", "Info 2", "Télécom 3"

    @Column(length = 10)
    private String groupe; // "G1", "G2", null = cours commun

    //Plusieurs modules appartiennent à un même département

    @ManyToOne
    @JoinColumn(name = "departement_id", nullable = false)
    private Departement departement;

    /**
     * Un module peut être affecté à un enseignant responsable.
     * Cette relation peut rester facultative au début.
     */
    @ManyToOne
    @JoinColumn(name = "enseignant_id")
    private Enseignant enseignantResponsable;

    //Un module peut contenir plusieurs séances.
    @OneToMany(mappedBy = "moduleCours")
    private List<Seance> seances = new ArrayList<>();

}
