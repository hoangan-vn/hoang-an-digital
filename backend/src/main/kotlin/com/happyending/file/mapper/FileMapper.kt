package com.happyending.file.mapper

import com.fasterxml.jackson.databind.ObjectMapper
import com.happyending.file.dtos.*
import com.happyending.file.entities.File
import org.springframework.stereotype.Component

@Component
class FileMapper(private val objectMapper: ObjectMapper) {

    fun toEntity(dto: CreateFileDTO, cloudinaryResult: Map<String, Any>): File {
        return File(
            originalName = dto.originalName,
            fileName = generateFileName(dto.originalName),
            publicId = cloudinaryResult["public_id"] as String,
            url = cloudinaryResult["url"] as String,
            secureUrl = cloudinaryResult["secure_url"] as String,
            format = cloudinaryResult["format"] as String,
            resourceType = cloudinaryResult["resource_type"] as String,
            fileSize = (cloudinaryResult["bytes"] as Number).toLong(),
            width = cloudinaryResult["width"] as? Int,
            height = cloudinaryResult["height"] as? Int,
            mimeType = getMimeTypeFromFormat(cloudinaryResult["format"] as String),
            uploaderId = dto.uploaderId,
            uploaderName = dto.uploaderName,
            uploaderEmail = dto.uploaderEmail,
            folder = dto.folder,
            tags = dto.tags,
            isPublic = dto.isPublic,
            accessMode = dto.accessMode,
            description = dto.description,
            altText = dto.altText,
            metadata = dto.metadata
        )
    }

    fun toDTO(entity: File): FileDTO {
        return FileDTO(
            id = entity.id,
            originalName = entity.originalName,
            fileName = entity.fileName,
            publicId = entity.publicId,
            url = entity.url,
            secureUrl = entity.secureUrl,
            format = entity.format,
            resourceType = entity.resourceType,
            fileSize = entity.fileSize,
            width = entity.width,
            height = entity.height,
            mimeType = entity.mimeType,
            uploaderId = entity.uploaderId ?: "",
            uploaderName = entity.uploaderName ?: "",
            uploaderEmail = entity.uploaderEmail ?: "",
            folder = entity.folder,
            tags = entity.tags,
            isPublic = entity.isPublic,
            accessMode = entity.accessMode,
            downloadCount = entity.downloadCount,
            viewCount = entity.viewCount,
            isFeatured = entity.isFeatured,
            description = entity.description,
            altText = entity.altText,
            metadata = entity.metadata,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    fun toDTOList(entities: List<File>): List<FileDTO> {
        return entities.map { toDTO(it) }
    }

    fun updateEntity(entity: File, dto: UpdateFileDTO): File {
        return entity.copy(
            fileName = dto.fileName ?: entity.fileName,
            tags = dto.tags ?: entity.tags,
            isPublic = dto.isPublic ?: entity.isPublic,
            accessMode = dto.accessMode ?: entity.accessMode,
            isFeatured = dto.isFeatured ?: entity.isFeatured,
            description = dto.description ?: entity.description,
            altText = dto.altText ?: entity.altText,
            metadata = dto.metadata ?: entity.metadata,
            updatedAt = java.time.LocalDateTime.now()
        )
    }

    fun toUploadResponse(file: FileDTO, uploadUrl: String, deleteUrl: String): FileUploadResponseDTO {
        return FileUploadResponseDTO(
            file = file,
            uploadUrl = uploadUrl,
            deleteUrl = deleteUrl,
            message = "File uploaded successfully"
        )
    }
    
    fun toBulkUploadResponse(
        successfulUploads: List<FileDTO>,
        failedUploads: List<FailedUploadDTO>
    ): FileBulkUploadResponseDTO {
        return FileBulkUploadResponseDTO(
            successfulUploads = successfulUploads,
            failedUploads = failedUploads,
            totalFiles = successfulUploads.size + failedUploads.size,
            successCount = successfulUploads.size,
            failureCount = failedUploads.size
        )
    }

    fun toFailedUpload(originalName: String, error: Exception): FailedUploadDTO {
        return FailedUploadDTO(
            originalName = originalName,
            error = error.message ?: "Unknown error",
            reason = error.javaClass.simpleName
        )
    }

    // Helper methods
    private fun generateFileName(originalName: String): String {
        val timestamp = System.currentTimeMillis()
        val extension = originalName.substringAfterLast('.')
        val nameWithoutExtension = originalName.substringBeforeLast('.')
        return "${nameWithoutExtension}_${timestamp}.${extension}"
    }

    private fun getMimeTypeFromFormat(format: String): String {
        return when (format.lowercase()) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "gif" -> "image/gif"
            "webp" -> "image/webp"
            "svg" -> "image/svg+xml"
            "mp4" -> "video/mp4"
            "avi" -> "video/avi"
            "mov" -> "video/quicktime"
            "wmv" -> "video/x-ms-wmv"
            "flv" -> "video/x-flv"
            "webm" -> "video/webm"
            "pdf" -> "application/pdf"
            "doc" -> "application/msword"
            "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            "xls" -> "application/vnd.ms-excel"
            "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            "ppt" -> "application/vnd.ms-powerpoint"
            "pptx" -> "application/vnd.openxmlformats-officedocument.presentationml.presentation"
            "txt" -> "text/plain"
            "rtf" -> "application/rtf"
            "zip" -> "application/zip"
            "rar" -> "application/x-rar-compressed"
            "7z" -> "application/x-7z-compressed"
            "mp3" -> "audio/mpeg"
            "wav" -> "audio/wav"
            "ogg" -> "audio/ogg"
            "aac" -> "audio/aac"
            "flac" -> "audio/flac"
            else -> "application/octet-stream"
        }
    }
}




