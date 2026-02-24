package com.happyending.mail.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime

@Document(collection = "emails")
data class Email(
    @Id
    val id: String? = null,
    
    @Field("to_email")
    val to: String,
    
    @Field("cc_emails")
    val cc: List<String> = emptyList(),
    
    @Field("bcc_emails")
    val bcc: List<String> = emptyList(),
    
    @Field("subject")
    val subject: String,
    
    @Field("content")
    val content: String,
    
    @Field("is_html")
    val isHtml: Boolean = true,
    
    @Field("template_name")
    val templateName: String? = null,
    
    @Field("template_data")
    val templateData: Map<String, Any> = emptyMap(),
    
    @Field("attachments")
    val attachments: List<EmailAttachment> = emptyList(),
    
    @Field("sent_at")
    val sentAt: LocalDateTime? = null,
    
    @Field("status")
    val status: EmailStatus = EmailStatus.PENDING,
    
    @Field("error_message")
    val errorMessage: String? = null,
    
    @Field("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Field("updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

data class EmailAttachment(
    val fileName: String,
    val contentType: String,
    val content: ByteArray,
    val size: Long
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EmailAttachment

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

enum class EmailStatus {
    PENDING,    // Chờ gửi
    SENT,        // Đã gửi thành công
    FAILED,     // Gửi thất bại
    DELIVERED,  // Đã giao đến inbox
    BOUNCED     // Email bị trả về
}
