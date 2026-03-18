package com.main.server.domain.media

data class StoredMedia(
    val publicId: String,
    val url: String,
    val secureUrl: String,
    val format: String? = null,
    val bytes: Long? = null,
    val width: Int? = null,
    val height: Int? = null,
)

interface MediaStorage {
    fun uploadImage(
        bytes: ByteArray,
        contentType: String?,
        originalFilename: String?,
        folder: String? = null,
    ): StoredMedia

    fun delete(publicId: String): Boolean
}

