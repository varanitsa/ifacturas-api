package com.arquitecturajava.rest.config;

import org.springframework.context.annotation.Bean;


import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.OpenAPI;



public class OpenApiConfig {

    @Bean
    public OpenAPI iFacturasOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("iFacturas API")
                        .description("Sample application")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("SpringShop Wiki Documentation")
                        .url("https://springshop.wiki.github.org/docs"));
    }
}
