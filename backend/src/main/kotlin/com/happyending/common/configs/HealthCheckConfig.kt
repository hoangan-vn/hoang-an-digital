package com.happyending.common.configs

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate

@Configuration
class HealthCheckConfig {

    @Bean
    fun productDbHealthIndicator(@Qualifier("productMongoTemplate") mongoTemplate: MongoTemplate): HealthIndicator {
        return HealthIndicator { 
            try {
                mongoTemplate.db.name
                Health.up().withDetail("database", "Product DB").build()
            } catch (e: Exception) {
                Health.down(e).withDetail("database", "Product DB").build()
            }
        }
    }

    @Bean
    fun orderDbHealthIndicator(@Qualifier("orderMongoTemplate") mongoTemplate: MongoTemplate): HealthIndicator {
        return HealthIndicator { 
            try {
                mongoTemplate.db.name
                Health.up().withDetail("database", "Order DB").build()
            } catch (e: Exception) {
                Health.down(e).withDetail("database", "Order DB").build()
            }
        }
    }
}
