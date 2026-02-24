package com.happyending.file.dtos

import com.happyending.file.entities.AccessMode
import java.time.LocalDateTime

data class FileFilterDTO(
    val uploaderId: String? = null,
    val resourceType: String? = null,
    val format: String? = null,
    val folder: String? = null,
    val tags: List<String>? = null,
    val isPublic: Boolean? = null,
    val accessMode: AccessMode? = null,
    val isFeatured: Boolean? = null,
    val searchTerm: String? = null,
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null
)
