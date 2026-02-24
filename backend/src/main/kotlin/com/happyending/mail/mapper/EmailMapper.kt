package com.happyending.mail.mapper

import com.happyending.mail.dtos.EmailAttachmentDTO
import com.happyending.mail.dtos.EmailDTO
import com.happyending.mail.entities.Email
import com.happyending.mail.entities.EmailAttachment
import org.springframework.stereotype.Component

@Component
class EmailMapper {
    
    fun toEntity(dto: EmailDTO): Email {
        return Email(
            id = dto.id,
            to = dto.to,
            cc = dto.cc,
            bcc = dto.bcc,
            subject = dto.subject,
            content = dto.content,
            isHtml = dto.isHtml,
            templateName = dto.templateName,
            templateData = dto.templateData,
            attachments = dto.attachments.map { toAttachmentEntity(it) },
            sentAt = dto.sentAt,
            status = dto.status
        )
    }
    
    fun toDTO(entity: Email): EmailDTO {
        return EmailDTO(
            id = entity.id,
            to = entity.to,
            cc = entity.cc,
            bcc = entity.bcc,
            subject = entity.subject,
            content = entity.content,
            isHtml = entity.isHtml,
            templateName = entity.templateName,
            templateData = entity.templateData,
            attachments = entity.attachments.map { toAttachmentDTO(it) },
            sentAt = entity.sentAt,
            status = entity.status
        )
    }
    
    fun toDTOList(entities: List<Email>): List<EmailDTO> {
        return entities.map { toDTO(it) }
    }
    
    private fun toAttachmentEntity(dto: EmailAttachmentDTO): EmailAttachment {
        return EmailAttachment(
            fileName = dto.fileName,
            contentType = dto.contentType,
            content = dto.content,
            size = dto.size
        )
    }
    
    private fun toAttachmentDTO(entity: EmailAttachment): EmailAttachmentDTO {
        return EmailAttachmentDTO(
            fileName = entity.fileName,
            contentType = entity.contentType,
            content = entity.content,
            size = entity.size
        )
    }
}
