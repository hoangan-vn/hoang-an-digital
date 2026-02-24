package com.happyending.file.dtos

data class FailedUploadDTO(
    val originalName: String,
    val error: String,
    val reason: String
)
