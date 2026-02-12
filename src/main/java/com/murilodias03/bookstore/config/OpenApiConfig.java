package com.murilodias03.bookstore.config;

import com.murilodias03.bookstore.services.InstanceInformationService;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private final InstanceInformationService instanceInformationService;

    public OpenApiConfig(InstanceInformationService instanceInformationService) {
        this.instanceInformationService = instanceInformationService;
    }

    @Bean
    OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Book Store")
                        .version("v1")
                        .description("REST API's RESTful from 0 with Java, Spring Boot, Kubernetes and Docker V3 " + instanceInformationService.retrieveInstanceInfo())
                        .termsOfService("https://github.com/MuriloDias03")
                        .license(new License()
                                .name("MIT License")
                                .url("https://github.com/MuriloDias03"))
                );
    }

}