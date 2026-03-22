package com.main.server.domain.exception

sealed class DomainException(
  message: String,
  cause: Throwable? = null,
) : RuntimeException(message, cause) {
  class BadRequest(message: String) : DomainException(message)

  class NotFound(message: String) : DomainException(message)

  class Unavailable(message: String) : DomainException(message)

  class UpstreamFailure(
    message: String,
    cause: Throwable? = null,
  ) : DomainException(message, cause)
}
