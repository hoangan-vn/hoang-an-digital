package com.main.server.infrastructure.persistence.mongo.media

import com.main.server.domain.media.MediaAsset
import com.main.server.domain.media.MediaAssetRepository
import org.springframework.stereotype.Repository

@Repository
class MediaAssetRepositoryAdapter(
    private val mongo: MediaAssetMongoRepository,
) : MediaAssetRepository {
    override fun save(asset: MediaAsset): MediaAsset =
        mongo.save(asset.toDocument()).toDomain()

    override fun findById(id: String): MediaAsset? =
        mongo.findById(id).orElse(null)?.toDomain()

    override fun findAll(): List<MediaAsset> =
        mongo.findAll().map { it.toDomain() }

    override fun deleteById(id: String) {
        mongo.deleteById(id)
    }
}

private fun MediaAssetDocument.toDomain(): MediaAsset =
    MediaAsset(
        id = id,
        publicId = publicId,
        url = url,
        secureUrl = secureUrl,
        format = format,
        bytes = bytes,
        width = width,
        height = height,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

private fun MediaAsset.toDocument(): MediaAssetDocument =
    MediaAssetDocument(
        id = id,
        publicId = publicId,
        url = url,
        secureUrl = secureUrl,
        format = format,
        bytes = bytes,
        width = width,
        height = height,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

