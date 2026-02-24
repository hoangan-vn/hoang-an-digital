package com.happyending.blog.repositories

import com.happyending.blog.entities.BlogCategory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface BlogCategoryRepository : MongoRepository<BlogCategory, String> {
    
    @Query("{ 'slug': ?0 }")
    fun findBySlug(slug: String): BlogCategory?
    
    @Query("{ 'parentId': ?0 }")
    fun findByParentId(parentId: String, pageable: Pageable): Page<BlogCategory>
    
    @Query("{ 'parentId': null }")
    fun findTopLevelCategories(pageable: Pageable): Page<BlogCategory>
    
    @Query("{ 'isActive': ?0 }")
    fun findByIsActive(isActive: Boolean, pageable: Pageable): Page<BlogCategory>
    
    @Query("{ 'isActive': true }")
    fun findActiveCategories(pageable: Pageable): Page<BlogCategory>
    
    @Query("{ 'isActive': true, 'parentId': null }")
    fun findActiveTopLevelCategories(pageable: Pageable): Page<BlogCategory>
    
    @Query("{ 'isActive': true, 'parentId': ?0 }")
    fun findActiveByParentId(parentId: String, pageable: Pageable): Page<BlogCategory>
    
    @Query("{ 'isActive': true }")
    fun findActiveCategoriesOrderBySortOrder(pageable: Pageable): Page<BlogCategory>
    
    @Query("{ 'isActive': true, 'parentId': null }")
    fun findActiveTopLevelCategoriesOrderBySortOrder(pageable: Pageable): Page<BlogCategory>
    
    @Query("{ 'isActive': true, 'parentId': ?0 }")
    fun findActiveByParentIdOrderBySortOrder(parentId: String, pageable: Pageable): Page<BlogCategory>
    
    @Query("{ 'isActive': true }")
    fun countActiveCategories(): Long
    
    @Query("{ 'isActive': true, 'parentId': null }")
    fun countActiveTopLevelCategories(): Long
    
    @Query("{ 'isActive': true, 'parentId': ?0 }")
    fun countActiveByParentId(parentId: String): Long
}


