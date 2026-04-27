package com.fst.gestionpresencepointage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Configuration
public class AuditConfig {
    // à créer dans un dossier config/

        @Bean
        public AuditorAware<String> auditorProvider() {
            return () -> Optional.of("système");
            // retourne le nom de qui fait l'action
            // plus tard on mettra le nom de l'utilisateur connecté
        }

}
