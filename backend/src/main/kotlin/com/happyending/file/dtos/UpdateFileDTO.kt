package com.happyending.file.dtos

import com.happyending.file.entities.AccessMode

data class UpdateFileDTO(
    val fileName: String? = null,
    val tags: List<String>? = null,
    val isPublic: Boolean? = null,
    val accessMode: AccessMode? = null,
    val isFeatured: Boolean? = null,
    val description: String? = null,
    val altText: String? = null,
    val metadata: Map<String, Any>? = null
)
