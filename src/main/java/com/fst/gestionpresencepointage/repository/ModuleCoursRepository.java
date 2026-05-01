package com.fst.gestionpresencepointage.repository;

import com.fst.gestionpresencepointage.models.Departement;
import com.fst.gestionpresencepointage.models.ModuleCours;
import com.fst.gestionpresencepointage.models.Enseignant;
import com.fst.gestionpresencepointage.models.enums.Semestre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ModuleCoursRepository extends JpaRepository<ModuleCours, Long> {
    Optional<ModuleCours> findByCode(String code);

    boolean existsByCode(String code);

    List<ModuleCours> findByDepartement(Departement departement);

    List<ModuleCours> findByEnseignantResponsable(Enseignant enseignant);

    List<ModuleCours> findByIntituleContainingIgnoreCase(String motCle);

    List<ModuleCours> findBySemestre(Semestre semestre);

    // Modules d'un département + niveau
    List<ModuleCours> findByDepartementAndNiveau(Departement departement, String niveau);

    // Modules d'un département + niveau + groupe
    List<ModuleCours> findByDepartementAndNiveauAndGroupe(
            Departement departement, String niveau, String groupe);
}
