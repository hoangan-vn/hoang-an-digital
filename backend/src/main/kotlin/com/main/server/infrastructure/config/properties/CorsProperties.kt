package com.main.server.infrastructure.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.cors")
data class CorsProperties(
    val allowedOrigins: String = "http://localhost:3000",
    val allowedMethods: String = "GET,POST,PUT,PATCH,DELETE,OPTIONS",
    val allowedHeaders: String = "*",
    val exposedHeaders: String = "",
    val allowCredentials: Boolean = true,
    val maxAgeSeconds: Long = 3600,
) {
    fun allowedOriginsList(): List<String> = splitCsv(allowedOrigins)
    fun allowedMethodsList(): List<String> = splitCsv(allowedMethods)
    fun allowedHeadersList(): List<String> = splitCsv(allowedHeaders)
    fun exposedHeadersList(): List<String> = splitCsv(exposedHeaders)

    private fun splitCsv(value: String): List<String> =
        value.split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
}
