package com.happyending.common.responses

data class XApiResponse<T>(
    val success: Boolean,
    val code: String,
    val message: String,
    val data: T? = null
) {
    companion object {
        fun <T> success(data: T): XApiResponse<T> {
            val responseCode = XResponseCode.SUCCESS
            return XApiResponse(true, responseCode.code, responseCode.message, data)
        }

        fun <T> success(data: T, message: String): XApiResponse<T> {
            val responseCode = XResponseCode.SUCCESS
            return XApiResponse(true, responseCode.code, message, data)
        }

        fun <T> created(data: T? = null): XApiResponse<T> {
            val responseCode = XResponseCode.CREATED
            return XApiResponse(true, responseCode.code, responseCode.message, data)
        }

        fun <T> error(responseCode: XResponseCode): XApiResponse<T> {
            return XApiResponse(false, responseCode.code, responseCode.message, null)
        }

        fun <T> error(responseCode: XResponseCode, message: String): XApiResponse<T> {
            return XApiResponse(false, responseCode.code, message, null)
        }

        fun <T> badRequest(message: String? = null): XApiResponse<T> {
            val responseCode = XResponseCode.BAD_REQUEST
            return XApiResponse(false, responseCode.code, message ?: responseCode.message, null)
        }

        fun <T> notFound(message: String? = null): XApiResponse<T> {
            val responseCode = XResponseCode.NOT_FOUND
            return XApiResponse(false, responseCode.code, message ?: responseCode.message, null)
        }

        fun <T> internalServerError(message: String? = null): XApiResponse<T> {
            val responseCode = XResponseCode.INTERNAL_SERVER_ERROR
            return XApiResponse(false, responseCode.code, message ?: responseCode.message, null)
        }
    }
}
