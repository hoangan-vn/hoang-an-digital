package com.happyending.mail.repositories

import com.happyending.mail.entities.Email
import com.happyending.mail.entities.EmailStatus
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface EmailRepository : MongoRepository<Email, String> {
    
    @Query("{ 'to': ?0, 'status': ?1 }")
    fun findByToAndStatus(to: String, status: EmailStatus): List<Email>
    
    @Query("{ 'status': ?0, 'createdAt': { \$gte: ?1, \$lte: ?2 } }")
    fun findByStatusAndCreatedAtBetween(
        status: EmailStatus,
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ): List<Email>
    
    @Query("{ 'status': 'PENDING' }")
    fun findPendingEmails(): List<Email>
    
    @Query("{ 'status': 'FAILED', 'createdAt': { \$gte: ?0 } }")
    fun findFailedEmailsAfter(startTime: LocalDateTime): List<Email>
    
    @Query("{ 'to': ?0, 'createdAt': { \$gte: ?1, \$lte: ?2 } }")
    fun findByToAndCreatedAtBetween(
        to: String,
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ): List<Email>
    
    @Query("{ 'status': ?0 }")
    fun countByStatus(status: EmailStatus): Long
    
    @Query("{ 'createdAt': { \$gte: ?0, \$lte: ?1 } }")
    fun countByCreatedAtBetween(startTime: LocalDateTime, endTime: LocalDateTime): Long
}
