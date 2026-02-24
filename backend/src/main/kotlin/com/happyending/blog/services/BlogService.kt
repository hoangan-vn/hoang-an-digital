package com.happyending.blog.services

import com.happyending.blog.dtos.*
import com.happyending.blog.entities.Blog
import com.happyending.blog.entities.BlogStatus
import com.happyending.blog.mapper.BlogMapper
import com.happyending.blog.repositories.BlogRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture

@Service
class BlogService(
    private val blogRepository: BlogRepository,
    private val blogMapper: BlogMapper
) {
    
    private val logger = LoggerFactory.getLogger(BlogService::class.java)
    
    fun createBlog(createBlogDTO: CreateBlogDTO): CompletableFuture<BlogDTO> {
        logger.info("Creating blog: ${createBlogDTO.title}")
        
        return CompletableFuture.supplyAsync {
            try {
                val blog = blogMapper.toEntity(createBlogDTO)
                val savedBlog = blogRepository.save(blog)
                
                logger.info("Blog created successfully with ID: ${savedBlog.id}")
                blogMapper.toDTO(savedBlog)
                
            } catch (e: Exception) {
                logger.error("Failed to create blog: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun updateBlog(id: String, updateBlogDTO: UpdateBlogDTO): CompletableFuture<BlogDTO> {
        logger.info("Updating blog: $id")
        
        return CompletableFuture.supplyAsync {
            try {
                val existingBlog = blogRepository.findById(id)
                    .orElseThrow { IllegalArgumentException("Blog not found with ID: $id") }
                
                val updatedBlog = blogMapper.updateEntity(existingBlog, updateBlogDTO)
                val savedBlog = blogRepository.save(updatedBlog)
                
                logger.info("Blog updated successfully: $id")
                blogMapper.toDTO(savedBlog)
                
            } catch (e: Exception) {
                logger.error("Failed to update blog $id: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getBlogById(id: String): CompletableFuture<BlogDTO> {
        logger.info("Getting blog by ID: $id")
        
        return CompletableFuture.supplyAsync {
            try {
                val blog = blogRepository.findById(id)
                    .orElseThrow { IllegalArgumentException("Blog not found with ID: $id") }
                
                blogMapper.toDTO(blog)
                
            } catch (e: Exception) {
                logger.error("Failed to get blog $id: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getBlogBySlug(slug: String): CompletableFuture<BlogDTO> {
        logger.info("Getting blog by slug: $slug")
        
        return CompletableFuture.supplyAsync {
            try {
                val blog = blogRepository.findBySlug(slug)
                    ?: throw IllegalArgumentException("Blog not found with slug: $slug")
                
                blogMapper.toDTO(blog)
                
            } catch (e: Exception) {
                logger.error("Failed to get blog by slug $slug: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getPublishedBlogBySlug(slug: String): CompletableFuture<BlogDTO> {
        logger.info("Getting published blog by slug: $slug")
        
        return CompletableFuture.supplyAsync {
            try {
                val blog = blogRepository.findPublishedBySlug(slug)
                    ?: throw IllegalArgumentException("Published blog not found with slug: $slug")
                
                // Increment view count
                incrementViewCount(blog.id!!)
                
                blogMapper.toDTO(blog)
                
            } catch (e: Exception) {
                logger.error("Failed to get published blog by slug $slug: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getBlogs(filter: BlogFilterDTO, pageable: Pageable): CompletableFuture<Page<BlogDTO>> {
        logger.info("Getting blogs with filter: $filter")
        
        return CompletableFuture.supplyAsync {
            try {
                val blogs = when {
                    filter.status != null && filter.authorId != null -> {
                        blogRepository.findByAuthorIdAndStatus(filter.authorId, filter.status, pageable)
                    }
                    filter.status != null -> {
                        blogRepository.findByStatus(filter.status, pageable)
                    }
                    filter.authorId != null -> {
                        blogRepository.findByAuthorId(filter.authorId, pageable)
                    }
                    filter.tags != null && filter.tags.isNotEmpty() -> {
                        blogRepository.findByTags(filter.tags, pageable)
                    }
                    filter.categories != null && filter.categories.isNotEmpty() -> {
                        blogRepository.findByCategories(filter.categories, pageable)
                    }
                    filter.searchTerm != null -> {
                        blogRepository.searchPublishedBlogs(filter.searchTerm, pageable)
                    }
                    filter.startDate != null && filter.endDate != null -> {
                        blogRepository.findByPublishedDateBetween(filter.startDate, filter.endDate, pageable)
                    }
                    else -> {
                        blogRepository.findPublishedBlogs(pageable)
                    }
                }
                
                blogs.map { blogMapper.toDTO(it) }
                
            } catch (e: Exception) {
                logger.error("Failed to get blogs: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getFeaturedBlogs(pageable: Pageable): CompletableFuture<Page<BlogDTO>> {
        logger.info("Getting featured blogs")
        
        return CompletableFuture.supplyAsync {
            try {
                val blogs = blogRepository.findFeaturedBlogs(pageable)
                blogs.map { blogMapper.toDTO(it) }
                
            } catch (e: Exception) {
                logger.error("Failed to get featured blogs: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getPinnedBlogs(pageable: Pageable): CompletableFuture<Page<BlogDTO>> {
        logger.info("Getting pinned blogs")
        
        return CompletableFuture.supplyAsync {
            try {
                val blogs = blogRepository.findPinnedBlogs(pageable)
                blogs.map { blogMapper.toDTO(it) }
                
            } catch (e: Exception) {
                logger.error("Failed to get pinned blogs: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getBlogsByAuthor(authorId: String, pageable: Pageable): CompletableFuture<Page<BlogDTO>> {
        logger.info("Getting blogs by author: $authorId")
        
        return CompletableFuture.supplyAsync {
            try {
                val blogs = blogRepository.findByAuthorId(authorId, pageable)
                blogs.map { blogMapper.toDTO(it) }
                
            } catch (e: Exception) {
                logger.error("Failed to get blogs by author $authorId: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun publishBlog(id: String): CompletableFuture<BlogDTO> {
        logger.info("Publishing blog: $id")
        
        return CompletableFuture.supplyAsync {
            try {
                val blog = blogRepository.findById(id)
                    .orElseThrow { IllegalArgumentException("Blog not found with ID: $id") }
                
                val publishedBlog = blog.copy(
                    status = BlogStatus.PUBLISHED,
                    publishedAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                )
                
                val savedBlog = blogRepository.save(publishedBlog)
                
                logger.info("Blog published successfully: $id")
                blogMapper.toDTO(savedBlog)
                
            } catch (e: Exception) {
                logger.error("Failed to publish blog $id: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun archiveBlog(id: String): CompletableFuture<BlogDTO> {
        logger.info("Archiving blog: $id")
        
        return CompletableFuture.supplyAsync {
            try {
                val blog = blogRepository.findById(id)
                    .orElseThrow { IllegalArgumentException("Blog not found with ID: $id") }
                
                val archivedBlog = blog.copy(
                    status = BlogStatus.ARCHIVED,
                    updatedAt = LocalDateTime.now()
                )
                
                val savedBlog = blogRepository.save(archivedBlog)
                
                logger.info("Blog archived successfully: $id")
                blogMapper.toDTO(savedBlog)
                
            } catch (e: Exception) {
                logger.error("Failed to archive blog $id: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun deleteBlog(id: String): CompletableFuture<Void> {
        logger.info("Deleting blog: $id")
        
        return CompletableFuture.runAsync {
            try {
                if (!blogRepository.existsById(id)) {
                    throw IllegalArgumentException("Blog not found with ID: $id")
                }
                
                blogRepository.deleteById(id)
                
                logger.info("Blog deleted successfully: $id")
                
            } catch (e: Exception) {
                logger.error("Failed to delete blog $id: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun incrementViewCount(id: String): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            try {
                val blog = blogRepository.findById(id).orElse(null)
                if (blog != null) {
                    val updatedBlog = blog.copy(
                        viewCount = blog.viewCount + 1,
                        updatedAt = LocalDateTime.now()
                    )
                    blogRepository.save(updatedBlog)
                }
            } catch (e: Exception) {
                logger.error("Failed to increment view count for blog $id: ${e.message}", e)
            }
        }
    }
    
    fun likeBlog(id: String): CompletableFuture<BlogDTO> {
        logger.info("Liking blog: $id")
        
        return CompletableFuture.supplyAsync {
            try {
                val blog = blogRepository.findById(id)
                    .orElseThrow { IllegalArgumentException("Blog not found with ID: $id") }
                
                val likedBlog = blog.copy(
                    likeCount = blog.likeCount + 1,
                    updatedAt = LocalDateTime.now()
                )
                
                val savedBlog = blogRepository.save(likedBlog)
                
                logger.info("Blog liked successfully: $id")
                blogMapper.toDTO(savedBlog)
                
            } catch (e: Exception) {
                logger.error("Failed to like blog $id: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getBlogSummary(): CompletableFuture<BlogSummaryDTO> {
        logger.info("Getting blog summary")
        
        return CompletableFuture.supplyAsync {
            try {
                val totalBlogs = blogRepository.count()
                val publishedBlogs = blogRepository.countByStatus(BlogStatus.PUBLISHED)
                val draftBlogs = blogRepository.countByStatus(BlogStatus.DRAFT)
                val archivedBlogs = blogRepository.countByStatus(BlogStatus.ARCHIVED)
                val totalViews = blogRepository.sumViewCountByStatus()
                val totalLikes = blogRepository.sumLikeCountByStatus()
                val totalComments = blogRepository.sumCommentCountByStatus()
                val averageReadingTime = blogRepository.averageReadingTimeByStatus()
                
                BlogSummaryDTO(
                    totalBlogs = totalBlogs,
                    publishedBlogs = publishedBlogs,
                    draftBlogs = draftBlogs,
                    archivedBlogs = archivedBlogs,
                    totalViews = totalViews,
                    totalLikes = totalLikes,
                    totalComments = totalComments,
                    averageReadingTime = averageReadingTime
                )
                
            } catch (e: Exception) {
                logger.error("Failed to get blog summary: ${e.message}", e)
                throw e
            }
        }
    }
}



import com.happyending.blog.dtos.*
import com.happyending.blog.entities.Blog
import com.happyending.blog.entities.BlogStatus
import com.happyending.blog.mapper.BlogMapper
import com.happyending.blog.repositories.BlogRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture

@Service
class BlogService(
    private val blogRepository: BlogRepository,
    private val blogMapper: BlogMapper
) {
    
    private val logger = LoggerFactory.getLogger(BlogService::class.java)
    
    fun createBlog(createBlogDTO: CreateBlogDTO): CompletableFuture<BlogDTO> {
        logger.info("Creating blog: ${createBlogDTO.title}")
        
        return CompletableFuture.supplyAsync {
            try {
                val blog = blogMapper.toEntity(createBlogDTO)
                val savedBlog = blogRepository.save(blog)
                
                logger.info("Blog created successfully with ID: ${savedBlog.id}")
                blogMapper.toDTO(savedBlog)
                
            } catch (e: Exception) {
                logger.error("Failed to create blog: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun updateBlog(id: String, updateBlogDTO: UpdateBlogDTO): CompletableFuture<BlogDTO> {
        logger.info("Updating blog: $id")
        
        return CompletableFuture.supplyAsync {
            try {
                val existingBlog = blogRepository.findById(id)
                    .orElseThrow { IllegalArgumentException("Blog not found with ID: $id") }
                
                val updatedBlog = blogMapper.updateEntity(existingBlog, updateBlogDTO)
                val savedBlog = blogRepository.save(updatedBlog)
                
                logger.info("Blog updated successfully: $id")
                blogMapper.toDTO(savedBlog)
                
            } catch (e: Exception) {
                logger.error("Failed to update blog $id: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getBlogById(id: String): CompletableFuture<BlogDTO> {
        logger.info("Getting blog by ID: $id")
        
        return CompletableFuture.supplyAsync {
            try {
                val blog = blogRepository.findById(id)
                    .orElseThrow { IllegalArgumentException("Blog not found with ID: $id") }
                
                blogMapper.toDTO(blog)
                
            } catch (e: Exception) {
                logger.error("Failed to get blog $id: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getBlogBySlug(slug: String): CompletableFuture<BlogDTO> {
        logger.info("Getting blog by slug: $slug")
        
        return CompletableFuture.supplyAsync {
            try {
                val blog = blogRepository.findBySlug(slug)
                    ?: throw IllegalArgumentException("Blog not found with slug: $slug")
                
                blogMapper.toDTO(blog)
                
            } catch (e: Exception) {
                logger.error("Failed to get blog by slug $slug: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getPublishedBlogBySlug(slug: String): CompletableFuture<BlogDTO> {
        logger.info("Getting published blog by slug: $slug")
        
        return CompletableFuture.supplyAsync {
            try {
                val blog = blogRepository.findPublishedBySlug(slug)
                    ?: throw IllegalArgumentException("Published blog not found with slug: $slug")
                
                // Increment view count
                incrementViewCount(blog.id!!)
                
                blogMapper.toDTO(blog)
                
            } catch (e: Exception) {
                logger.error("Failed to get published blog by slug $slug: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getBlogs(filter: BlogFilterDTO, pageable: Pageable): CompletableFuture<Page<BlogDTO>> {
        logger.info("Getting blogs with filter: $filter")
        
        return CompletableFuture.supplyAsync {
            try {
                val blogs = when {
                    filter.status != null && filter.authorId != null -> {
                        blogRepository.findByAuthorIdAndStatus(filter.authorId, filter.status, pageable)
                    }
                    filter.status != null -> {
                        blogRepository.findByStatus(filter.status, pageable)
                    }
                    filter.authorId != null -> {
                        blogRepository.findByAuthorId(filter.authorId, pageable)
                    }
                    filter.tags != null && filter.tags.isNotEmpty() -> {
                        blogRepository.findByTags(filter.tags, pageable)
                    }
                    filter.categories != null && filter.categories.isNotEmpty() -> {
                        blogRepository.findByCategories(filter.categories, pageable)
                    }
                    filter.searchTerm != null -> {
                        blogRepository.searchPublishedBlogs(filter.searchTerm, pageable)
                    }
                    filter.startDate != null && filter.endDate != null -> {
                        blogRepository.findByPublishedDateBetween(filter.startDate, filter.endDate, pageable)
                    }
                    else -> {
                        blogRepository.findPublishedBlogs(pageable)
                    }
                }
                
                blogs.map { blogMapper.toDTO(it) }
                
            } catch (e: Exception) {
                logger.error("Failed to get blogs: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getFeaturedBlogs(pageable: Pageable): CompletableFuture<Page<BlogDTO>> {
        logger.info("Getting featured blogs")
        
        return CompletableFuture.supplyAsync {
            try {
                val blogs = blogRepository.findFeaturedBlogs(pageable)
                blogs.map { blogMapper.toDTO(it) }
                
            } catch (e: Exception) {
                logger.error("Failed to get featured blogs: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getPinnedBlogs(pageable: Pageable): CompletableFuture<Page<BlogDTO>> {
        logger.info("Getting pinned blogs")
        
        return CompletableFuture.supplyAsync {
            try {
                val blogs = blogRepository.findPinnedBlogs(pageable)
                blogs.map { blogMapper.toDTO(it) }
                
            } catch (e: Exception) {
                logger.error("Failed to get pinned blogs: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getBlogsByAuthor(authorId: String, pageable: Pageable): CompletableFuture<Page<BlogDTO>> {
        logger.info("Getting blogs by author: $authorId")
        
        return CompletableFuture.supplyAsync {
            try {
                val blogs = blogRepository.findByAuthorId(authorId, pageable)
                blogs.map { blogMapper.toDTO(it) }
                
            } catch (e: Exception) {
                logger.error("Failed to get blogs by author $authorId: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun publishBlog(id: String): CompletableFuture<BlogDTO> {
        logger.info("Publishing blog: $id")
        
        return CompletableFuture.supplyAsync {
            try {
                val blog = blogRepository.findById(id)
                    .orElseThrow { IllegalArgumentException("Blog not found with ID: $id") }
                
                val publishedBlog = blog.copy(
                    status = BlogStatus.PUBLISHED,
                    publishedAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                )
                
                val savedBlog = blogRepository.save(publishedBlog)
                
                logger.info("Blog published successfully: $id")
                blogMapper.toDTO(savedBlog)
                
            } catch (e: Exception) {
                logger.error("Failed to publish blog $id: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun archiveBlog(id: String): CompletableFuture<BlogDTO> {
        logger.info("Archiving blog: $id")
        
        return CompletableFuture.supplyAsync {
            try {
                val blog = blogRepository.findById(id)
                    .orElseThrow { IllegalArgumentException("Blog not found with ID: $id") }
                
                val archivedBlog = blog.copy(
                    status = BlogStatus.ARCHIVED,
                    updatedAt = LocalDateTime.now()
                )
                
                val savedBlog = blogRepository.save(archivedBlog)
                
                logger.info("Blog archived successfully: $id")
                blogMapper.toDTO(savedBlog)
                
            } catch (e: Exception) {
                logger.error("Failed to archive blog $id: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun deleteBlog(id: String): CompletableFuture<Void> {
        logger.info("Deleting blog: $id")
        
        return CompletableFuture.runAsync {
            try {
                if (!blogRepository.existsById(id)) {
                    throw IllegalArgumentException("Blog not found with ID: $id")
                }
                
                blogRepository.deleteById(id)
                
                logger.info("Blog deleted successfully: $id")
                
            } catch (e: Exception) {
                logger.error("Failed to delete blog $id: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun incrementViewCount(id: String): CompletableFuture<Void> {
        return CompletableFuture.runAsync {
            try {
                val blog = blogRepository.findById(id).orElse(null)
                if (blog != null) {
                    val updatedBlog = blog.copy(
                        viewCount = blog.viewCount + 1,
                        updatedAt = LocalDateTime.now()
                    )
                    blogRepository.save(updatedBlog)
                }
            } catch (e: Exception) {
                logger.error("Failed to increment view count for blog $id: ${e.message}", e)
            }
        }
    }
    
    fun likeBlog(id: String): CompletableFuture<BlogDTO> {
        logger.info("Liking blog: $id")
        
        return CompletableFuture.supplyAsync {
            try {
                val blog = blogRepository.findById(id)
                    .orElseThrow { IllegalArgumentException("Blog not found with ID: $id") }
                
                val likedBlog = blog.copy(
                    likeCount = blog.likeCount + 1,
                    updatedAt = LocalDateTime.now()
                )
                
                val savedBlog = blogRepository.save(likedBlog)
                
                logger.info("Blog liked successfully: $id")
                blogMapper.toDTO(savedBlog)
                
            } catch (e: Exception) {
                logger.error("Failed to like blog $id: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getBlogSummary(): CompletableFuture<BlogSummaryDTO> {
        logger.info("Getting blog summary")
        
        return CompletableFuture.supplyAsync {
            try {
                val totalBlogs = blogRepository.count()
                val publishedBlogs = blogRepository.countByStatus(BlogStatus.PUBLISHED)
                val draftBlogs = blogRepository.countByStatus(BlogStatus.DRAFT)
                val archivedBlogs = blogRepository.countByStatus(BlogStatus.ARCHIVED)
                val totalViews = blogRepository.sumViewCountByStatus()
                val totalLikes = blogRepository.sumLikeCountByStatus()
                val totalComments = blogRepository.sumCommentCountByStatus()
                val averageReadingTime = blogRepository.averageReadingTimeByStatus()
                
                BlogSummaryDTO(
                    totalBlogs = totalBlogs,
                    publishedBlogs = publishedBlogs,
                    draftBlogs = draftBlogs,
                    archivedBlogs = archivedBlogs,
                    totalViews = totalViews,
                    totalLikes = totalLikes,
                    totalComments = totalComments,
                    averageReadingTime = averageReadingTime
                )
                
            } catch (e: Exception) {
                logger.error("Failed to get blog summary: ${e.message}", e)
                throw e
            }
        }
    }
}





