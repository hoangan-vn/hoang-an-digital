package com.main.server.infrastructure.config

import com.main.server.infrastructure.config.properties.CorsProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableConfigurationProperties(CorsProperties::class)
class CorsConfig(
    private val cors: CorsProperties,
) : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        val origins = cors.allowedOriginsList()
        if (origins.isEmpty()) return

        val methods = cors.allowedMethodsList().toTypedArray()
        val headers = cors.allowedHeadersList().toTypedArray()
        val exposed = cors.exposedHeadersList().toTypedArray()

        registry.addMapping("/**")
            .allowedOriginPatterns(*origins.toTypedArray())
            .allowedMethods(*methods)
            .allowedHeaders(*headers)
            .exposedHeaders(*exposed)
            .allowCredentials(cors.allowCredentials)
            .maxAge(cors.maxAgeSeconds)
    }
}
