package com.happyending.mail.controllers

import com.happyending.common.responses.XApiResponse
import com.happyending.mail.dtos.*
import com.happyending.mail.services.MailService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/mail")
@Tag(name = "Mail Service", description = "API for sending emails")
class MailController(
    private val mailService: MailService
) {
    
    @PostMapping("/send")
    @Operation(summary = "Send email", description = "Send a single email")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Email sent successfully"),
            ApiResponse(responseCode = "400", description = "Invalid email data"),
            ApiResponse(responseCode = "500", description = "Failed to send email")
        ]
    )
    fun sendEmail(
        @RequestBody emailDTO: EmailDTO
    ): ResponseEntity<XApiResponse<EmailDTO>> {
        return try {
            val result = mailService.sendEmail(emailDTO).get()
            ResponseEntity.ok(XApiResponse.success(result, "Email sent successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to send email: ${e.message}"))
        }
    }
    
    @PostMapping("/send/bulk")
    @Operation(summary = "Send bulk email", description = "Send email to multiple recipients")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Bulk email sent successfully"),
            ApiResponse(responseCode = "400", description = "Invalid bulk email data"),
            ApiResponse(responseCode = "500", description = "Failed to send bulk email")
        ]
    )
    fun sendBulkEmail(
        @RequestBody bulkEmailDTO: BulkEmailDTO
    ): ResponseEntity<XApiResponse<List<EmailDTO>>> {
        return try {
            val results = mailService.sendBulkEmail(bulkEmailDTO).get()
            ResponseEntity.ok(XApiResponse.success(results, "Bulk email sent successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to send bulk email: ${e.message}"))
        }
    }
    
    @PostMapping("/send/welcome")
    @Operation(summary = "Send welcome email", description = "Send welcome email to new user")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Welcome email sent successfully"),
            ApiResponse(responseCode = "400", description = "Invalid data"),
            ApiResponse(responseCode = "500", description = "Failed to send welcome email")
        ]
    )
    fun sendWelcomeEmail(
        @Parameter(description = "Recipient email address") 
        @RequestParam to: String,
        
        @Parameter(description = "User name") 
        @RequestParam userName: String,
        
        @Parameter(description = "Account activation link") 
        @RequestParam activationLink: String
    ): ResponseEntity<XApiResponse<EmailDTO>> {
        return try {
            val result = mailService.sendWelcomeEmail(to, userName, activationLink).get()
            ResponseEntity.ok(XApiResponse.success(result, "Welcome email sent successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to send welcome email: ${e.message}"))
        }
    }
    
    @PostMapping("/send/password-reset")
    @Operation(summary = "Send password reset email", description = "Send password reset email to user")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Password reset email sent successfully"),
            ApiResponse(responseCode = "400", description = "Invalid data"),
            ApiResponse(responseCode = "500", description = "Failed to send password reset email")
        ]
    )
    fun sendPasswordResetEmail(
        @Parameter(description = "Recipient email address") 
        @RequestParam to: String,
        
        @Parameter(description = "User name") 
        @RequestParam userName: String,
        
        @Parameter(description = "Password reset link") 
        @RequestParam resetLink: String
    ): ResponseEntity<XApiResponse<EmailDTO>> {
        return try {
            val result = mailService.sendPasswordResetEmail(to, userName, resetLink).get()
            ResponseEntity.ok(XApiResponse.success(result, "Password reset email sent successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to send password reset email: ${e.message}"))
        }
    }
    
    @PostMapping("/send/order-confirmation")
    @Operation(summary = "Send order confirmation email", description = "Send order confirmation email to customer")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Order confirmation email sent successfully"),
            ApiResponse(responseCode = "400", description = "Invalid data"),
            ApiResponse(responseCode = "500", description = "Failed to send order confirmation email")
        ]
    )
    fun sendOrderConfirmationEmail(
        @Parameter(description = "Recipient email address") 
        @RequestParam to: String,
        
        @Parameter(description = "Customer name") 
        @RequestParam userName: String,
        
        @Parameter(description = "Order number") 
        @RequestParam orderNumber: String,
        
        @Parameter(description = "Order date") 
        @RequestParam orderDate: String,
        
        @Parameter(description = "Total amount") 
        @RequestParam totalAmount: String,
        
        @Parameter(description = "Order items") 
        @RequestBody items: List<Map<String, Any>>
    ): ResponseEntity<XApiResponse<EmailDTO>> {
        return try {
            val result = mailService.sendOrderConfirmationEmail(
                to, userName, orderNumber, orderDate, totalAmount, items
            ).get()
            ResponseEntity.ok(XApiResponse.success(result, "Order confirmation email sent successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to send order confirmation email: ${e.message}"))
        }
    }
    
    @PostMapping("/send/notification")
    @Operation(summary = "Send notification email", description = "Send notification email to user")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Notification email sent successfully"),
            ApiResponse(responseCode = "400", description = "Invalid data"),
            ApiResponse(responseCode = "500", description = "Failed to send notification email")
        ]
    )
    fun sendNotificationEmail(
        @Parameter(description = "Recipient email address") 
        @RequestParam to: String,
        
        @Parameter(description = "Notification title") 
        @RequestParam title: String,
        
        @Parameter(description = "Notification message") 
        @RequestParam message: String,
        
        @Parameter(description = "Action URL (optional)") 
        @RequestParam(required = false) actionUrl: String?,
        
        @Parameter(description = "Action text (optional)") 
        @RequestParam(required = false) actionText: String?
    ): ResponseEntity<XApiResponse<EmailDTO>> {
        return try {
            val result = mailService.sendNotificationEmail(to, title, message, actionUrl, actionText).get()
            ResponseEntity.ok(XApiResponse.success(result, "Notification email sent successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to send notification email: ${e.message}"))
        }
    }
    
    @PostMapping("/send/with-attachment")
    @Operation(summary = "Send email with attachment", description = "Send email with file attachments")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Email with attachment sent successfully"),
            ApiResponse(responseCode = "400", description = "Invalid data"),
            ApiResponse(responseCode = "500", description = "Failed to send email with attachment")
        ]
    )
    fun sendEmailWithAttachment(
        @Parameter(description = "Recipient email address") 
        @RequestParam to: String,
        
        @Parameter(description = "Email subject") 
        @RequestParam subject: String,
        
        @Parameter(description = "Email content") 
        @RequestParam content: String,
        
        @Parameter(description = "File attachments") 
        @RequestParam attachments: List<MultipartFile>
    ): ResponseEntity<XApiResponse<EmailDTO>> {
        return try {
            val result = mailService.sendEmailWithAttachment(to, subject, content, attachments).get()
            ResponseEntity.ok(XApiResponse.success(result, "Email with attachment sent successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to send email with attachment: ${e.message}"))
        }
    }
    
    @GetMapping("/history/{email}")
    @Operation(summary = "Get email history", description = "Get email history for a specific email address")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Email history retrieved successfully"),
            ApiResponse(responseCode = "404", description = "Email not found"),
            ApiResponse(responseCode = "500", description = "Failed to retrieve email history")
        ]
    )
    fun getEmailHistory(
        @Parameter(description = "Email address") 
        @PathVariable email: String,
        
        @Parameter(description = "Number of days to look back (default: 30)") 
        @RequestParam(defaultValue = "30") days: Int
    ): ResponseEntity<XApiResponse<List<EmailDTO>>> {
        return try {
            val history = mailService.getEmailHistory(email, days)
            ResponseEntity.ok(XApiResponse.success(history, "Email history retrieved successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to retrieve email history: ${e.message}"))
        }
    }
    
    @GetMapping("/pending")
    @Operation(summary = "Get pending emails", description = "Get all pending emails")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Pending emails retrieved successfully"),
            ApiResponse(responseCode = "500", description = "Failed to retrieve pending emails")
        ]
    )
    fun getPendingEmails(): ResponseEntity<XApiResponse<List<EmailDTO>>> {
        return try {
            val pendingEmails = mailService.getPendingEmails()
            ResponseEntity.ok(XApiResponse.success(pendingEmails, "Pending emails retrieved successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to retrieve pending emails: ${e.message}"))
        }
    }
    
    @GetMapping("/failed")
    @Operation(summary = "Get failed emails", description = "Get all failed emails from last N hours")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Failed emails retrieved successfully"),
            ApiResponse(responseCode = "500", description = "Failed to retrieve failed emails")
        ]
    )
    fun getFailedEmails(
        @Parameter(description = "Number of hours to look back (default: 24)") 
        @RequestParam(defaultValue = "24") hours: Int
    ): ResponseEntity<XApiResponse<List<EmailDTO>>> {
        return try {
            val failedEmails = mailService.getFailedEmails(hours)
            ResponseEntity.ok(XApiResponse.success(failedEmails, "Failed emails retrieved successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to retrieve failed emails: ${e.message}"))
        }
    }
    
    @PostMapping("/retry")
    @Operation(summary = "Retry failed emails", description = "Retry sending all failed emails")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Failed emails retry completed"),
            ApiResponse(responseCode = "500", description = "Failed to retry emails")
        ]
    )
    fun retryFailedEmails(): ResponseEntity<XApiResponse<List<EmailDTO>>> {
        return try {
            val results = mailService.retryFailedEmails().get()
            ResponseEntity.ok(XApiResponse.success(results, "Failed emails retry completed"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to retry emails: ${e.message}"))
        }
    }
    
    @PostMapping("/send/template")
    @Operation(summary = "Send template email", description = "Send email using a template with custom data")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Template email sent successfully"),
            ApiResponse(responseCode = "400", description = "Invalid template data"),
            ApiResponse(responseCode = "500", description = "Failed to send template email")
        ]
    )
    fun sendTemplateEmail(
        @RequestBody templateEmailDTO: com.happyending.mail.dtos.TemplateEmailDTO
    ): ResponseEntity<XApiResponse<EmailDTO>> {
        return try {
            val result = mailService.sendTemplateEmail(templateEmailDTO).get()
            ResponseEntity.ok(XApiResponse.success(result, "Template email sent successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to send template email: ${e.message}"))
        }
    }
    
    @PostMapping("/send/template/bulk")
    @Operation(summary = "Send bulk template email", description = "Send template email to multiple recipients")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Bulk template email sent successfully"),
            ApiResponse(responseCode = "400", description = "Invalid bulk template data"),
            ApiResponse(responseCode = "500", description = "Failed to send bulk template email")
        ]
    )
    fun sendBulkTemplateEmail(
        @RequestBody bulkTemplateEmailDTO: com.happyending.mail.dtos.BulkTemplateEmailDTO
    ): ResponseEntity<XApiResponse<List<EmailDTO>>> {
        return try {
            val results = mailService.sendBulkTemplateEmail(bulkTemplateEmailDTO).get()
            ResponseEntity.ok(XApiResponse.success(results, "Bulk template email sent successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to send bulk template email: ${e.message}"))
        }
    }
    
    @GetMapping("/templates")
    @Operation(summary = "Get available templates", description = "Get list of available email templates with their requirements")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Templates retrieved successfully"),
            ApiResponse(responseCode = "500", description = "Failed to retrieve templates")
        ]
    )
    fun getAvailableTemplates(): ResponseEntity<XApiResponse<List<com.happyending.mail.dtos.TemplateInfoDTO>>> {
        return try {
            val templates = mailService.getAvailableTemplates()
            ResponseEntity.ok(XApiResponse.success(templates, "Templates retrieved successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to retrieve templates: ${e.message}"))
        }
    }
    
    @GetMapping("/templates/{templateName}/preview")
    @Operation(summary = "Preview template", description = "Preview template with sample data")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Template preview generated successfully"),
            ApiResponse(responseCode = "404", description = "Template not found"),
            ApiResponse(responseCode = "500", description = "Failed to generate template preview")
        ]
    )
    fun previewTemplate(
        @Parameter(description = "Template name") 
        @PathVariable templateName: String,
        
        @Parameter(description = "Custom template data (optional)") 
        @RequestBody(required = false) customData: Map<String, Any>?
    ): ResponseEntity<XApiResponse<Map<String, Any>>> {
        return try {
            val templates = mailService.getAvailableTemplates()
            val template = templates.find { it.templateName == templateName }
            
            if (template == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(XApiResponse.error("Template '$templateName' not found"))
            }
            
            val data = customData ?: template.exampleData
            val content = mailService.emailTemplateService.processTemplate(templateName, data)
            
            val preview = mapOf(
                "templateName" to templateName,
                "subject" to template.subject,
                "content" to content,
                "data" to data
            )
            
            ResponseEntity.ok(XApiResponse.success(preview, "Template preview generated successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to generate template preview: ${e.message}"))
        }
    }
}
