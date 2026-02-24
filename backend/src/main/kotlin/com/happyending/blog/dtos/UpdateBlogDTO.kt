package com.happyending.blog.dtos

import com.happyending.blog.entities.BlogStatus

data class UpdateBlogDTO(
    val title: String? = null,
    val content: String? = null,
    val excerpt: String? = null,
    val status: BlogStatus? = null,
    val featuredImage: String? = null,
    val tags: List<String>? = null,
    val categories: List<String>? = null,
    val metaTitle: String? = null,
    val metaDescription: String? = null,
    val seoKeywords: List<String>? = null,
    val isFeatured: Boolean? = null,
    val isPinned: Boolean? = null
)
