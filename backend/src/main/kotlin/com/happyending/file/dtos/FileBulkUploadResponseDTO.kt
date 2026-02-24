package com.happyending.file.dtos

data class FileBulkUploadResponseDTO(
    val successfulUploads: List<FileDTO>,
    val failedUploads: List<FailedUploadDTO>,
    val totalFiles: Int,
    val successCount: Int,
    val failureCount: Int
)
