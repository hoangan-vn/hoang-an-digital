package com.happyending.blog.mapper

import com.happyending.blog.dtos.*
import com.happyending.blog.entities.Blog
import com.happyending.blog.entities.BlogCategory
import com.happyending.blog.entities.BlogComment
import org.springframework.stereotype.Component

@Component
class BlogMapper {
    
    // Blog mapping
    fun toEntity(dto: CreateBlogDTO): Blog {
        return Blog(
            title = dto.title,
            slug = generateSlug(dto.title),
            content = dto.content,
            excerpt = dto.excerpt ?: generateExcerpt(dto.content),
            authorId = dto.authorId,
            authorName = dto.authorName,
            authorEmail = dto.authorEmail,
            featuredImage = dto.featuredImage,
            tags = dto.tags,
            categories = dto.categories,
            metaTitle = dto.metaTitle,
            metaDescription = dto.metaDescription,
            seoKeywords = dto.seoKeywords,
            isFeatured = dto.isFeatured,
            isPinned = dto.isPinned,
            readingTime = calculateReadingTime(dto.content)
        )
    }
    
    fun toDTO(entity: Blog): BlogDTO {
        return BlogDTO(
            id = entity.id,
            title = entity.title,
            slug = entity.slug,
            content = entity.content,
            excerpt = entity.excerpt,
            authorId = entity.authorId,
            authorName = entity.authorName,
            authorEmail = entity.authorEmail,
            status = entity.status,
            featuredImage = entity.featuredImage,
            tags = entity.tags,
            categories = entity.categories,
            metaTitle = entity.metaTitle,
            metaDescription = entity.metaDescription,
            seoKeywords = entity.seoKeywords,
            viewCount = entity.viewCount,
            likeCount = entity.likeCount,
            commentCount = entity.commentCount,
            isFeatured = entity.isFeatured,
            isPinned = entity.isPinned,
            readingTime = entity.readingTime,
            publishedAt = entity.publishedAt,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }
    
    fun toDTOList(entities: List<Blog>): List<BlogDTO> {
        return entities.map { toDTO(it) }
    }
    
    fun updateEntity(entity: Blog, dto: UpdateBlogDTO): Blog {
        return entity.copy(
            title = dto.title ?: entity.title,
            slug = if (dto.title != null) generateSlug(dto.title) else entity.slug,
            content = dto.content ?: entity.content,
            excerpt = dto.excerpt ?: entity.excerpt,
            status = dto.status ?: entity.status,
            featuredImage = dto.featuredImage ?: entity.featuredImage,
            tags = dto.tags ?: entity.tags,
            categories = dto.categories ?: entity.categories,
            metaTitle = dto.metaTitle ?: entity.metaTitle,
            metaDescription = dto.metaDescription ?: entity.metaDescription,
            seoKeywords = dto.seoKeywords ?: entity.seoKeywords,
            isFeatured = dto.isFeatured ?: entity.isFeatured,
            isPinned = dto.isPinned ?: entity.isPinned,
            readingTime = if (dto.content != null) calculateReadingTime(dto.content) else entity.readingTime,
            updatedAt = java.time.LocalDateTime.now()
        )
    }
    
    // BlogComment mapping
    fun toEntity(dto: CreateBlogCommentDTO): BlogComment {
        return BlogComment(
            blogId = dto.blogId,
            parentId = dto.parentId,
            authorName = dto.authorName,
            authorEmail = dto.authorEmail,
            authorWebsite = dto.authorWebsite,
            content = dto.content,
            ipAddress = dto.ipAddress,
            userAgent = dto.userAgent
        )
    }
    
    fun toDTO(entity: BlogComment): BlogCommentDTO {
        return BlogCommentDTO(
            id = entity.id,
            blogId = entity.blogId,
            parentId = entity.parentId,
            authorName = entity.authorName,
            authorEmail = entity.authorEmail,
            authorWebsite = entity.authorWebsite,
            content = entity.content,
            status = entity.status,
            ipAddress = entity.ipAddress,
            userAgent = entity.userAgent,
            likeCount = entity.likeCount,
            replyCount = entity.replyCount,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }
    
    fun toDTOList(entities: List<BlogComment>): List<BlogCommentDTO> {
        return entities.map { toDTO(it) }
    }
    
    fun updateEntity(entity: BlogComment, dto: UpdateBlogCommentDTO): BlogComment {
        return entity.copy(
            content = dto.content ?: entity.content,
            status = dto.status ?: entity.status,
            updatedAt = java.time.LocalDateTime.now()
        )
    }
    
    // BlogCategory mapping
    fun toEntity(dto: CreateBlogCategoryDTO): BlogCategory {
        return BlogCategory(
            name = dto.name,
            slug = generateSlug(dto.name),
            description = dto.description,
            parentId = dto.parentId,
            color = dto.color,
            icon = dto.icon,
            sortOrder = dto.sortOrder,
            metaTitle = dto.metaTitle,
            metaDescription = dto.metaDescription
        )
    }
    
    fun toDTO(entity: BlogCategory): BlogCategoryDTO {
        return BlogCategoryDTO(
            id = entity.id,
            name = entity.name,
            slug = entity.slug,
            description = entity.description,
            parentId = entity.parentId,
            color = entity.color,
            icon = entity.icon,
            isActive = entity.isActive,
            sortOrder = entity.sortOrder,
            blogCount = entity.blogCount,
            metaTitle = entity.metaTitle,
            metaDescription = entity.metaDescription,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }
    
    fun toDTOList(entities: List<BlogCategory>): List<BlogCategoryDTO> {
        return entities.map { toDTO(it) }
    }
    
    fun updateEntity(entity: BlogCategory, dto: UpdateBlogCategoryDTO): BlogCategory {
        return entity.copy(
            name = dto.name ?: entity.name,
            slug = if (dto.name != null) generateSlug(dto.name) else entity.slug,
            description = dto.description ?: entity.description,
            parentId = dto.parentId ?: entity.parentId,
            color = dto.color ?: entity.color,
            icon = dto.icon ?: entity.icon,
            isActive = dto.isActive ?: entity.isActive,
            sortOrder = dto.sortOrder ?: entity.sortOrder,
            metaTitle = dto.metaTitle ?: entity.metaTitle,
            metaDescription = dto.metaDescription ?: entity.metaDescription,
            updatedAt = java.time.LocalDateTime.now()
        )
    }
    
    // Helper methods
    private fun generateSlug(title: String): String {
        return title.lowercase()
            .replace(Regex("[^a-z0-9\\s]"), "")
            .replace(Regex("\\s+"), "-")
            .trim('-')
    }
    
    private fun generateExcerpt(content: String, maxLength: Int = 160): String {
        val plainText = content.replace(Regex("<[^>]*>"), "")
        return if (plainText.length <= maxLength) {
            plainText
        } else {
            plainText.substring(0, maxLength).trim() + "..."
        }
    }
    
    private fun calculateReadingTime(content: String): Int {
        val wordsPerMinute = 200
        val wordCount = content.split(Regex("\\s+")).size
        return maxOf(1, (wordCount / wordsPerMinute).toInt())
    }
}



import com.happyending.blog.dtos.*
import com.happyending.blog.entities.Blog
import com.happyending.blog.entities.BlogCategory
import com.happyending.blog.entities.BlogComment
import org.springframework.stereotype.Component

@Component
class BlogMapper {
    
    // Blog mapping
    fun toEntity(dto: CreateBlogDTO): Blog {
        return Blog(
            title = dto.title,
            slug = generateSlug(dto.title),
            content = dto.content,
            excerpt = dto.excerpt ?: generateExcerpt(dto.content),
            authorId = dto.authorId,
            authorName = dto.authorName,
            authorEmail = dto.authorEmail,
            featuredImage = dto.featuredImage,
            tags = dto.tags,
            categories = dto.categories,
            metaTitle = dto.metaTitle,
            metaDescription = dto.metaDescription,
            seoKeywords = dto.seoKeywords,
            isFeatured = dto.isFeatured,
            isPinned = dto.isPinned,
            readingTime = calculateReadingTime(dto.content)
        )
    }
    
    fun toDTO(entity: Blog): BlogDTO {
        return BlogDTO(
            id = entity.id,
            title = entity.title,
            slug = entity.slug,
            content = entity.content,
            excerpt = entity.excerpt,
            authorId = entity.authorId,
            authorName = entity.authorName,
            authorEmail = entity.authorEmail,
            status = entity.status,
            featuredImage = entity.featuredImage,
            tags = entity.tags,
            categories = entity.categories,
            metaTitle = entity.metaTitle,
            metaDescription = entity.metaDescription,
            seoKeywords = entity.seoKeywords,
            viewCount = entity.viewCount,
            likeCount = entity.likeCount,
            commentCount = entity.commentCount,
            isFeatured = entity.isFeatured,
            isPinned = entity.isPinned,
            readingTime = entity.readingTime,
            publishedAt = entity.publishedAt,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }
    
    fun toDTOList(entities: List<Blog>): List<BlogDTO> {
        return entities.map { toDTO(it) }
    }
    
    fun updateEntity(entity: Blog, dto: UpdateBlogDTO): Blog {
        return entity.copy(
            title = dto.title ?: entity.title,
            slug = if (dto.title != null) generateSlug(dto.title) else entity.slug,
            content = dto.content ?: entity.content,
            excerpt = dto.excerpt ?: entity.excerpt,
            status = dto.status ?: entity.status,
            featuredImage = dto.featuredImage ?: entity.featuredImage,
            tags = dto.tags ?: entity.tags,
            categories = dto.categories ?: entity.categories,
            metaTitle = dto.metaTitle ?: entity.metaTitle,
            metaDescription = dto.metaDescription ?: entity.metaDescription,
            seoKeywords = dto.seoKeywords ?: entity.seoKeywords,
            isFeatured = dto.isFeatured ?: entity.isFeatured,
            isPinned = dto.isPinned ?: entity.isPinned,
            readingTime = if (dto.content != null) calculateReadingTime(dto.content) else entity.readingTime,
            updatedAt = java.time.LocalDateTime.now()
        )
    }
    
    // BlogComment mapping
    fun toEntity(dto: CreateBlogCommentDTO): BlogComment {
        return BlogComment(
            blogId = dto.blogId,
            parentId = dto.parentId,
            authorName = dto.authorName,
            authorEmail = dto.authorEmail,
            authorWebsite = dto.authorWebsite,
            content = dto.content,
            ipAddress = dto.ipAddress,
            userAgent = dto.userAgent
        )
    }
    
    fun toDTO(entity: BlogComment): BlogCommentDTO {
        return BlogCommentDTO(
            id = entity.id,
            blogId = entity.blogId,
            parentId = entity.parentId,
            authorName = entity.authorName,
            authorEmail = entity.authorEmail,
            authorWebsite = entity.authorWebsite,
            content = entity.content,
            status = entity.status,
            ipAddress = entity.ipAddress,
            userAgent = entity.userAgent,
            likeCount = entity.likeCount,
            replyCount = entity.replyCount,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }
    
    fun toDTOList(entities: List<BlogComment>): List<BlogCommentDTO> {
        return entities.map { toDTO(it) }
    }
    
    fun updateEntity(entity: BlogComment, dto: UpdateBlogCommentDTO): BlogComment {
        return entity.copy(
            content = dto.content ?: entity.content,
            status = dto.status ?: entity.status,
            updatedAt = java.time.LocalDateTime.now()
        )
    }
    
    // BlogCategory mapping
    fun toEntity(dto: CreateBlogCategoryDTO): BlogCategory {
        return BlogCategory(
            name = dto.name,
            slug = generateSlug(dto.name),
            description = dto.description,
            parentId = dto.parentId,
            color = dto.color,
            icon = dto.icon,
            sortOrder = dto.sortOrder,
            metaTitle = dto.metaTitle,
            metaDescription = dto.metaDescription
        )
    }
    
    fun toDTO(entity: BlogCategory): BlogCategoryDTO {
        return BlogCategoryDTO(
            id = entity.id,
            name = entity.name,
            slug = entity.slug,
            description = entity.description,
            parentId = entity.parentId,
            color = entity.color,
            icon = entity.icon,
            isActive = entity.isActive,
            sortOrder = entity.sortOrder,
            blogCount = entity.blogCount,
            metaTitle = entity.metaTitle,
            metaDescription = entity.metaDescription,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }
    
    fun toDTOList(entities: List<BlogCategory>): List<BlogCategoryDTO> {
        return entities.map { toDTO(it) }
    }
    
    fun updateEntity(entity: BlogCategory, dto: UpdateBlogCategoryDTO): BlogCategory {
        return entity.copy(
            name = dto.name ?: entity.name,
            slug = if (dto.name != null) generateSlug(dto.name) else entity.slug,
            description = dto.description ?: entity.description,
            parentId = dto.parentId ?: entity.parentId,
            color = dto.color ?: entity.color,
            icon = dto.icon ?: entity.icon,
            isActive = dto.isActive ?: entity.isActive,
            sortOrder = dto.sortOrder ?: entity.sortOrder,
            metaTitle = dto.metaTitle ?: entity.metaTitle,
            metaDescription = dto.metaDescription ?: entity.metaDescription,
            updatedAt = java.time.LocalDateTime.now()
        )
    }
    
    // Helper methods
    private fun generateSlug(title: String): String {
        return title.lowercase()
            .replace(Regex("[^a-z0-9\\s]"), "")
            .replace(Regex("\\s+"), "-")
            .trim('-')
    }
    
    private fun generateExcerpt(content: String, maxLength: Int = 160): String {
        val plainText = content.replace(Regex("<[^>]*>"), "")
        return if (plainText.length <= maxLength) {
            plainText
        } else {
            plainText.substring(0, maxLength).trim() + "..."
        }
    }
    
    private fun calculateReadingTime(content: String): Int {
        val wordsPerMinute = 200
        val wordCount = content.split(Regex("\\s+")).size
        return maxOf(1, (wordCount / wordsPerMinute).toInt())
    }
}





