package com.happyending.file.dtos

import com.happyending.file.entities.AccessMode

data class CreateFileDTO(
    val originalName: String,
    val uploaderId: String,
    val uploaderName: String,
    val uploaderEmail: String,
    val folder: String? = null,
    val tags: List<String> = emptyList(),
    val isPublic: Boolean = true,
    val accessMode: AccessMode = AccessMode.PUBLIC,
    val description: String? = null,
    val altText: String? = null,
    val metadata: Map<String, Any> = emptyMap()
)
