package com.main.server.infrastructure.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.cloudinary")
data class CloudinaryProperties(
    val cloudName: String = "",
    val apiKey: String = "",
    val apiSecret: String = "",
) {
    fun isConfigured(): Boolean =
        cloudName.isNotBlank() && apiKey.isNotBlank() && apiSecret.isNotBlank()
}
