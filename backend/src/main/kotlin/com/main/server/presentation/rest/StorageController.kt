package com.main.server.presentation.rest

import com.main.server.application.usecase.storage.MediaStorageUseCase
import com.main.server.application.usecase.storage.UploadMediaCommand
import com.main.server.presentation.rest.dto.DeleteMediaResponse
import com.main.server.presentation.rest.dto.MediaResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/media")
class StorageController(
  private val mediaStorageUseCase: MediaStorageUseCase,
) {
  @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
  fun upload(
    @RequestPart("file") file: MultipartFile,
    @RequestParam(required = false) folder: String?,
    @RequestParam(required = false) publicId: String?,
    @RequestParam(required = false, defaultValue = "false") overwrite: Boolean,
  ): ResponseEntity<MediaResponse> {
    val command =
      UploadMediaCommand(
        bytes = file.bytes,
        contentType = file.contentType,
        originalFilename = file.originalFilename,
        fileName = file.originalFilename ?: "uploaded-file",
        folder = folder,
        publicId = publicId,
        overwrite = overwrite,
      )
    val uploaded = mediaStorageUseCase.upload(command)
    return ResponseEntity.status(HttpStatus.CREATED).body(uploaded.toResponse())
  }

  @GetMapping
  fun get(
    @RequestParam publicId: String,
  ): MediaResponse = mediaStorageUseCase.get(publicId).toResponse()

  @DeleteMapping
  fun delete(
    @RequestParam publicId: String,
  ): DeleteMediaResponse =
    DeleteMediaResponse(
      publicId = publicId,
      deleted = mediaStorageUseCase.delete(publicId),
    )
}

private fun com.main.server.domain.entities.storage.FileEntity.toResponse(): MediaResponse =
  MediaResponse(
    name = name,
    url = url,
    folder = folder,
    publicId = publicId,
  )
