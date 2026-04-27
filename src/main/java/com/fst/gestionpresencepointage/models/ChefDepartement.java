package com.fst.gestionpresencepointage.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "chefs_departement")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ChefDepartement extends Utilisateur {
    // pas de @Id ici — hérité de Utilisateur ✅

    // Un chef gère un seul département
    @OneToOne
    @JoinColumn(name = "departement_id")
    private Departement departement;

}