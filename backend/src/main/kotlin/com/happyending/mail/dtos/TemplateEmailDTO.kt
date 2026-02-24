package com.happyending.mail.dtos

data class TemplateEmailDTO(
    val to: String,
    val cc: List<String> = emptyList(),
    val bcc: List<String> = emptyList(),
    val subject: String,
    val templateName: String,
    val templateData: Map<String, Any> = emptyMap(),
    val attachments: List<EmailAttachmentDTO> = emptyList()
)

data class BulkTemplateEmailDTO(
    val recipients: List<String>,
    val subject: String,
    val templateName: String,
    val templateData: Map<String, Any> = emptyMap()
)

data class TemplateInfoDTO(
    val templateName: String,
    val subject: String,
    val description: String,
    val requiredVariables: List<String>,
    val optionalVariables: List<String> = emptyList(),
    val exampleData: Map<String, Any> = emptyMap()
)
