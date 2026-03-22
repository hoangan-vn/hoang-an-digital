package com.main.server.application.usecase.storage

import com.main.server.application.port.StoragePort
import com.main.server.domain.entities.storage.FileEntity
import org.springframework.stereotype.Service

@Service
class MediaStorageService(
  private val storagePort: StoragePort,
) : MediaStorageUseCase {
  override fun upload(command: UploadMediaCommand): FileEntity =
    storagePort.upload(
      bytes = command.bytes,
      contentType = command.contentType,
      originalFilename = command.originalFilename,
      fileName = command.fileName,
      folder = command.folder,
      publicId = command.publicId,
      overwrite = command.overwrite,
    )

  override fun get(publicId: String): FileEntity = storagePort.get(publicId)

  override fun delete(publicId: String): Boolean = storagePort.delete(publicId)
}
