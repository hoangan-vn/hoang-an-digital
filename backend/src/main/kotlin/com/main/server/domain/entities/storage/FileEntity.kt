package com.main.server.domain.entities.storage

data class FileEntity(
  val name: String,
  val url: String,
  val folder: String,
  val publicId: String,
)
