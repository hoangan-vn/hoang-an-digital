package com.happyending.common.configs

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Value("\${spring.application.name:Happy Ending API}")
    private lateinit var appName: String

    @Value("\${openapi.title:Happy Ending API}")
    private lateinit var apiTitle: String

    @Value("\${openapi.description:API documentation for Happy Ending project}")
    private lateinit var apiDescription: String

    @Value("\${openapi.version:v1.0.0}")
    private lateinit var apiVersion: String

    @Value("\${openapi.contact.name:Happy Ending Team}")
    private lateinit var contactName: String

    @Value("\${openapi.contact.email:contact@happyending.com}")
    private lateinit var contactEmail: String

    @Value("\${openapi.license.name:MIT License}")
    private lateinit var licenseName: String

    @Value("\${openapi.license.url:https://opensource.org/licenses/MIT}")
    private lateinit var licenseUrl: String

    @Value("\${openapi.server.dev.url:http://localhost:8085}")
    private lateinit var devServerUrl: String

    @Value("\${openapi.server.dev.description:Development server}")
    private lateinit var devServerDescription: String

    @Value("\${openapi.server.prod.url:https://api.happyending.com}")
    private lateinit var prodServerUrl: String

    @Value("\${openapi.server.prod.description:Production server}")
    private lateinit var prodServerDescription: String

    @Bean
    fun openAPI(): OpenAPI {
        val securitySchemeName = "bearerAuth"
        return OpenAPI()
            .info(
                Info()
                    .title(apiTitle)
                    .description(apiDescription)
                    .version(apiVersion)
                    .contact(
                        Contact()
                            .name(contactName)
                            .email(contactEmail)
                    )
                    .license(
                        License()
                            .name(licenseName)
                            .url(licenseUrl)
                    )
            )
            .servers(
                listOf(
                    Server()
                        .url(devServerUrl)
                        .description(devServerDescription),
                    Server()
                        .url(prodServerUrl)
                        .description(prodServerDescription)
                )
            )
            // Commented out security configuration for development
            // .components(
            //     Components()
            //         .addSecuritySchemes(
            //             securitySchemeName,
            //             SecurityScheme()
            //                 .name(securitySchemeName)
            //                 .type(SecurityScheme.Type.HTTP)
            //                 .scheme("bearer")
            //                 .bearerFormat("JWT")
            //         )
            // )
    }

    @Bean
    fun productApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("products")
            .pathsToMatch("/api/v1/products/**")
            .build()
    }
}
