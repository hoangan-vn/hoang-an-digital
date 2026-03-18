package com.main.server.infrastructure.persistence.mongo.media

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "media_assets")
data class MediaAssetDocument(
    @Id
    val id: String? = null,

    val publicId: String,
    val url: String,
    val secureUrl: String,
    val format: String? = null,
    val bytes: Long? = null,
    val width: Int? = null,
    val height: Int? = null,

    @CreatedDate
    val createdAt: Instant? = null,

    @LastModifiedDate
    val updatedAt: Instant? = null,
)

