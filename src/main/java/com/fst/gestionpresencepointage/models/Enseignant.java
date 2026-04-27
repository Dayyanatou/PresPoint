package com.fst.gestionpresencepointage.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "enseignants")
@Data
@SuperBuilder       // pas @Builder — on hérite de Utilisateur
@NoArgsConstructor
@AllArgsConstructor
public class Enseignant extends Utilisateur {
    // pas de @Id ici — hérité de Utilisateur✅

    @Column(length = 50)
    private String grade;

    @Column(length = 50)
    private String specialite;

    @Column(name = "statut_enseignant", length = 50)
    private String statutEnseignant;
    // renommé pour éviter le conflit avec statut de Utilisateur✅

    @Column(length = 50)
    private String profil;

    @Column(name = "taux_horaire")
    private Integer tauxHoraire;

    @ManyToOne
    @JoinColumn(name = "departement_id")
    private Departement departement;

    @OneToMany(mappedBy = "enseignant", cascade = CascadeType.ALL)
    private List<Seance> seances = new ArrayList<>();

    @OneToMany(mappedBy = "enseignant", cascade = CascadeType.ALL)
    private List<Pointage> pointages = new ArrayList<>();
}