package com.happyending.mail.services

import com.happyending.mail.dtos.*
import com.happyending.mail.entities.Email
import com.happyending.mail.entities.EmailStatus
import com.happyending.mail.mapper.EmailMapper
import com.happyending.mail.repositories.EmailRepository
import org.slf4j.LoggerFactory
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture
import javax.mail.internet.MimeMessage

@Service
class MailService(
    private val javaMailSender: JavaMailSender,
    private val emailRepository: EmailRepository,
    private val emailMapper: EmailMapper,
    private val emailTemplateService: EmailTemplateService
) {
    
    private val logger = LoggerFactory.getLogger(MailService::class.java)
    
    fun sendEmail(emailDTO: EmailDTO): CompletableFuture<EmailDTO> {
        logger.info("Sending email to: ${emailDTO.to}")
        
        return CompletableFuture.supplyAsync {
            try {
                // Save email to database first
                val email = emailMapper.toEntity(emailDTO)
                val savedEmail = emailRepository.save(email)
                
                // Send email
                val mimeMessage = createMimeMessage(savedEmail)
                javaMailSender.send(mimeMessage)
                
                // Update status to SENT
                val updatedEmail = savedEmail.copy(
                    status = EmailStatus.SENT,
                    sentAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                )
                val finalEmail = emailRepository.save(updatedEmail)
                
                logger.info("Email sent successfully to: ${emailDTO.to}")
                emailMapper.toDTO(finalEmail)
                
            } catch (e: Exception) {
                logger.error("Failed to send email to ${emailDTO.to}: ${e.message}", e)
                
                // Update status to FAILED
                val failedEmail = emailRepository.findById(emailDTO.id ?: "").orElse(null)
                if (failedEmail != null) {
                    val updatedEmail = failedEmail.copy(
                        status = EmailStatus.FAILED,
                        errorMessage = e.message,
                        updatedAt = LocalDateTime.now()
                    )
                    emailRepository.save(updatedEmail)
                }
                
                throw e
            }
        }
    }
    
    fun sendBulkEmail(bulkEmailDTO: BulkEmailDTO): CompletableFuture<List<EmailDTO>> {
        logger.info("Sending bulk email to ${bulkEmailDTO.recipients.size} recipients")
        
        return CompletableFuture.supplyAsync {
            val results = mutableListOf<EmailDTO>()
            
            bulkEmailDTO.recipients.forEach { recipient ->
                try {
                    val emailDTO = EmailDTO(
                        to = recipient,
                        subject = bulkEmailDTO.subject,
                        content = bulkEmailDTO.content,
                        isHtml = bulkEmailDTO.isHtml,
                        templateName = bulkEmailDTO.templateName,
                        templateData = bulkEmailDTO.templateData
                    )
                    
                    val result = sendEmail(emailDTO).get()
                    results.add(result)
                    
                } catch (e: Exception) {
                    logger.error("Failed to send bulk email to $recipient: ${e.message}", e)
                    // Continue with other recipients
                }
            }
            
            logger.info("Bulk email completed. Sent: ${results.size}/${bulkEmailDTO.recipients.size}")
            results
        }
    }
    
    fun sendWelcomeEmail(to: String, userName: String, activationLink: String): CompletableFuture<EmailDTO> {
        logger.info("Sending welcome email to: $to")
        
        val content = emailTemplateService.getWelcomeEmailContent(userName, activationLink)
        val emailDTO = EmailDTO(
            to = to,
            subject = "Welcome to Happy Ending! Please activate your account",
            content = content,
            isHtml = true,
            templateName = "welcome"
        )
        
        return sendEmail(emailDTO)
    }
    
    fun sendPasswordResetEmail(to: String, userName: String, resetLink: String): CompletableFuture<EmailDTO> {
        logger.info("Sending password reset email to: $to")
        
        val content = emailTemplateService.getPasswordResetContent(userName, resetLink)
        val emailDTO = EmailDTO(
            to = to,
            subject = "Password Reset Request - Happy Ending",
            content = content,
            isHtml = true,
            templateName = "password-reset"
        )
        
        return sendEmail(emailDTO)
    }
    
    fun sendOrderConfirmationEmail(
        to: String,
        userName: String,
        orderNumber: String,
        orderDate: String,
        totalAmount: String,
        items: List<Map<String, Any>>
    ): CompletableFuture<EmailDTO> {
        logger.info("Sending order confirmation email to: $to")
        
        val content = emailTemplateService.getOrderConfirmationContent(
            userName, orderNumber, orderDate, totalAmount, items
        )
        val emailDTO = EmailDTO(
            to = to,
            subject = "Order Confirmation #$orderNumber - Happy Ending",
            content = content,
            isHtml = true,
            templateName = "order-confirmation"
        )
        
        return sendEmail(emailDTO)
    }
    
    fun sendNotificationEmail(
        to: String,
        title: String,
        message: String,
        actionUrl: String? = null,
        actionText: String? = null
    ): CompletableFuture<EmailDTO> {
        logger.info("Sending notification email to: $to")
        
        val content = emailTemplateService.getNotificationContent(title, message, actionUrl, actionText)
        val emailDTO = EmailDTO(
            to = to,
            subject = title,
            content = content,
            isHtml = true,
            templateName = "notification"
        )
        
        return sendEmail(emailDTO)
    }
    
    fun sendEmailWithAttachment(
        to: String,
        subject: String,
        content: String,
        attachments: List<MultipartFile>
    ): CompletableFuture<EmailDTO> {
        logger.info("Sending email with attachments to: $to")
        
        val attachmentDTOs = attachments.map { file ->
            EmailAttachmentDTO(
                fileName = file.originalFilename ?: "attachment",
                contentType = file.contentType ?: "application/octet-stream",
                content = file.bytes,
                size = file.size
            )
        }
        
        val emailDTO = EmailDTO(
            to = to,
            subject = subject,
            content = content,
            isHtml = true,
            attachments = attachmentDTOs
        )
        
        return sendEmail(emailDTO)
    }
    
    private fun createMimeMessage(email: Email): MimeMessage {
        val mimeMessage = javaMailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage, true, "UTF-8")
        
        helper.setTo(email.to)
        if (email.cc.isNotEmpty()) {
            helper.setCc(email.cc.toTypedArray())
        }
        if (email.bcc.isNotEmpty()) {
            helper.setBcc(email.bcc.toTypedArray())
        }
        helper.setSubject(email.subject)
        helper.setText(email.content, email.isHtml)
        
        // Add attachments
        email.attachments.forEach { attachment ->
            helper.addAttachment(
                attachment.fileName,
                { attachment.content.inputStream() },
                attachment.contentType
            )
        }
        
        return mimeMessage
    }
    
    fun getEmailHistory(to: String, days: Int = 30): List<EmailDTO> {
        logger.info("Getting email history for: $to")
        
        val endTime = LocalDateTime.now()
        val startTime = endTime.minusDays(days.toLong())
        
        val emails = emailRepository.findByToAndCreatedAtBetween(to, startTime, endTime)
        return emailMapper.toDTOList(emails)
    }
    
    fun getPendingEmails(): List<EmailDTO> {
        logger.info("Getting pending emails")
        
        val emails = emailRepository.findPendingEmails()
        return emailMapper.toDTOList(emails)
    }
    
    fun getFailedEmails(hours: Int = 24): List<EmailDTO> {
        logger.info("Getting failed emails from last $hours hours")
        
        val startTime = LocalDateTime.now().minusHours(hours.toLong())
        val emails = emailRepository.findFailedEmailsAfter(startTime)
        return emailMapper.toDTOList(emails)
    }
    
    fun retryFailedEmails(): CompletableFuture<List<EmailDTO>> {
        logger.info("Retrying failed emails")
        
        return CompletableFuture.supplyAsync {
            val failedEmails = getFailedEmails(24)
            val results = mutableListOf<EmailDTO>()
            
            failedEmails.forEach { emailDTO ->
                try {
                    val result = sendEmail(emailDTO).get()
                    results.add(result)
                } catch (e: Exception) {
                    logger.error("Failed to retry email ${emailDTO.id}: ${e.message}", e)
                }
            }
            
            logger.info("Retry completed. Success: ${results.size}/${failedEmails.size}")
            results
        }
    }
    
    fun sendTemplateEmail(templateEmailDTO: TemplateEmailDTO): CompletableFuture<EmailDTO> {
        logger.info("Sending template email: ${templateEmailDTO.templateName} to ${templateEmailDTO.to}")
        
        return CompletableFuture.supplyAsync {
            try {
                // Process template
                val content = emailTemplateService.processTemplate(
                    templateEmailDTO.templateName,
                    templateEmailDTO.templateData
                )
                
                // Create email DTO
                val emailDTO = EmailDTO(
                    to = templateEmailDTO.to,
                    cc = templateEmailDTO.cc,
                    bcc = templateEmailDTO.bcc,
                    subject = templateEmailDTO.subject,
                    content = content,
                    isHtml = true,
                    templateName = templateEmailDTO.templateName,
                    templateData = templateEmailDTO.templateData,
                    attachments = templateEmailDTO.attachments
                )
                
                // Send email
                sendEmail(emailDTO).get()
                
            } catch (e: Exception) {
                logger.error("Failed to send template email: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun sendBulkTemplateEmail(bulkTemplateEmailDTO: BulkTemplateEmailDTO): CompletableFuture<List<EmailDTO>> {
        logger.info("Sending bulk template email: ${bulkTemplateEmailDTO.templateName} to ${bulkTemplateEmailDTO.recipients.size} recipients")
        
        return CompletableFuture.supplyAsync {
            val results = mutableListOf<EmailDTO>()
            
            bulkTemplateEmailDTO.recipients.forEach { recipient ->
                try {
                    val templateEmailDTO = TemplateEmailDTO(
                        to = recipient,
                        subject = bulkTemplateEmailDTO.subject,
                        templateName = bulkTemplateEmailDTO.templateName,
                        templateData = bulkTemplateEmailDTO.templateData
                    )
                    
                    val result = sendTemplateEmail(templateEmailDTO).get()
                    results.add(result)
                    
                } catch (e: Exception) {
                    logger.error("Failed to send bulk template email to $recipient: ${e.message}", e)
                    // Continue with other recipients
                }
            }
            
            logger.info("Bulk template email completed. Sent: ${results.size}/${bulkTemplateEmailDTO.recipients.size}")
            results
        }
    }
    
    fun getAvailableTemplates(): List<TemplateInfoDTO> {
        logger.info("Getting available email templates")
        
        return listOf(
            TemplateInfoDTO(
                templateName = "welcome",
                subject = "Welcome to Happy Ending!",
                description = "Welcome email for new users",
                requiredVariables = listOf("userName", "activationLink"),
                optionalVariables = listOf("currentYear"),
                exampleData = mapOf(
                    "userName" to "John Doe",
                    "activationLink" to "https://app.com/activate?token=abc123",
                    "currentYear" to 2024
                )
            ),
            TemplateInfoDTO(
                templateName = "password-reset",
                subject = "Password Reset Request - Happy Ending",
                description = "Password reset email for users",
                requiredVariables = listOf("userName", "resetLink"),
                optionalVariables = listOf("currentYear"),
                exampleData = mapOf(
                    "userName" to "John Doe",
                    "resetLink" to "https://app.com/reset?token=xyz789",
                    "currentYear" to 2024
                )
            ),
            TemplateInfoDTO(
                templateName = "order-confirmation",
                subject = "Order Confirmation #{{orderNumber}} - Happy Ending",
                description = "Order confirmation email for customers",
                requiredVariables = listOf("userName", "orderNumber", "orderDate", "totalAmount", "items"),
                optionalVariables = listOf("currentYear"),
                exampleData = mapOf(
                    "userName" to "John Doe",
                    "orderNumber" to "ORD-123",
                    "orderDate" to "2024-01-15",
                    "totalAmount" to "99.99",
                    "items" to listOf(
                        mapOf("name" to "Product 1", "quantity" to 2, "price" to "25.00", "total" to "50.00"),
                        mapOf("name" to "Product 2", "quantity" to 1, "price" to "49.99", "total" to "49.99")
                    ),
                    "currentYear" to 2024
                )
            ),
            TemplateInfoDTO(
                templateName = "notification",
                subject = "{{title}} - Happy Ending",
                description = "General notification email",
                requiredVariables = listOf("title", "message"),
                optionalVariables = listOf("actionUrl", "actionText", "currentYear"),
                exampleData = mapOf(
                    "title" to "System Maintenance",
                    "message" to "We will perform maintenance tonight from 2-4 AM",
                    "actionUrl" to "https://app.com/status",
                    "actionText" to "Check Status",
                    "currentYear" to 2024
                )
            ),
            TemplateInfoDTO(
                templateName = "newsletter",
                subject = "{{title}} - Happy Ending Newsletter",
                description = "Newsletter email template",
                requiredVariables = listOf("title", "content", "articles"),
                optionalVariables = listOf("unsubscribeLink", "currentYear"),
                exampleData = mapOf(
                    "title" to "Weekly Newsletter",
                    "content" to "Check out our latest updates and offers!",
                    "articles" to listOf(
                        mapOf("title" to "New Products", "summary" to "Discover our latest arrivals"),
                        mapOf("title" to "Special Offers", "summary" to "Don't miss our exclusive deals")
                    ),
                    "unsubscribeLink" to "https://app.com/unsubscribe",
                    "currentYear" to 2024
                )
            ),
            TemplateInfoDTO(
                templateName = "promotion",
                subject = "{{promoTitle}} - Limited Time Offer!",
                description = "Promotional email template",
                requiredVariables = listOf("promoTitle", "discount", "validUntil", "promoCode"),
                optionalVariables = listOf("productImage", "currentYear"),
                exampleData = mapOf(
                    "promoTitle" to "Black Friday Sale",
                    "discount" to "50% OFF",
                    "validUntil" to "2024-01-31",
                    "promoCode" to "BLACKFRIDAY50",
                    "productImage" to "https://example.com/image.jpg",
                    "currentYear" to 2024
                )
            )
        )
    }
}
