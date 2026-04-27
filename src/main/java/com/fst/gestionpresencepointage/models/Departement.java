package com.fst.gestionpresencepointage.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departements")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Departement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    private String nom;

    private String description;

    // Un département a un chef
    @OneToOne(mappedBy = "departement")
    private ChefDepartement chef;
// mappedBy = "departement" car c'est ChefDepartement
// qui possède la colonne departement_id ✅
    /**
     * Relation 1 → plusieurs avec les enseignants.
     * Un département peut avoir plusieurs enseignants.
     */
    @OneToMany(mappedBy = "departement")
    @Builder.Default
    private List<Enseignant> enseignants = new ArrayList<>();

    @OneToMany(mappedBy = "departement")
    @Builder.Default
    private List<Etudiant> etudiants = new ArrayList<>();

    /**
     * Relation 1 → plusieurs avec les modules.
     * Un département peut proposer plusieurs modules.
     */
    @OneToMany(mappedBy = "departement")
    @Builder.Default
    private List<ModuleCours> modules = new ArrayList<>();


}
