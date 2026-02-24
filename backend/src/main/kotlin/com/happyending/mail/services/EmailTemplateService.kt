package com.happyending.mail.services

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer
import java.io.StringWriter
import java.util.*

@Service
class EmailTemplateService(
    private val freeMarkerConfigurer: FreeMarkerConfigurer
) {
    
    private val logger = LoggerFactory.getLogger(EmailTemplateService::class.java)
    
    fun processTemplate(templateName: String, data: Map<String, Any>): String {
        return try {
            val template = freeMarkerConfigurer.configuration.getTemplate("$templateName.ftl")
            val stringWriter = StringWriter()
            template.process(data, stringWriter)
            stringWriter.toString()
        } catch (e: Exception) {
            logger.error("Failed to process template $templateName: ${e.message}", e)
            throw e
        }
    }
    
    fun getWelcomeEmailContent(userName: String, activationLink: String): String {
        val data = mapOf(
            "userName" to userName,
            "activationLink" to activationLink,
            "currentYear" to Calendar.getInstance().get(Calendar.YEAR)
        )
        return processTemplate("welcome", data)
    }
    
    fun getPasswordResetContent(userName: String, resetLink: String): String {
        val data = mapOf(
            "userName" to userName,
            "resetLink" to resetLink,
            "currentYear" to Calendar.getInstance().get(Calendar.YEAR)
        )
        return processTemplate("password-reset", data)
    }
    
    fun getOrderConfirmationContent(
        userName: String,
        orderNumber: String,
        orderDate: String,
        totalAmount: String,
        items: List<Map<String, Any>>
    ): String {
        val data = mapOf(
            "userName" to userName,
            "orderNumber" to orderNumber,
            "orderDate" to orderDate,
            "totalAmount" to totalAmount,
            "items" to items,
            "currentYear" to Calendar.getInstance().get(Calendar.YEAR)
        )
        return processTemplate("order-confirmation", data)
    }
    
    fun getNotificationContent(
        title: String,
        message: String,
        actionUrl: String? = null,
        actionText: String? = null
    ): String {
        val data = mapOf(
            "title" to title,
            "message" to message,
            "actionUrl" to (actionUrl ?: ""),
            "actionText" to (actionText ?: ""),
            "currentYear" to Calendar.getInstance().get(Calendar.YEAR)
        )
        return processTemplate("notification", data)
    }
}
