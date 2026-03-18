package com.main.server.infrastructure.persistence.mongo.media

import org.springframework.data.mongodb.repository.MongoRepository

interface MediaAssetMongoRepository : MongoRepository<MediaAssetDocument, String>

