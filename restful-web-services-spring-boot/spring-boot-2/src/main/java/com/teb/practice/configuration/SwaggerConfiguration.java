package com.teb.practice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    public static final Contact DEFAULT_CONTACT =
            new Contact(
                    "Kuntal Ganguly",
                    "https://github.com/TheEncodedBong/",
                    "theencodedbong@gmail.com");

    public static final ApiInfo DEFAULT_API_INFO =
            new ApiInfo(
                    "Rest API",
                    "Restful Web Services",
                    "1.0",
                    "urn:tos",
                    DEFAULT_CONTACT,
                    "Apache 2.0",
                    "http://www.apache.org/licenses/LICENSE-2.0",
                    new ArrayList<>());

    /* Swagger documentation to layout details of the application in JSON format */
    @Bean
    public Docket api() {

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(DEFAULT_API_INFO); // personalised API info
    }
}
