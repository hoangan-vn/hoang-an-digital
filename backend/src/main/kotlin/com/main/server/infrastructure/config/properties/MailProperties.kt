package com.main.server.infrastructure.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.mail")
data class MailProperties(
    val from: String = "no-reply@example.com",
)
