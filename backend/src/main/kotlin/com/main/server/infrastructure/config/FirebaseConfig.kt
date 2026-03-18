package com.main.server.infrastructure.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.main.server.infrastructure.config.properties.FirebaseProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File
import java.io.FileInputStream

@Configuration
@EnableConfigurationProperties(FirebaseProperties::class)
class FirebaseConfig(
    private val props: FirebaseProperties,
) {
    @Bean
    fun firebaseApp(): FirebaseApp? {
        val path = props.serviceAccountPath.trim()
        if (path.isEmpty()) return null

        val file = File(path)
        require(file.exists() && file.isFile) { "Firebase service account json not found at: $path" }

        FileInputStream(file).use { input ->
            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(input))
                .build()

            val existing = FirebaseApp.getApps().firstOrNull()
            return existing ?: FirebaseApp.initializeApp(options)
        }
    }
}
