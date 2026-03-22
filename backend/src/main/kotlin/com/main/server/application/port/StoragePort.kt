package com.main.server.application.port

import com.main.server.domain.entities.storage.FileEntity

interface StoragePort {
  fun upload(
    bytes: ByteArray,
    contentType: String?,
    originalFilename: String?,
    fileName: String,
    folder: String?,
    publicId: String?,
    overwrite: Boolean,
  ): FileEntity

  fun delete(publicId: String): Boolean

  fun get(publicId: String): FileEntity
}