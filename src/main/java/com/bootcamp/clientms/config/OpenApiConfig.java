package com.bootcamp.clientms.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(title = "Client API", version = "1.0", description = "Microservicio de clientes"))
public class OpenApiConfig {
}
