package com.happyending.blog.dtos

data class CreateBlogDTO(
    val title: String,
    val content: String,
    val excerpt: String? = null,
    val authorId: String,
    val authorName: String,
    val authorEmail: String,
    val featuredImage: String? = null,
    val tags: List<String> = emptyList(),
    val categories: List<String> = emptyList(),
    val metaTitle: String? = null,
    val metaDescription: String? = null,
    val seoKeywords: List<String> = emptyList(),
    val isFeatured: Boolean = false,
    val isPinned: Boolean = false
)
