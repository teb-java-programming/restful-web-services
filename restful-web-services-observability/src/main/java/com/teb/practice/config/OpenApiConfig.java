package com.teb.practice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String CORRELATION_ID = "correlationId";

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(
                        new Info()
                                .title("User API")
                                .version("v1")
                                .description("Spring Boot + Actuator + Swagger"))
                .components(
                        new Components()
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
