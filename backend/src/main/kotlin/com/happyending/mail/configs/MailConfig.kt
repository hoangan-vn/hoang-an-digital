package com.happyending.mail.configs

import freemarker.template.Configuration
import freemarker.template.TemplateExceptionHandler
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration as SpringConfiguration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer
import java.util.*

@SpringConfiguration
class MailConfig {
    
    @Value("\${spring.mail.host}")
    private lateinit var mailHost: String
    
    @Value("\${spring.mail.port}")
    private var mailPort: Int = 587
    
    @Value("\${spring.mail.username}")
    private lateinit var mailUsername: String
    
    @Value("\${spring.mail.password}")
    private lateinit var mailPassword: String
    
    @Value("\${spring.mail.properties.mail.smtp.auth}")
    private var smtpAuth: Boolean = true
    
    @Value("\${spring.mail.properties.mail.smtp.starttls.enable}")
    private var starttlsEnable: Boolean = true
    
    @Bean
    fun javaMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = mailHost
        mailSender.port = mailPort
        mailSender.username = mailUsername
        mailSender.password = mailPassword
        
        val props = mailSender.javaMailProperties
        props["mail.transport.protocol"] = "smtp"
        props["mail.smtp.auth"] = smtpAuth.toString()
        props["mail.smtp.starttls.enable"] = starttlsEnable.toString()
        props["mail.smtp.ssl.trust"] = mailHost
        props["mail.smtp.ssl.protocols"] = "TLSv1.2"
        props["mail.debug"] = "false"
        
        return mailSender
    }
    
    @Bean
    fun freeMarkerConfigurer(): FreeMarkerConfigurer {
        val configurer = FreeMarkerConfigurer()
        configurer.templateLoaderPath = "classpath:/templates/"
        configurer.defaultEncoding = "UTF-8"
        
        val config = Configuration(Configuration.VERSION_2_3_31)
        config.setClassForTemplateLoading(this.javaClass, "/templates")
        config.defaultEncoding = "UTF-8"
        config.templateExceptionHandler = TemplateExceptionHandler.RETHROW_HANDLER
        configurer.configuration = config
        
        return configurer
    }
}
