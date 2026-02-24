package com.happyending.common.configs

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory
import org.springframework.stereotype.Component

@Component
class MongoConfig {
    fun createMongoTemplate(uri: String): MongoTemplate {
        val factory = SimpleMongoClientDatabaseFactory(uri)
        return MongoTemplate(factory)
    }
}
