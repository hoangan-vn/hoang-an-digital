package com.main.server.presentation.rest.dto

data class DeleteMediaResponse(
  val publicId: String,
  val deleted: Boolean,
)
