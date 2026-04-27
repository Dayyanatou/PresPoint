package com.fst.gestionpresencepointage.models;

import com.fst.gestionpresencepointage.models.enums.StatutPresence;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(
        name = "presences",
        uniqueConstraints = {
                //empêche que le même étudiant ait deux lignes de présence pour la même séance
                @UniqueConstraint(
                        name = "uk_presence_etudiant_seance",
                        columnNames = {"etudiant_id", "seance_id"}
                )
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Presence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Date de l'enregistrement de la présence
    private LocalDate datePresence;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)// pour indiquer à spring que c'est un enum
    private StatutPresence statut;// En liant avec celle-ci

    //Remarque éventuelle
    @Column(length = 255)
    private String remarque;

    //Plusieurs présences peuvent concerner un même étudiant.
    @ManyToOne
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;

    //Plusieurs présences appartiennent à une même séance
    @ManyToOne
    @JoinColumn(name = "seance_id", nullable = false)
    private Seance seance;

}
