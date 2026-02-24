package com.happyending.blog.services

import com.happyending.blog.dtos.*
import com.happyending.blog.entities.BlogCategory
import com.happyending.blog.mapper.BlogMapper
import com.happyending.blog.repositories.BlogCategoryRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture

@Service
class BlogCategoryService(
    private val blogCategoryRepository: BlogCategoryRepository,
    private val blogMapper: BlogMapper
) {
    
    private val logger = LoggerFactory.getLogger(BlogCategoryService::class.java)
    
    fun createCategory(createCategoryDTO: CreateBlogCategoryDTO): CompletableFuture<BlogCategoryDTO> {
        logger.info("Creating category: ${createCategoryDTO.name}")
        
        return CompletableFuture.supplyAsync {
            try {
                val category = blogMapper.toEntity(createCategoryDTO)
                val savedCategory = blogCategoryRepository.save(category)
                
                logger.info("Category created successfully with ID: ${savedCategory.id}")
                blogMapper.toDTO(savedCategory)
                
            } catch (e: Exception) {
                logger.error("Failed to create category: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun updateCategory(id: String, updateCategoryDTO: UpdateBlogCategoryDTO): CompletableFuture<BlogCategoryDTO> {
        logger.info("Updating category: $id")
        
        return CompletableFuture.supplyAsync {
            try {
                val existingCategory = blogCategoryRepository.findById(id)
                    .orElseThrow { IllegalArgumentException("Category not found with ID: $id") }
                
                val updatedCategory = blogMapper.updateEntity(existingCategory, updateCategoryDTO)
                val savedCategory = blogCategoryRepository.save(updatedCategory)
                
                logger.info("Category updated successfully: $id")
                blogMapper.toDTO(savedCategory)
                
            } catch (e: Exception) {
                logger.error("Failed to update category $id: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getCategoryById(id: String): CompletableFuture<BlogCategoryDTO> {
        logger.info("Getting category by ID: $id")
        
        return CompletableFuture.supplyAsync {
            try {
                val category = blogCategoryRepository.findById(id)
                    .orElseThrow { IllegalArgumentException("Category not found with ID: $id") }
                
                blogMapper.toDTO(category)
                
            } catch (e: Exception) {
                logger.error("Failed to get category $id: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getCategoryBySlug(slug: String): CompletableFuture<BlogCategoryDTO> {
        logger.info("Getting category by slug: $slug")
        
        return CompletableFuture.supplyAsync {
            try {
                val category = blogCategoryRepository.findBySlug(slug)
                    ?: throw IllegalArgumentException("Category not found with slug: $slug")
                
                blogMapper.toDTO(category)
                
            } catch (e: Exception) {
                logger.error("Failed to get category by slug $slug: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getCategories(filter: BlogCategoryFilterDTO, pageable: Pageable): CompletableFuture<Page<BlogCategoryDTO>> {
        logger.info("Getting categories with filter: $filter")
        
        return CompletableFuture.supplyAsync {
            try {
                val categories = when {
                    filter.parentId != null && filter.isActive != null -> {
                        if (filter.isActive) {
                            blogCategoryRepository.findActiveByParentId(filter.parentId, pageable)
                        } else {
                            blogCategoryRepository.findByParentId(filter.parentId, pageable)
                        }
                    }
                    filter.parentId != null -> {
                        blogCategoryRepository.findByParentId(filter.parentId, pageable)
                    }
                    filter.isActive != null -> {
                        if (filter.isActive) {
                            blogCategoryRepository.findActiveCategories(pageable)
                        } else {
                            blogCategoryRepository.findByIsActive(false, pageable)
                        }
                    }
                    else -> {
                        blogCategoryRepository.findActiveCategories(pageable)
                    }
                }
                
                categories.map { blogMapper.toDTO(it) }
                
            } catch (e: Exception) {
                logger.error("Failed to get categories: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getTopLevelCategories(pageable: Pageable): CompletableFuture<Page<BlogCategoryDTO>> {
        logger.info("Getting top-level categories")
        
        return CompletableFuture.supplyAsync {
            try {
                val categories = blogCategoryRepository.findTopLevelCategories(pageable)
                categories.map { blogMapper.toDTO(it) }
                
            } catch (e: Exception) {
                logger.error("Failed to get top-level categories: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getActiveTopLevelCategories(pageable: Pageable): CompletableFuture<Page<BlogCategoryDTO>> {
        logger.info("Getting active top-level categories")
        
        return CompletableFuture.supplyAsync {
            try {
                val categories = blogCategoryRepository.findActiveTopLevelCategories(pageable)
                categories.map { blogMapper.toDTO(it) }
                
            } catch (e: Exception) {
                logger.error("Failed to get active top-level categories: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getCategoriesByParentId(parentId: String, pageable: Pageable): CompletableFuture<Page<BlogCategoryDTO>> {
        logger.info("Getting categories by parent ID: $parentId")
        
        return CompletableFuture.supplyAsync {
            try {
                val categories = blogCategoryRepository.findByParentId(parentId, pageable)
                categories.map { blogMapper.toDTO(it) }
                
            } catch (e: Exception) {
                logger.error("Failed to get categories by parent ID $parentId: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getActiveCategoriesByParentId(parentId: String, pageable: Pageable): CompletableFuture<Page<BlogCategoryDTO>> {
        logger.info("Getting active categories by parent ID: $parentId")
        
        return CompletableFuture.supplyAsync {
            try {
                val categories = blogCategoryRepository.findActiveByParentId(parentId, pageable)
                categories.map { blogMapper.toDTO(it) }
                
            } catch (e: Exception) {
                logger.error("Failed to get active categories by parent ID $parentId: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun activateCategory(id: String): CompletableFuture<BlogCategoryDTO> {
        logger.info("Activating category: $id")
        
        return CompletableFuture.supplyAsync {
            try {
                val category = blogCategoryRepository.findById(id)
                    .orElseThrow { IllegalArgumentException("Category not found with ID: $id") }
                
                val activatedCategory = category.copy(
                    isActive = true,
                    updatedAt = LocalDateTime.now()
                )
                
                val savedCategory = blogCategoryRepository.save(activatedCategory)
                
                logger.info("Category activated successfully: $id")
                blogMapper.toDTO(savedCategory)
                
            } catch (e: Exception) {
                logger.error("Failed to activate category $id: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun deactivateCategory(id: String): CompletableFuture<BlogCategoryDTO> {
        logger.info("Deactivating category: $id")
        
        return CompletableFuture.supplyAsync {
            try {
                val category = blogCategoryRepository.findById(id)
                    .orElseThrow { IllegalArgumentException("Category not found with ID: $id") }
                
                val deactivatedCategory = category.copy(
                    isActive = false,
                    updatedAt = LocalDateTime.now()
                )
                
                val savedCategory = blogCategoryRepository.save(deactivatedCategory)
                
                logger.info("Category deactivated successfully: $id")
                blogMapper.toDTO(savedCategory)
                
            } catch (e: Exception) {
                logger.error("Failed to deactivate category $id: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun deleteCategory(id: String): CompletableFuture<Void> {
        logger.info("Deleting category: $id")
        
        return CompletableFuture.runAsync {
            try {
                if (!blogCategoryRepository.existsById(id)) {
                    throw IllegalArgumentException("Category not found with ID: $id")
                }
                
                blogCategoryRepository.deleteById(id)
                
                logger.info("Category deleted successfully: $id")
                
            } catch (e: Exception) {
                logger.error("Failed to delete category $id: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getCategoryCount(): CompletableFuture<Long> {
        logger.info("Getting category count")
        
        return CompletableFuture.supplyAsync {
            try {
                blogCategoryRepository.count()
                
            } catch (e: Exception) {
                logger.error("Failed to get category count: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getActiveCategoryCount(): CompletableFuture<Long> {
        logger.info("Getting active category count")
        
        return CompletableFuture.supplyAsync {
            try {
                blogCategoryRepository.countActiveCategories()
                
            } catch (e: Exception) {
                logger.error("Failed to get active category count: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getTopLevelCategoryCount(): CompletableFuture<Long> {
        logger.info("Getting top-level category count")
        
        return CompletableFuture.supplyAsync {
            try {
                blogCategoryRepository.countActiveTopLevelCategories()
                
            } catch (e: Exception) {
                logger.error("Failed to get top-level category count: ${e.message}", e)
                throw e
            }
        }
    }
}



import com.happyending.blog.dtos.*
import com.happyending.blog.entities.BlogCategory
import com.happyending.blog.mapper.BlogMapper
import com.happyending.blog.repositories.BlogCategoryRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture

@Service
class BlogCategoryService(
    private val blogCategoryRepository: BlogCategoryRepository,
    private val blogMapper: BlogMapper
) {
    
    private val logger = LoggerFactory.getLogger(BlogCategoryService::class.java)
    
    fun createCategory(createCategoryDTO: CreateBlogCategoryDTO): CompletableFuture<BlogCategoryDTO> {
        logger.info("Creating category: ${createCategoryDTO.name}")
        
        return CompletableFuture.supplyAsync {
            try {
                val category = blogMapper.toEntity(createCategoryDTO)
                val savedCategory = blogCategoryRepository.save(category)
                
                logger.info("Category created successfully with ID: ${savedCategory.id}")
                blogMapper.toDTO(savedCategory)
                
            } catch (e: Exception) {
                logger.error("Failed to create category: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun updateCategory(id: String, updateCategoryDTO: UpdateBlogCategoryDTO): CompletableFuture<BlogCategoryDTO> {
        logger.info("Updating category: $id")
        
        return CompletableFuture.supplyAsync {
            try {
                val existingCategory = blogCategoryRepository.findById(id)
                    .orElseThrow { IllegalArgumentException("Category not found with ID: $id") }
                
                val updatedCategory = blogMapper.updateEntity(existingCategory, updateCategoryDTO)
                val savedCategory = blogCategoryRepository.save(updatedCategory)
                
                logger.info("Category updated successfully: $id")
                blogMapper.toDTO(savedCategory)
                
            } catch (e: Exception) {
                logger.error("Failed to update category $id: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getCategoryById(id: String): CompletableFuture<BlogCategoryDTO> {
        logger.info("Getting category by ID: $id")
        
        return CompletableFuture.supplyAsync {
            try {
                val category = blogCategoryRepository.findById(id)
                    .orElseThrow { IllegalArgumentException("Category not found with ID: $id") }
                
                blogMapper.toDTO(category)
                
            } catch (e: Exception) {
                logger.error("Failed to get category $id: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getCategoryBySlug(slug: String): CompletableFuture<BlogCategoryDTO> {
        logger.info("Getting category by slug: $slug")
        
        return CompletableFuture.supplyAsync {
            try {
                val category = blogCategoryRepository.findBySlug(slug)
                    ?: throw IllegalArgumentException("Category not found with slug: $slug")
                
                blogMapper.toDTO(category)
                
            } catch (e: Exception) {
                logger.error("Failed to get category by slug $slug: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getCategories(filter: BlogCategoryFilterDTO, pageable: Pageable): CompletableFuture<Page<BlogCategoryDTO>> {
        logger.info("Getting categories with filter: $filter")
        
        return CompletableFuture.supplyAsync {
            try {
                val categories = when {
                    filter.parentId != null && filter.isActive != null -> {
                        if (filter.isActive) {
                            blogCategoryRepository.findActiveByParentId(filter.parentId, pageable)
                        } else {
                            blogCategoryRepository.findByParentId(filter.parentId, pageable)
                        }
                    }
                    filter.parentId != null -> {
                        blogCategoryRepository.findByParentId(filter.parentId, pageable)
                    }
                    filter.isActive != null -> {
                        if (filter.isActive) {
                            blogCategoryRepository.findActiveCategories(pageable)
                        } else {
                            blogCategoryRepository.findByIsActive(false, pageable)
                        }
                    }
                    else -> {
                        blogCategoryRepository.findActiveCategories(pageable)
                    }
                }
                
                categories.map { blogMapper.toDTO(it) }
                
            } catch (e: Exception) {
                logger.error("Failed to get categories: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getTopLevelCategories(pageable: Pageable): CompletableFuture<Page<BlogCategoryDTO>> {
        logger.info("Getting top-level categories")
        
        return CompletableFuture.supplyAsync {
            try {
                val categories = blogCategoryRepository.findTopLevelCategories(pageable)
                categories.map { blogMapper.toDTO(it) }
                
            } catch (e: Exception) {
                logger.error("Failed to get top-level categories: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getActiveTopLevelCategories(pageable: Pageable): CompletableFuture<Page<BlogCategoryDTO>> {
        logger.info("Getting active top-level categories")
        
        return CompletableFuture.supplyAsync {
            try {
                val categories = blogCategoryRepository.findActiveTopLevelCategories(pageable)
                categories.map { blogMapper.toDTO(it) }
                
            } catch (e: Exception) {
                logger.error("Failed to get active top-level categories: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getCategoriesByParentId(parentId: String, pageable: Pageable): CompletableFuture<Page<BlogCategoryDTO>> {
        logger.info("Getting categories by parent ID: $parentId")
        
        return CompletableFuture.supplyAsync {
            try {
                val categories = blogCategoryRepository.findByParentId(parentId, pageable)
                categories.map { blogMapper.toDTO(it) }
                
            } catch (e: Exception) {
                logger.error("Failed to get categories by parent ID $parentId: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getActiveCategoriesByParentId(parentId: String, pageable: Pageable): CompletableFuture<Page<BlogCategoryDTO>> {
        logger.info("Getting active categories by parent ID: $parentId")
        
        return CompletableFuture.supplyAsync {
            try {
                val categories = blogCategoryRepository.findActiveByParentId(parentId, pageable)
                categories.map { blogMapper.toDTO(it) }
                
            } catch (e: Exception) {
                logger.error("Failed to get active categories by parent ID $parentId: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun activateCategory(id: String): CompletableFuture<BlogCategoryDTO> {
        logger.info("Activating category: $id")
        
        return CompletableFuture.supplyAsync {
            try {
                val category = blogCategoryRepository.findById(id)
                    .orElseThrow { IllegalArgumentException("Category not found with ID: $id") }
                
                val activatedCategory = category.copy(
                    isActive = true,
                    updatedAt = LocalDateTime.now()
                )
                
                val savedCategory = blogCategoryRepository.save(activatedCategory)
                
                logger.info("Category activated successfully: $id")
                blogMapper.toDTO(savedCategory)
                
            } catch (e: Exception) {
                logger.error("Failed to activate category $id: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun deactivateCategory(id: String): CompletableFuture<BlogCategoryDTO> {
        logger.info("Deactivating category: $id")
        
        return CompletableFuture.supplyAsync {
            try {
                val category = blogCategoryRepository.findById(id)
                    .orElseThrow { IllegalArgumentException("Category not found with ID: $id") }
                
                val deactivatedCategory = category.copy(
                    isActive = false,
                    updatedAt = LocalDateTime.now()
                )
                
                val savedCategory = blogCategoryRepository.save(deactivatedCategory)
                
                logger.info("Category deactivated successfully: $id")
                blogMapper.toDTO(savedCategory)
                
            } catch (e: Exception) {
                logger.error("Failed to deactivate category $id: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun deleteCategory(id: String): CompletableFuture<Void> {
        logger.info("Deleting category: $id")
        
        return CompletableFuture.runAsync {
            try {
                if (!blogCategoryRepository.existsById(id)) {
                    throw IllegalArgumentException("Category not found with ID: $id")
                }
                
                blogCategoryRepository.deleteById(id)
                
                logger.info("Category deleted successfully: $id")
                
            } catch (e: Exception) {
                logger.error("Failed to delete category $id: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getCategoryCount(): CompletableFuture<Long> {
        logger.info("Getting category count")
        
        return CompletableFuture.supplyAsync {
            try {
                blogCategoryRepository.count()
                
            } catch (e: Exception) {
                logger.error("Failed to get category count: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getActiveCategoryCount(): CompletableFuture<Long> {
        logger.info("Getting active category count")
        
        return CompletableFuture.supplyAsync {
            try {
                blogCategoryRepository.countActiveCategories()
                
            } catch (e: Exception) {
                logger.error("Failed to get active category count: ${e.message}", e)
                throw e
            }
        }
    }
    
    fun getTopLevelCategoryCount(): CompletableFuture<Long> {
        logger.info("Getting top-level category count")
        
        return CompletableFuture.supplyAsync {
            try {
                blogCategoryRepository.countActiveTopLevelCategories()
                
            } catch (e: Exception) {
                logger.error("Failed to get top-level category count: ${e.message}", e)
                throw e
            }
        }
    }
}





