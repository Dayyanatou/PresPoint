package com.fst.gestionpresencepointage.models;

import com.fst.gestionpresencepointage.models.enums.StatutPointage;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "pointages")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pointage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime heureDebut;

    private LocalTime heureFin;

    private Integer duree;
    @PrePersist
    @PreUpdate // Ajoute ça !
    public void calculerDuree() {
        if (this.heureDebut != null && this.heureFin != null) {
            this.duree = (int) java.time.Duration.between(heureDebut, heureFin).toMinutes();
        }
    }

    @Enumerated(EnumType.STRING)// pour indiquer à spring que c'est un enum
    private StatutPointage statut;

    @Column(length = 255)
    private String remarque;

    @Column(nullable = false)
    private LocalDate date;

    //Plusieurs pointages peuvent être liés à un enseignant.
    @ManyToOne
    @JoinColumn(name = "enseignant_id", nullable = false) //Clé étrangère
    private Enseignant enseignant;

    //Ici, on choisit un pointage principal par séance.
    @OneToOne
    @JoinColumn(name = "seance_id", nullable = false, unique = true)
    private Seance seance;

    // Qui a confirmé ce pointage
    @ManyToOne
    @JoinColumn(name = "superviseur_id")
    private Superviseur superviseur; // nullable au début — pas encore confirmé


    @ManyToOne
    @JoinColumn(name = "chef_departement_id")
    private ChefDepartement chefDepartement; // null → validé quand rempli
}
