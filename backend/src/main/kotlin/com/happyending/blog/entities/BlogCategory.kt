package com.happyending.blog.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime

@Document(collection = "blog_categories")
data class BlogCategory(
    @Id
    val id: String? = null,
    
    @Field("name")
    val name: String,
    
    @Field("slug")
    val slug: String,
    
    @Field("description")
    val description: String? = null,
    
    @Field("parent_id")
    val parentId: String? = null, // For hierarchical categories
    
    @Field("color")
    val color: String? = null, // Hex color for UI
    
    @Field("icon")
    val icon: String? = null, // Icon class or URL
    
    @Field("is_active")
    val isActive: Boolean = true,
    
    @Field("sort_order")
    val sortOrder: Int = 0,
    
    @Field("blog_count")
    val blogCount: Long = 0,
    
    @Field("meta_title")
    val metaTitle: String? = null,
    
    @Field("meta_description")
    val metaDescription: String? = null,
    
    @Field("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Field("updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

