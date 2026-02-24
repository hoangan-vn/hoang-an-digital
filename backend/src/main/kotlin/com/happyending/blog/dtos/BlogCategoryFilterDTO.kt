package com.happyending.blog.dtos

data class BlogCategoryFilterDTO(
    val parentId: String? = null,
    val isActive: Boolean? = null,
    val searchTerm: String? = null
)
