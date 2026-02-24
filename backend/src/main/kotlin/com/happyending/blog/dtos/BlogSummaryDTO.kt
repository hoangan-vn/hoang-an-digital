package com.happyending.blog.dtos

data class BlogSummaryDTO(
    val totalBlogs: Long,
    val publishedBlogs: Long,
    val draftBlogs: Long,
    val archivedBlogs: Long,
    val totalViews: Long,
    val totalLikes: Long,
    val totalComments: Long,
    val averageReadingTime: Double
)
