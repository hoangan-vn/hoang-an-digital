package com.main.server.presentation.rest

import com.main.server.domain.exception.DomainException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
  @ExceptionHandler(DomainException.BadRequest::class)
  fun handleBadRequest(exception: DomainException.BadRequest): ResponseEntity<ErrorResponse> =
    ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse(exception.message ?: "Bad request"))

  @ExceptionHandler(DomainException.NotFound::class)
  fun handleNotFound(exception: DomainException.NotFound): ResponseEntity<ErrorResponse> =
    ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse(exception.message ?: "Not found"))

  @ExceptionHandler(DomainException.Unavailable::class)
  fun handleUnavailable(exception: DomainException.Unavailable): ResponseEntity<ErrorResponse> =
    ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ErrorResponse(exception.message ?: "Service unavailable"))

  @ExceptionHandler(DomainException.UpstreamFailure::class)
  fun handleUpstreamFailure(exception: DomainException.UpstreamFailure): ResponseEntity<ErrorResponse> =
    ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(ErrorResponse(exception.message ?: "Upstream service failed"))
}

data class ErrorResponse(
  val message: String,
)
