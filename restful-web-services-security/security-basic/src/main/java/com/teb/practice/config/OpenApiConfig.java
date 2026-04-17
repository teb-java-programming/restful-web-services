package com.teb.practice.config;

import static io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        // Name used in Swagger security config
        String securitySchemeName = "basicAuth";

        return new OpenAPI()
                .info(
                        new Info()
                                .title("Security Basic API")
                                .version("1.0")
                                .description("Spring Basic Security + Swagger"))
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(securitySchemeName)) // Apply global auth in Swagger
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(HTTP)
                                                .scheme("basic") // HTTP Basic Auth
                                        ));
    }
}
