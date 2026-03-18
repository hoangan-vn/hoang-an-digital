package com.main.server.application.media

import com.main.server.domain.media.MediaAsset
import com.main.server.domain.media.MediaAssetRepository
import com.main.server.domain.media.MediaStorage
import org.springframework.stereotype.Service

@Service
class MediaAssetService(
    private val repo: MediaAssetRepository,
    private val storage: MediaStorage,
) {
    fun uploadAndCreate(
        bytes: ByteArray,
        contentType: String?,
        originalFilename: String?,
        folder: String? = "uploads",
    ): MediaAsset {
        val stored = storage.uploadImage(
            bytes = bytes,
            contentType = contentType,
            originalFilename = originalFilename,
            folder = folder,
        )

        return repo.save(
            MediaAsset(
                publicId = stored.publicId,
                url = stored.url,
                secureUrl = stored.secureUrl,
                format = stored.format,
                bytes = stored.bytes,
                width = stored.width,
                height = stored.height,
            ),
        )
    }

    fun get(id: String): MediaAsset? = repo.findById(id)
    fun list(): List<MediaAsset> = repo.findAll()

    fun delete(id: String) {
        val asset = repo.findById(id) ?: return
        storage.delete(asset.publicId)
        repo.deleteById(id)
    }
}

