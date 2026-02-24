package com.happyending.file.dtos

import com.happyending.file.entities.AccessMode
import java.time.LocalDateTime

data class FileDTO(
    val id: String? = null,
    val originalName: String,
    val fileName: String,
    val publicId: String,
    val url: String,
    val secureUrl: String,
    val format: String,
    val resourceType: String,
    val fileSize: Long,
    val width: Int? = null,
    val height: Int? = null,
    val mimeType: String,
    val uploaderId: String,
    val uploaderName: String,
    val uploaderEmail: String,
    val folder: String? = null,
    val tags: List<String> = emptyList(),
    val isPublic: Boolean = true,
    val accessMode: AccessMode = AccessMode.PUBLIC,
    val downloadCount: Long = 0,
    val viewCount: Long = 0,
    val isFeatured: Boolean = false,
    val description: String? = null,
    val altText: String? = null,
    val metadata: Map<String, Any> = emptyMap(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)




