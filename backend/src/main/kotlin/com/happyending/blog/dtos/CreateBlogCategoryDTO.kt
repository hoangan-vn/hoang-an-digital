package com.happyending.blog.dtos

data class CreateBlogCategoryDTO(
    val name: String,
    val description: String? = null,
    val parentId: String? = null,
    val color: String? = null,
    val icon: String? = null,
    val sortOrder: Int = 0,
    val metaTitle: String? = null,
    val metaDescription: String? = null
)
