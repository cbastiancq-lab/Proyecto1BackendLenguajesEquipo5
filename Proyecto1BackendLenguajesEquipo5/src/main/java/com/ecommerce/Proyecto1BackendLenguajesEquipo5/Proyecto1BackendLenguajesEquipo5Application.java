package com.ecommerce.Proyecto1BackendLenguajesEquipo5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.ecommerce.proyecto.data.entity")
@EnableJpaRepositories("com.ecommerce.proyecto.data.repository")
public class Proyecto1BackendLenguajesEquipo5Application {

	public static void main(String[] args) {
		SpringApplication.run(Proyecto1BackendLenguajesEquipo5Application.class, args);
	}

}
