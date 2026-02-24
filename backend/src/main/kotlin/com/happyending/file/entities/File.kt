package com.happyending.file.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "files")
data class File(
    @Id
    val id: String? = null,

    var originalName: String,
    var fileName: String,
    var publicId: String,
    var url: String,
    var secureUrl: String,
    var format: String,
    var resourceType: String,
    var fileSize: Long,
    var width: Int? = null,
    var height: Int? = null,
    var mimeType: String,

    var uploaderId: String? = null,
    var uploaderName: String? = null,
    var uploaderEmail: String? = null,

    var folder: String? = null,

    var tags: List<String> = emptyList(),

    var isPublic: Boolean = true,
    var accessMode: AccessMode = AccessMode.PUBLIC,

    var downloadCount: Long = 0,
    var viewCount: Long = 0,
    var isFeatured: Boolean = false,

    var description: String? = null,

    var altText: String? = null,

    var metadata: Map<String, Any> = emptyMap(),

    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
)




