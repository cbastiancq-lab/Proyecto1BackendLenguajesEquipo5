package com.ecommerce.Proyecto1BackendLenguajesEquipo5.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Habilitador de propiedades de configuración.
 * 
 * Esta clase asegura que las clases anotadas con @ConfigurationProperties
 * sean reconocidas como beans y se carguen correctamente desde application.properties
 */
@Configuration
@EnableConfigurationProperties({
    CorsProperties.class,
    JwtProperties.class
})
public class ConfigurationPropertiesConfig {
    // Esta clase solo actúa como contenedor para @EnableConfigurationProperties
}
