package com.fst.gestionpresencepointage.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "superviseurs")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Superviseur extends Utilisateur {

    // La relation avec les pointages qu'il supervise
    @OneToMany(mappedBy = "superviseur", cascade = CascadeType.ALL)
    private List<Pointage> pointagesAConfirmer = new ArrayList<>();

    // La relation avec le département qu'il supervise
    @ManyToOne
    @JoinColumn(name = "departement_id")
    private Departement departement;
}
