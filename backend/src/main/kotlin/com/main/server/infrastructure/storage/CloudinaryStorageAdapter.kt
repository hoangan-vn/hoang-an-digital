package com.main.server.infrastructure.storage

import com.cloudinary.Cloudinary
import com.main.server.domain.entities.storage.FileEntity
import com.main.server.application.port.StoragePort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException

@Repository
class CloudinaryStorageAdapter(
  private val cloudinary: Cloudinary?,
): StoragePort {
  companion object {
    private val supportedResourceTypes = setOf("image", "video", "raw")
  }

  override fun upload(
    file: MultipartFile,
    fileName: String,
    folder: String?,
    publicId: String?,
  ): FileEntity {
    if (file.isEmpty) {
      throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Uploaded file must not be empty.")
    }

    val options =
      mutableMapOf<String, Any>(
        "resource_type" to resolveResourceType(file.contentType),
        "overwrite" to overwrite,
        "use_filename" to true,
        "unique_filename" to publicId.isNullOrBlank(),
      )

    folder?.trim()?.takeIf { it.isNotBlank() }?.let { options["folder"] = it.trim('/') }
    publicId?.trim()?.takeIf { it.isNotBlank() }?.let { options["public_id"] = it }
    file.originalFilename?.let { options["filename_override"] = it }

    return try {
      requireCloudinary().uploader().upload(file.bytes, options).toMediaAsset()
    } catch (exception: Exception) {
      throw ResponseStatusException(HttpStatus.BAD_GATEWAY, "Failed to upload asset to Cloudinary.", exception)
    }
  }

  override fun get(
    publicId: String,
  ): FileEntity {
    val normalizedPublicId = publicId.trim()
    if (normalizedPublicId.isBlank()) {
      throw ResponseStatusException(HttpStatus.BAD_REQUEST, "publicId must not be blank.")
    }

    val normalizedResourceType = normalizeResourceType(resourceType)

    return try {
      requireCloudinary()
        .api()
        .resource(normalizedPublicId, mapOf("resource_type" to normalizedResourceType))
        .toMediaAsset()
    } catch (exception: Exception) {
      if (exception.message?.contains("not found", ignoreCase = true) == true) {
        throw ResponseStatusException(HttpStatus.NOT_FOUND, "Cloudinary asset '$normalizedPublicId' was not found.")
      }
      throw ResponseStatusException(
        HttpStatus.BAD_GATEWAY,
        "Failed to fetch asset '$normalizedPublicId' from Cloudinary.",
        exception,
      )
    }
  }

  override fun delete(
    publicId: String,
  ): Boolean {
    val normalizedPublicId = publicId.trim()
    if (normalizedPublicId.isBlank()) {
      throw ResponseStatusException(HttpStatus.BAD_REQUEST, "publicId must not be blank.")
    }

    val normalizedResourceType = normalizeResourceType(resourceType)

    return try {
      val result =
        requireCloudinary().uploader().destroy(
          normalizedPublicId,
          mapOf(
            "resource_type" to normalizedResourceType,
            "invalidate" to true,
          ),
        )

      val status = result["result"]?.toString().orEmpty()
      CloudinaryDeleteResult(
        publicId = normalizedPublicId,
        deleted = status.equals("ok", ignoreCase = true),
        result = status.ifBlank { "unknown" },
      )
    } catch (exception: Exception) {
      throw ResponseStatusException(
        HttpStatus.BAD_GATEWAY,
        "Failed to delete asset '$normalizedPublicId' from Cloudinary.",
        exception,
      )
    }
  }

  private fun requireCloudinary(): Cloudinary =
    cloudinary
      ?: throw ResponseStatusException(
        HttpStatus.SERVICE_UNAVAILABLE,
        "Cloudinary is not configured. Please set app.cloudinary credentials.",
      )

  private fun normalizeResourceType(resourceType: String?): String {
    val normalized = resourceType?.trim()?.lowercase().orEmpty()
    if (normalized.isBlank()) return "image"
    if (normalized in supportedResourceTypes) return normalized

    throw ResponseStatusException(
      HttpStatus.BAD_REQUEST,
      "resourceType must be one of: image, video, raw.",
    )
  }

  private fun resolveResourceType(contentType: String?): String =
    when {
      contentType.isNullOrBlank() -> "auto"
      contentType.startsWith("image/") -> "image"
      contentType.startsWith("video/") -> "video"
      else -> "raw"
    }

  private fun Map<*, *>.toMediaAsset(): CloudinaryMediaAsset =
    CloudinaryMediaAsset(
      assetId = stringValue("asset_id"),
      publicId =
        stringValue("public_id")
          ?: throw ResponseStatusException(HttpStatus.BAD_GATEWAY, "Cloudinary response is missing public_id."),
      resourceType = stringValue("resource_type") ?: "image",
      format = stringValue("format"),
      url = stringValue("url"),
      secureUrl = stringValue("secure_url"),
      originalFilename = stringValue("original_filename"),
      bytes = longValue("bytes"),
      width = intValue("width"),
      height = intValue("height"),
      folder = stringValue("folder"),
      createdAt = stringValue("created_at"),
    )

  private fun Map<*, *>.stringValue(key: String): String? = this[key]?.toString()

  private fun Map<*, *>.intValue(key: String): Int? = (this[key] as? Number)?.toInt()

  private fun Map<*, *>.longValue(key: String): Long? = (this[key] as? Number)?.toLong()
}
