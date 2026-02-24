package com.happyending.product.configs

import com.happyending.common.configs.MongoConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongoRepositories(basePackages = ["com.happyending.file"], mongoTemplateRef = "fileMongoTemplate")
class FileMongoConfig(private val mongoConfig: MongoConfig) {
    @Primary
    @Bean(name = ["fileMongoTemplate"])
    fun fileMongoTemplate(@Value("\${mongodb.file.uri}") uri: String): MongoTemplate {
        return mongoConfig.createMongoTemplate(uri)
    }
}
