package com.happyending.blog.dtos

import com.happyending.blog.entities.BlogStatus
import java.time.LocalDateTime

data class BlogDTO(
    val id: String? = null,
    val title: String,
    val slug: String,
    val content: String,
    val excerpt: String,
    val authorId: String,
    val authorName: String,
    val authorEmail: String,
    val status: BlogStatus = BlogStatus.DRAFT,
    val featuredImage: String? = null,
    val tags: List<String> = emptyList(),
    val categories: List<String> = emptyList(),
    val metaTitle: String? = null,
    val metaDescription: String? = null,
    val seoKeywords: List<String> = emptyList(),
    val viewCount: Long = 0,
    val likeCount: Long = 0,
    val commentCount: Long = 0,
    val isFeatured: Boolean = false,
    val isPinned: Boolean = false,
    val readingTime: Int = 0,
    val publishedAt: LocalDateTime? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
