package com.main.server.infrastructure.config

import com.main.server.infrastructure.config.properties.MailProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(MailProperties::class)
class MailConfig
