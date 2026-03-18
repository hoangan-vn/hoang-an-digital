package com.main.server.domain.mail

data class EmailMessage(
    val to: String,
    val subject: String,
    val text: String? = null,
    val html: String? = null,
)

interface EmailSender {
    fun send(message: EmailMessage)
}

