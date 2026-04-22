package com.teb.practice.config;

import static io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String CORRELATION_ID = "correlationId";
    private static final String BASIC_AUTH = "basicAuth";

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(
                        new Info()
                                .title("User API")
                                .version("1.0")
                                .description("Spring Boot + Actuator + Swagger"))
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(BASIC_AUTH)) // Apply global auth in Swagger
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        BASIC_AUTH,
                                        new SecurityScheme()
                                                .name(BASIC_AUTH)
                                                .type(HTTP)
                                                .scheme("basic")) // HTTP Basic Auth
                                .addParameters(
                                        CORRELATION_ID,
                                        new Parameter()
                                                .in("header")
                                                .name(CORRELATION_ID)
                                                .description("Correlation Id for request tracing")
                                                .required(false)
                                                .schema(new StringSchema())));
    }
}
