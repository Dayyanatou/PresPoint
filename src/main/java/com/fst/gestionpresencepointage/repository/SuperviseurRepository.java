package com.fst.gestionpresencepointage.repository;

import com.fst.gestionpresencepointage.models.Pointage;
import com.fst.gestionpresencepointage.models.Superviseur;
import com.fst.gestionpresencepointage.models.enums.StatutPointage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SuperviseurRepository extends JpaRepository<Superviseur, Long> {
    // Récupérer tous les pointages qu'un superviseur doit valider
    //List<Pointage> findBySuperviseur(Superviseur superviseur);
    // Et les pointages en attente de validation
    //List<Pointage> findByStatut(StatutPointage statut);
// → findByStatut(StatutPointage.EN_ATTENTE) dans le service ✅
}
