package com.main.server.domain.media

import java.time.Instant

data class MediaAsset(
    val id: String? = null,
    val publicId: String,
    val url: String,
    val secureUrl: String,
    val format: String? = null,
    val bytes: Long? = null,
    val width: Int? = null,
    val height: Int? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
)

