package com.happyending.file.repositories

import com.happyending.file.entities.AccessMode
import com.happyending.file.entities.File
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface FileRepository : MongoRepository<File, String> {
    
    @Query("{ 'uploaderId': ?0 }")
    fun findByUploaderId(uploaderId: String, pageable: Pageable): Page<File>
    
    @Query("{ 'publicId': ?0 }")
    fun findByPublicId(publicId: String): File?
    
    @Query("{ 'resourceType': ?0 }")
    fun findByResourceType(resourceType: String, pageable: Pageable): Page<File>
    
    @Query("{ 'format': ?0 }")
    fun findByFormat(format: String, pageable: Pageable): Page<File>
    
    @Query("{ 'folder': ?0 }")
    fun findByFolder(folder: String, pageable: Pageable): Page<File>
    
    @Query("{ 'tags': { \$in: ?0 } }")
    fun findByTags(tags: List<String>, pageable: Pageable): Page<File>
    
    @Query("{ 'isPublic': ?0 }")
    fun findByIsPublic(isPublic: Boolean, pageable: Pageable): Page<File>
    
    @Query("{ 'accessMode': ?0 }")
    fun findByAccessMode(accessMode: AccessMode, pageable: Pageable): Page<File>
    
    @Query("{ 'isFeatured': ?0 }")
    fun findByIsFeatured(isFeatured: Boolean, pageable: Pageable): Page<File>
    
    @Query("{ 'isPublic': true, 'accessMode': 'PUBLIC' }")
    fun findPublicFiles(pageable: Pageable): Page<File>
    
    @Query("{ 'uploaderId': ?0, 'isPublic': true }")
    fun findPublicFilesByUploader(uploaderId: String, pageable: Pageable): Page<File>
    
    @Query("{ 'isPublic': true, 'resourceType': 'image' }")
    fun findPublicImages(pageable: Pageable): Page<File>
    
    @Query("{ 'isPublic': true, 'resourceType': 'video' }")
    fun findPublicVideos(pageable: Pageable): Page<File>
    
    @Query("{ 'isPublic': true, 'resourceType': 'raw' }")
    fun findPublicDocuments(pageable: Pageable): Page<File>
    
    @Query("{ 'isPublic': true, 'isFeatured': true }")
    fun findFeaturedFiles(pageable: Pageable): Page<File>
    
    @Query("{ 'isPublic': true, \$or: [ { 'originalName': { \$regex: ?0, \$options: 'i' } }, { 'description': { \$regex: ?0, \$options: 'i' } }, { 'altText': { \$regex: ?0, \$options: 'i' } } ] }")
    fun searchPublicFiles(searchTerm: String, pageable: Pageable): Page<File>
    
    @Query("{ 'createdAt': { \$gte: ?0, \$lte: ?1 } }")
    fun findByCreatedAtBetween(startDate: LocalDateTime, endDate: LocalDateTime, pageable: Pageable): Page<File>
    
    @Query("{ 'uploaderId': ?0, 'resourceType': ?1 }")
    fun findByUploaderIdAndResourceType(uploaderId: String, resourceType: String, pageable: Pageable): Page<File>
    
    @Query("{ 'uploaderId': ?0, 'folder': ?1 }")
    fun findByUploaderIdAndFolder(uploaderId: String, folder: String, pageable: Pageable): Page<File>
    
    @Query("{ 'uploaderId': ?0, 'tags': { \$in: ?1 } }")
    fun findByUploaderIdAndTags(uploaderId: String, tags: List<String>, pageable: Pageable): Page<File>
    
    @Query("{ 'uploaderId': ?0 }")
    fun countByUploaderId(uploaderId: String): Long
    
    @Query("{ 'resourceType': ?0 }")
    fun countByResourceType(resourceType: String): Long
    
    @Query("{ 'format': ?0 }")
    fun countByFormat(format: String): Long
    
    @Query("{ 'folder': ?0 }")
    fun countByFolder(folder: String): Long
    
    @Query("{ 'isPublic': ?0 }")
    fun countByIsPublic(isPublic: Boolean): Long
    
    @Query("{ 'accessMode': ?0 }")
    fun countByAccessMode(accessMode: AccessMode): Long
    
    @Query("{ 'isFeatured': ?0 }")
    fun countByIsFeatured(isFeatured: Boolean): Long
    
    @Query("{ 'isPublic': true }")
    fun sumFileSizeByIsPublic(): Long
    
    @Query("{ 'isPublic': true }")
    fun sumDownloadCountByIsPublic(): Long
    
    @Query("{ 'isPublic': true }")
    fun sumViewCountByIsPublic(): Long
}




