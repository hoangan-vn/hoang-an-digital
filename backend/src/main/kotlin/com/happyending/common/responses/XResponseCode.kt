package com.happyending.common.responses

enum class XResponseCode(
    val code: String,
    val message: String
) {
    SUCCESS("0000", "Success"),
    CREATED("0001", "Created"),

    BAD_REQUEST("E400", "Bad Request"),
    UNAUTHORIZED("E401", "Unauthorized"),
    FORBIDDEN("E403", "Forbidden"),
    NOT_FOUND("E404", "Not Found"),

    INTERNAL_SERVER_ERROR("E500", "Internal Server Error")
}