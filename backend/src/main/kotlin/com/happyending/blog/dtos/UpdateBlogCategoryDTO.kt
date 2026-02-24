package com.happyending.blog.dtos

data class UpdateBlogCategoryDTO(
    val name: String? = null,
    val description: String? = null,
    val parentId: String? = null,
    val color: String? = null,
    val icon: String? = null,
    val isActive: Boolean? = null,
    val sortOrder: Int? = null,
    val metaTitle: String? = null,
    val metaDescription: String? = null
)
