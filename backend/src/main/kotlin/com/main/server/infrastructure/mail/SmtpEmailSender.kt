package com.main.server.infrastructure.mail

import com.main.server.domain.mail.EmailMessage
import com.main.server.domain.mail.EmailSender
import com.main.server.infrastructure.config.properties.MailProperties
import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component

@Component
class SmtpEmailSender(
    private val mailSender: JavaMailSender,
    private val props: MailProperties,
) : EmailSender {
    override fun send(message: EmailMessage) {
        require(message.text != null || message.html != null) { "Email must contain text or html content" }

        val mimeMessage: MimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage, "UTF-8")
        helper.setFrom(props.from)
        helper.setTo(message.to)
        helper.setSubject(message.subject)

        val html = message.html
        val text = message.text
        if (html != null) {
            helper.setText(html, true)
        } else {
            helper.setText(requireNotNull(text), false)
        }

        mailSender.send(mimeMessage)
    }
}

