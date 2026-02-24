package com.happyending.blog.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime

@Document(collection = "blogs")
data class Blog(
    @Id
    val id: String? = null,
    
    @Field("title")
    val title: String,
    
    @Field("slug")
    val slug: String,
    
    @Field("content")
    val content: String,
    
    @Field("excerpt")
    val excerpt: String,
    
    @Field("author_id")
    val authorId: String,
    
    @Field("author_name")
    val authorName: String,
    
    @Field("author_email")
    val authorEmail: String,
    
    @Field("status")
    val status: BlogStatus = BlogStatus.DRAFT,
    
    @Field("featured_image")
    val featuredImage: String? = null,
    
    @Field("tags")
    val tags: List<String> = emptyList(),
    
    @Field("categories")
    val categories: List<String> = emptyList(),
    
    @Field("meta_title")
    val metaTitle: String? = null,
    
    @Field("meta_description")
    val metaDescription: String? = null,
    
    @Field("seo_keywords")
    val seoKeywords: List<String> = emptyList(),
    
    @Field("view_count")
    val viewCount: Long = 0,
    
    @Field("like_count")
    val likeCount: Long = 0,
    
    @Field("comment_count")
    val commentCount: Long = 0,
    
    @Field("is_featured")
    val isFeatured: Boolean = false,
    
    @Field("is_pinned")
    val isPinned: Boolean = false,
    
    @Field("reading_time")
    val readingTime: Int = 0, // in minutes
    
    @Field("published_at")
    val publishedAt: LocalDateTime? = null,
    
    @Field("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Field("updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
