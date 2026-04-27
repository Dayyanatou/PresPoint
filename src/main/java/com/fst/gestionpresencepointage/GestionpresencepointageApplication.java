package com.fst.gestionpresencepointage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing //Active la gestion automatique des dates et des infos d’audit”
@SpringBootApplication
public class GestionpresencepointageApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionpresencepointageApplication.class, args);
	}

}
