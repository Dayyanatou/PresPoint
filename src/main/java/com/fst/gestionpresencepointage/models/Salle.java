package com.fst.gestionpresencepointage.models;

import com.fst.gestionpresencepointage.models.enums.TypeSalle;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "salles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Salle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Numéro ou code de salle.
    @Column(nullable = false, unique = true, length = 30)
    private String numero;

    //Capacité maximale
    private Integer capacite;
    /**
     * Type de salle (optionnel au début).
     * Exemple : Salle classique, laboratoire, amphithéâtre.
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private TypeSalle type;

    //Une salle peut accueillir plusieurs séances à des dates différentes
    @OneToMany(mappedBy = "salle")
    @Builder.Default
    private List<Seance> seances = new ArrayList<>();

}
