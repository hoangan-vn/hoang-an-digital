package com.main.server.domain.media

interface MediaAssetRepository {
    fun save(asset: MediaAsset): MediaAsset
    fun findById(id: String): MediaAsset?
    fun findAll(): List<MediaAsset>
    fun deleteById(id: String)
}

