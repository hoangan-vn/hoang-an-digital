package com.main.server.infrastructure.cloudinary

import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.main.server.domain.media.MediaStorage
import com.main.server.domain.media.StoredMedia
import org.springframework.stereotype.Component

@Component
class CloudinaryMediaStorage(
    private val cloudinary: Cloudinary?,
) : MediaStorage {
    override fun uploadImage(
        bytes: ByteArray,
        contentType: String?,
        originalFilename: String?,
        folder: String?,
    ): StoredMedia {
        val c = requireNotNull(cloudinary) { "Cloudinary is not configured. Set app.cloudinary.*" }

        val options = mutableMapOf<String, Any>(
            "resource_type" to "image",
        )
        if (!folder.isNullOrBlank()) options["folder"] = folder
        if (!contentType.isNullOrBlank()) options["content_type"] = contentType
        if (!originalFilename.isNullOrBlank()) options["filename_override"] = originalFilename

        @Suppress("UNCHECKED_CAST")
        val result = c.uploader().upload(bytes, options as Map<String, Any>)

        return StoredMedia(
            publicId = result["public_id"] as String,
            url = result["url"] as String,
            secureUrl = result["secure_url"] as String,
            format = result["format"] as? String,
            bytes = (result["bytes"] as? Number)?.toLong(),
            width = (result["width"] as? Number)?.toInt(),
            height = (result["height"] as? Number)?.toInt(),
        )
    }

    override fun delete(publicId: String): Boolean {
        val c = requireNotNull(cloudinary) { "Cloudinary is not configured. Set app.cloudinary.*" }

        @Suppress("UNCHECKED_CAST")
        val result = c.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "image")) as Map<String, Any>
        return (result["result"] as? String) == "ok"
    }
}

