package com.happyending.mail.dtos

import java.time.LocalDateTime

data class EmailDTO(
    val id: String? = null,
    val to: String,
    val cc: List<String> = emptyList(),
    val bcc: List<String> = emptyList(),
    val subject: String,
    val content: String,
    val isHtml: Boolean = true,
    val templateName: String? = null,
    val templateData: Map<String, Any> = emptyMap(),
    val attachments: List<EmailAttachmentDTO> = emptyList(),
    val sentAt: LocalDateTime? = null,
    val status: EmailStatus = EmailStatus.PENDING
)

data class EmailAttachmentDTO(
    val fileName: String,
    val contentType: String,
    val content: ByteArray,
    val size: Long
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EmailAttachmentDTO

        if (fileName != other.fileName) return false
        if (contentType != other.contentType) return false
        if (!content.contentEquals(other.content)) return false
        if (size != other.size) return false

        return true
    }

    override fun hashCode(): Int {
        var result = fileName.hashCode()
        result = 31 * result + contentType.hashCode()
        result = 31 * result + content.contentHashCode()
        result = 31 * result + size.hashCode()
        return result
    }
}

data class EmailTemplateDTO(
    val templateName: String,
    val subject: String,
    val content: String,
    val variables: List<String> = emptyList()
)

data class BulkEmailDTO(
    val recipients: List<String>,
    val subject: String,
    val content: String,
    val isHtml: Boolean = true,
    val templateName: String? = null,
    val templateData: Map<String, Any> = emptyMap()
)

enum class EmailStatus {
    PENDING,    // Chờ gửi
    SENT,        // Đã gửi thành công
    FAILED,     // Gửi thất bại
    DELIVERED,  // Đã giao đến inbox
    BOUNCED     // Email bị trả về
}
