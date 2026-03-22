package com.main.server.infrastructure.storage

import com.cloudinary.Cloudinary
import com.main.server.application.port.StoragePort
import com.main.server.domain.entities.storage.FileEntity
import com.main.server.domain.exception.DomainException
import org.springframework.stereotype.Repository

@Repository
class CloudinaryStorageAdapter(
  private val cloudinary: Cloudinary?,
) : StoragePort {
  companion object {
    private val supportedResourceTypes = setOf("image", "video", "raw")
  }

  override fun upload(
    bytes: ByteArray,
    contentType: String?,
    originalFilename: String?,
    fileName: String,
    folder: String?,
    publicId: String?,
    overwrite: Boolean,
  ): FileEntity {
    if (bytes.isEmpty()) {
      throw DomainException.BadRequest("Uploaded file must not be empty.")
    }

    val options =
      mutableMapOf<String, Any>(
        "resource_type" to resolveResourceType(contentType),
        "overwrite" to overwrite,
        "use_filename" to true,
        "unique_filename" to publicId.isNullOrBlank(),
      )

    folder?.trim()?.takeIf { it.isNotBlank() }?.let { options["folder"] = it.trim('/') }
    publicId?.trim()?.takeIf { it.isNotBlank() }?.let { options["public_id"] = it }
    originalFilename?.let { options["filename_override"] = it }

    return try {
      requireCloudinary().uploader().upload(bytes, options).toFileEntity(fileName)
    } catch (exception: Exception) {
      throw DomainException.UpstreamFailure("Failed to upload asset to Cloudinary.", exception)
    }
  }

  override fun get(
    publicId: String,
  ): FileEntity {
    val normalizedPublicId = publicId.trim()
    if (normalizedPublicId.isBlank()) {
      throw DomainException.BadRequest("publicId must not be blank.")
    }

    return try {
      requireCloudinary()
        .api()
        .resource(normalizedPublicId, mapOf("resource_type" to "image"))
        .toFileEntity()
    } catch (exception: Exception) {
      if (exception.message?.contains("not found", ignoreCase = true) == true) {
        throw DomainException.NotFound("Cloudinary asset '$normalizedPublicId' was not found.")
      }
      throw DomainException.UpstreamFailure("Failed to fetch asset '$normalizedPublicId' from Cloudinary.", exception)
    }
  }

  override fun delete(
    publicId: String,
  ): Boolean {
    val normalizedPublicId = publicId.trim()
    if (normalizedPublicId.isBlank()) {
      throw DomainException.BadRequest("publicId must not be blank.")
    }

    return try {
      val result =
        requireCloudinary().uploader().destroy(
          normalizedPublicId,
          mapOf(
            "resource_type" to "image",
            "invalidate" to true,
          ),
        )

      val status = result["result"]?.toString().orEmpty()
      status.equals("ok", ignoreCase = true)
    } catch (exception: Exception) {
      throw DomainException.UpstreamFailure("Failed to delete asset '$normalizedPublicId' from Cloudinary.", exception)
    }
  }

  private fun requireCloudinary(): Cloudinary =
    cloudinary
      ?: throw DomainException.Unavailable("Cloudinary is not configured. Please set app.cloudinary credentials.")

  private fun resolveResourceType(contentType: String?): String =
    when {
      contentType.isNullOrBlank() -> "auto"
      contentType.startsWith("image/") -> "image"
      contentType.startsWith("video/") -> "video"
      else -> "raw"
    }

  private fun Map<*, *>.toFileEntity(fileNameFromRequest: String? = null): FileEntity {
    val publicId =
      stringValue("public_id")
        ?: throw DomainException.UpstreamFailure("Cloudinary response is missing public_id.")
    val url =
      stringValue("secure_url")
        ?: stringValue("url")
        ?: throw DomainException.UpstreamFailure("Cloudinary response is missing asset url.")
    val folder = stringValue("folder").orEmpty()
    val fileName = fileNameFromRequest?.takeIf { it.isNotBlank() } ?: publicId.substringAfterLast('/')
    return FileEntity(name = fileName, url = url, folder = folder, publicId = publicId)
  }

  private fun Map<*, *>.stringValue(key: String): String? = this[key]?.toString()

  private fun Map<*, *>.intValue(key: String): Int? = (this[key] as? Number)?.toInt()

  private fun Map<*, *>.longValue(key: String): Long? = (this[key] as? Number)?.toLong()
}
