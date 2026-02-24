package com.happyending

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationListener
import org.springframework.core.env.Environment

@SpringBootApplication(exclude = [MongoAutoConfiguration::class, MongoDataAutoConfiguration::class])
class HappyendingApplication

fun main(args: Array<String>) {
    runApplication<HappyendingApplication>(*args) {
        addListeners(SwaggerUrlLogger())
    }
}

class SwaggerUrlLogger : ApplicationListener<ApplicationReadyEvent> {
    private val log = LoggerFactory.getLogger(SwaggerUrlLogger::class.java)

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        val environment = event.applicationContext.environment
        val port = environment.getProperty("local.server.port")
        val hostAddress = try {
            java.net.InetAddress.getLocalHost().hostAddress
        } catch (e: java.net.UnknownHostException) {
            "localhost"
        }

        val swaggerUrl = "http://$hostAddress:$port/swagger-ui.html"
        val localSwaggerUrl = "http://localhost:$port/swagger-ui.html"

        log.info("""
======================================================================================
" +
                "Swagger UI available at: $swaggerUrl
" +
                "Local Swagger UI available at: $localSwaggerUrl
" +
                "======================================================================================""")
    }
}