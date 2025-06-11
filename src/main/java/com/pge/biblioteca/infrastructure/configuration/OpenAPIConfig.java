package com.pge.biblioteca.infrastructure.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI bibliotecaApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Biblioteca API - PGE")
                        .description("Sistema de gestão de empréstimos de livros")
                        .version("1.0.0"));
    }
}
