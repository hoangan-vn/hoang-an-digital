package com.happyending.file.dtos

data class FileSummaryDTO(
    val totalFiles: Long,
    val totalSize: Long,
    val imageFiles: Long,
    val videoFiles: Long,
    val documentFiles: Long,
    val audioFiles: Long,
    val publicFiles: Long,
    val privateFiles: Long,
    val featuredFiles: Long,
    val totalDownloads: Long,
    val totalViews: Long
)
