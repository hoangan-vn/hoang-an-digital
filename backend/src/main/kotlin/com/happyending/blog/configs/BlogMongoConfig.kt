package com.happyending.blog.configs

import com.happyending.common.configs.MongoConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongoRepositories(basePackages = ["com.happyending.blog"], mongoTemplateRef = "blogMongoTemplate")
class BlogMongoConfig(private val mongoConfig: MongoConfig) {
    @Primary
    @Bean(name = ["blogMongoTemplate"])
    fun blogMongoTemplate(@Value("\${mongodb.blog.uri}") uri: String): MongoTemplate {
        return mongoConfig.createMongoTemplate(uri)
    }
}




