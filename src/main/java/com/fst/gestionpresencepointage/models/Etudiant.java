package com.fst.gestionpresencepointage.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "etudiants")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Etudiant extends Utilisateur {

    @Column(nullable = false, unique = true, length = 50)
    private String matricule;

    @PastOrPresent //validation
    private LocalDate dateNaissance;

    private String lieuNaissance;

    @Column(length = 50)
    private String niveau;

    @Column(length = 200)
    private String adresse;

    @Column(length = 150)
    private String personneAContacter;//Personne à contacter si besoin.


    /**
     * Plusieurs étudiants peuvent appartenir à un même département.
     */
    @ManyToOne
    @JoinColumn(name = "departement_id")
    private Departement departement;

    /**
     * Un étudiant peut avoir plusieurs enregistrements de présence.
     */
    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL)
    private List<Presence> presences = new ArrayList<>();

}
