package com.happyending.file.dtos

data class FileUploadResponseDTO(
    val file: FileDTO,
    val uploadUrl: String,
    val deleteUrl: String,
    val message: String
)
