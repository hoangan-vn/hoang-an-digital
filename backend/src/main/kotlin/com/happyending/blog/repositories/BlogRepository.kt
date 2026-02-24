package com.happyending.blog.repositories

import com.happyending.blog.entities.Blog
import com.happyending.blog.entities.BlogStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface BlogRepository : MongoRepository<Blog, String> {
    
    @Query("{ 'status': ?0 }")
    fun findByStatus(status: BlogStatus, pageable: Pageable): Page<Blog>
    
    @Query("{ 'authorId': ?0 }")
    fun findByAuthorId(authorId: String, pageable: Pageable): Page<Blog>
    
    @Query("{ 'slug': ?0 }")
    fun findBySlug(slug: String): Blog?
    
    @Query("{ 'status': 'PUBLISHED', 'slug': ?0 }")
    fun findPublishedBySlug(slug: String): Blog?
    
    @Query("{ 'status': 'PUBLISHED', 'isFeatured': true }")
    fun findFeaturedBlogs(pageable: Pageable): Page<Blog>
    
    @Query("{ 'status': 'PUBLISHED', 'isPinned': true }")
    fun findPinnedBlogs(pageable: Pageable): Page<Blog>
    
    @Query("{ 'status': 'PUBLISHED', 'tags': { \$in: ?0 } }")
    fun findByTags(tags: List<String>, pageable: Pageable): Page<Blog>
    
    @Query("{ 'status': 'PUBLISHED', 'categories': { \$in: ?0 } }")
    fun findByCategories(categories: List<String>, pageable: Pageable): Page<Blog>
    
    @Query("{ 'status': 'PUBLISHED', \$or: [ { 'title': { \$regex: ?0, \$options: 'i' } }, { 'content': { \$regex: ?0, \$options: 'i' } }, { 'excerpt': { \$regex: ?0, \$options: 'i' } } ] }")
    fun searchPublishedBlogs(searchTerm: String, pageable: Pageable): Page<Blog>
    
    @Query("{ 'status': 'PUBLISHED', 'publishedAt': { \$gte: ?0, \$lte: ?1 } }")
    fun findByPublishedDateBetween(startDate: LocalDateTime, endDate: LocalDateTime, pageable: Pageable): Page<Blog>
    
    @Query("{ 'status': 'PUBLISHED' }")
    fun findPublishedBlogs(pageable: Pageable): Page<Blog>
    
    @Query("{ 'status': 'PUBLISHED' }")
    fun findPublishedBlogsOrderByCreatedAtDesc(pageable: Pageable): Page<Blog>
    
    @Query("{ 'status': 'PUBLISHED' }")
    fun findPublishedBlogsOrderByViewCountDesc(pageable: Pageable): Page<Blog>
    
    @Query("{ 'status': 'PUBLISHED' }")
    fun findPublishedBlogsOrderByLikeCountDesc(pageable: Pageable): Page<Blog>
    
    @Query("{ 'authorId': ?0, 'status': ?1 }")
    fun findByAuthorIdAndStatus(authorId: String, status: BlogStatus, pageable: Pageable): Page<Blog>
    
    @Query("{ 'status': ?0 }")
    fun countByStatus(status: BlogStatus): Long
    
    @Query("{ 'authorId': ?0 }")
    fun countByAuthorId(authorId: String): Long
    
    @Query("{ 'status': 'PUBLISHED' }")
    fun sumViewCountByStatus(): Long
    
    @Query("{ 'status': 'PUBLISHED' }")
    fun sumLikeCountByStatus(): Long
    
    @Query("{ 'status': 'PUBLISHED' }")
    fun sumCommentCountByStatus(): Long
    
    @Query("{ 'status': 'PUBLISHED' }")
    fun averageReadingTimeByStatus(): Double
}

