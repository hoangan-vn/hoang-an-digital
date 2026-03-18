package com.main.server.infrastructure.config

import com.cloudinary.Cloudinary
import com.main.server.infrastructure.config.properties.CloudinaryProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(CloudinaryProperties::class)
class CloudinaryConfig(
    private val props: CloudinaryProperties,
) {
    @Bean
    fun cloudinary(): Cloudinary? {
        if (!props.isConfigured()) return null

        return Cloudinary(
            mapOf(
                "cloud_name" to props.cloudName,
                "api_key" to props.apiKey,
                "api_secret" to props.apiSecret,
                "secure" to true,
            ),
        )
    }
}
