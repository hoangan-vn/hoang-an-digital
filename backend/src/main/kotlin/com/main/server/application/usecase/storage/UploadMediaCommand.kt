package com.main.server.application.usecase.storage

data class UploadMediaCommand(
  val bytes: ByteArray,
  val contentType: String?,
  val originalFilename: String?,
  val fileName: String,
  val folder: String?,
  val publicId: String?,
  val overwrite: Boolean,
)
