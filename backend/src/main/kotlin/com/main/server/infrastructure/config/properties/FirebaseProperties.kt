package com.main.server.infrastructure.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.firebase")
data class FirebaseProperties(
    /**
     * Absolute path to service account json.
     * Keep empty to disable Firebase initialization (useful for local dev).
     */
    val serviceAccountPath: String = "",
)
