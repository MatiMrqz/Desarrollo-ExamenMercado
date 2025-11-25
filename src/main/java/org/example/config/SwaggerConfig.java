package org.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mutant Detection API")
                        .version("1.0.0")
                        .description("API for detecting mutants based on DNA sequences and retrieving statistics.")
                        .contact(new Contact()
                                .name("Matías Márquez")
                                .email("mati.mrqz@gmail.com")));
    }
}
