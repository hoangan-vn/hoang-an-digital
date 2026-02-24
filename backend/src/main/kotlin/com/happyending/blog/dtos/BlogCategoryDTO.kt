package com.happyending.blog.dtos

import java.time.LocalDateTime

data class BlogCategoryDTO(
    val id: String? = null,
    val name: String,
    val slug: String,
    val description: String? = null,
    val parentId: String? = null,
    val color: String? = null,
    val icon: String? = null,
    val isActive: Boolean = true,
    val sortOrder: Int = 0,
    val blogCount: Long = 0,
    val metaTitle: String? = null,
    val metaDescription: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
