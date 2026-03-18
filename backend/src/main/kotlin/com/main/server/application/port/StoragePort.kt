package com.main.server.application.port

import com.main.server.domain.entities.storage.FileEntity
import org.springframework.stereotype.Repository
import org.springframework.web.multipart.MultipartFile

@Repository
interface StoragePort {
  fun upload(
    file: MultipartFile,
    fileName: String,
    folder: String?,
    publicId: String?,
  ): FileEntity

  fun delete(publicId: String): Boolean

  fun get(publicId: String): FileEntity
}