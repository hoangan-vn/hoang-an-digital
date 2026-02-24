package com.happyending.blog.dtos

import com.happyending.blog.entities.BlogStatus
import java.time.LocalDateTime

data class BlogFilterDTO(
    val status: BlogStatus? = null,
    val authorId: String? = null,
    val tags: List<String>? = null,
    val categories: List<String>? = null,
    val isFeatured: Boolean? = null,
    val isPinned: Boolean? = null,
    val searchTerm: String? = null,
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null
)
