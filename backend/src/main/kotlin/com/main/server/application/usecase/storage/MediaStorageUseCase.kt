package com.main.server.application.usecase.storage

import com.main.server.domain.entities.storage.FileEntity

interface MediaStorageUseCase {
  fun upload(command: UploadMediaCommand): FileEntity

  fun get(publicId: String): FileEntity

  fun delete(publicId: String): Boolean
}
