package com.fst.gestionpresencepointage.config;

import com.fst.gestionpresencepointage.models.Departement;
import com.fst.gestionpresencepointage.repository.DepartementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * INITIALISATION DES DONNÉES DE BASE
 * Cette classe s'exécute AUTOMATIQUEMENT à chaque démarrage de l'application.
 */
@Component
@RequiredArgsConstructor
public class DonneeInitialiser implements CommandLineRunner {

    private final DepartementRepository departementRepository;

    @Override
    public void run(String... args) {

        // ==================== CRÉATION DES 2 DÉPARTEMENTS FIXES ====================
        creerDepartementSiAbsent("informatique", "Département Informatique");
        creerDepartementSiAbsent("télécom", "Département Télécommunications");
    }

    /**
     * Méthode réutilisable pour créer un département seulement s'il n'existe pas déjà
     */
    private void creerDepartementSiAbsent(String nom, String description) {
        if (!departementRepository.existsByNomIgnoreCase(nom)) {
            Departement d = Departement.builder()
                    .nom(nom)
                    .description(description)
                    .build();
            departementRepository.save(d);
            System.out.println("✅ Département '" + nom + "' créé automatiquement");
        } else {
            System.out.println("✅ Département '" + nom + "' existe déjà");
        }
    }
}


/*
package com.fst.gestionpresencepointage.config;

import com.fst.gestionpresencepointage.models.Departement;
import com.fst.gestionpresencepointage.repository.DepartementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DonneeInitialiser implements CommandLineRunner  {

    private final DepartementRepository departementRepository;

    @Override
    public void run(String... args) {

        if (!departementRepository.existsByNomIgnoreCase("informatique")) {

            Departement d = new Departement();
            d.setNom("informatique");

            departementRepository.save(d);
        }
    }
}*/
