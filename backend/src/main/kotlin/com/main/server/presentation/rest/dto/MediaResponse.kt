package com.main.server.presentation.rest.dto

data class MediaResponse(
  val name: String,
  val url: String,
  val folder: String,
  val publicId: String,
)
