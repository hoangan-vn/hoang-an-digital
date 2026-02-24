package com.happyending.common.configs

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import javax.annotation.PostConstruct

@Configuration
class FirebaseConfig {

    @Value("\${firebase.service-account-file}")
    private lateinit var serviceAccountFile: Resource

    @PostConstruct
    fun initializeFirebaseApp() {
        if (FirebaseApp.getApps().isEmpty()) {
            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccountFile.inputStream))
                .build()
            FirebaseApp.initializeApp(options)
        }
    }

    @Bean
    fun firebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Bean
    fun firebaseMessaging(): FirebaseMessaging {
        return FirebaseMessaging.getInstance()
    }
}