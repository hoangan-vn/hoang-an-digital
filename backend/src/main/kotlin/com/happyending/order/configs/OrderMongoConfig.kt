package com.happyending.order.configs

import com.happyending.common.configs.MongoConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongoRepositories(basePackages = ["com.happyending.order"], mongoTemplateRef = "orderMongoTemplate")
class OrderMongoConfig(private val mongoConfig: MongoConfig) {
    @Bean(name = ["orderMongoTemplate"])
    fun orderMongoTemplate(@Value("\${mongodb.order.uri}") uri: String): MongoTemplate {
        return mongoConfig.createMongoTemplate(uri)
    }
}

