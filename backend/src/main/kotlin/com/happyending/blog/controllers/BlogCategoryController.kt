package com.happyending.blog.controllers

import com.happyending.blog.dtos.*
import com.happyending.blog.services.BlogCategoryService
import com.happyending.common.responses.XApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Blog Categories", description = "API for managing blog categories")
class BlogCategoryController(
    private val blogCategoryService: BlogCategoryService
) {
    
    @PostMapping
    @Operation(summary = "Create category", description = "Create a new blog category")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Category created successfully"),
            ApiResponse(responseCode = "400", description = "Invalid category data"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun createCategory(
        @RequestBody createCategoryDTO: CreateBlogCategoryDTO
    ): ResponseEntity<XApiResponse<BlogCategoryDTO>> {
        return try {
            val category = blogCategoryService.createCategory(createCategoryDTO).get()
            ResponseEntity.status(HttpStatus.CREATED)
                .body(XApiResponse.success(category, "Category created successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to create category: ${e.message}"))
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID", description = "Get a category by its ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
            ApiResponse(responseCode = "404", description = "Category not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getCategoryById(
        @Parameter(description = "Category ID") 
        @PathVariable id: String
    ): ResponseEntity<XApiResponse<BlogCategoryDTO>> {
        return try {
            val category = blogCategoryService.getCategoryById(id).get()
            ResponseEntity.ok(XApiResponse.success(category, "Category retrieved successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to retrieve category: ${e.message}"))
        }
    }
    
    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get category by slug", description = "Get a category by its slug")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
            ApiResponse(responseCode = "404", description = "Category not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getCategoryBySlug(
        @Parameter(description = "Category slug") 
        @PathVariable slug: String
    ): ResponseEntity<XApiResponse<BlogCategoryDTO>> {
        return try {
            val category = blogCategoryService.getCategoryBySlug(slug).get()
            ResponseEntity.ok(XApiResponse.success(category, "Category retrieved successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to retrieve category: ${e.message}"))
        }
    }
    
    @GetMapping
    @Operation(summary = "Get categories", description = "Get list of categories with filtering and pagination")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getCategories(
        @Parameter(description = "Parent category ID") 
        @RequestParam(required = false) parentId: String?,
        
        @Parameter(description = "Active categories only") 
        @RequestParam(required = false) active: Boolean?,
        
        @Parameter(description = "Search term") 
        @RequestParam(required = false) search: String?,
        
        pageable: Pageable
    ): ResponseEntity<XApiResponse<Page<BlogCategoryDTO>>> {
        return try {
            val filter = BlogCategoryFilterDTO(
                parentId = parentId,
                isActive = active,
                searchTerm = search
            )
            
            val categories = blogCategoryService.getCategories(filter, pageable).get()
            ResponseEntity.ok(XApiResponse.success(categories, "Categories retrieved successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to retrieve categories: ${e.message}"))
        }
    }
    
    @GetMapping("/top-level")
    @Operation(summary = "Get top-level categories", description = "Get list of top-level categories")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Top-level categories retrieved successfully"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getTopLevelCategories(pageable: Pageable): ResponseEntity<XApiResponse<Page<BlogCategoryDTO>>> {
        return try {
            val categories = blogCategoryService.getTopLevelCategories(pageable).get()
            ResponseEntity.ok(XApiResponse.success(categories, "Top-level categories retrieved successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to retrieve top-level categories: ${e.message}"))
        }
    }
    
    @GetMapping("/active/top-level")
    @Operation(summary = "Get active top-level categories", description = "Get list of active top-level categories")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Active top-level categories retrieved successfully"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getActiveTopLevelCategories(pageable: Pageable): ResponseEntity<XApiResponse<Page<BlogCategoryDTO>>> {
        return try {
            val categories = blogCategoryService.getActiveTopLevelCategories(pageable).get()
            ResponseEntity.ok(XApiResponse.success(categories, "Active top-level categories retrieved successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to retrieve active top-level categories: ${e.message}"))
        }
    }
    
    @GetMapping("/parent/{parentId}")
    @Operation(summary = "Get categories by parent", description = "Get list of categories by parent ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getCategoriesByParentId(
        @Parameter(description = "Parent category ID") 
        @PathVariable parentId: String,
        pageable: Pageable
    ): ResponseEntity<XApiResponse<Page<BlogCategoryDTO>>> {
        return try {
            val categories = blogCategoryService.getCategoriesByParentId(parentId, pageable).get()
            ResponseEntity.ok(XApiResponse.success(categories, "Categories retrieved successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to retrieve categories: ${e.message}"))
        }
    }
    
    @GetMapping("/active/parent/{parentId}")
    @Operation(summary = "Get active categories by parent", description = "Get list of active categories by parent ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Active categories retrieved successfully"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getActiveCategoriesByParentId(
        @Parameter(description = "Parent category ID") 
        @PathVariable parentId: String,
        pageable: Pageable
    ): ResponseEntity<XApiResponse<Page<BlogCategoryDTO>>> {
        return try {
            val categories = blogCategoryService.getActiveCategoriesByParentId(parentId, pageable).get()
            ResponseEntity.ok(XApiResponse.success(categories, "Active categories retrieved successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to retrieve active categories: ${e.message}"))
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update category", description = "Update a category")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Category updated successfully"),
            ApiResponse(responseCode = "404", description = "Category not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun updateCategory(
        @Parameter(description = "Category ID") 
        @PathVariable id: String,
        @RequestBody updateCategoryDTO: UpdateBlogCategoryDTO
    ): ResponseEntity<XApiResponse<BlogCategoryDTO>> {
        return try {
            val category = blogCategoryService.updateCategory(id, updateCategoryDTO).get()
            ResponseEntity.ok(XApiResponse.success(category, "Category updated successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to update category: ${e.message}"))
        }
    }
    
    @PostMapping("/{id}/activate")
    @Operation(summary = "Activate category", description = "Activate a category")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Category activated successfully"),
            ApiResponse(responseCode = "404", description = "Category not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun activateCategory(
        @Parameter(description = "Category ID") 
        @PathVariable id: String
    ): ResponseEntity<XApiResponse<BlogCategoryDTO>> {
        return try {
            val category = blogCategoryService.activateCategory(id).get()
            ResponseEntity.ok(XApiResponse.success(category, "Category activated successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to activate category: ${e.message}"))
        }
    }
    
    @PostMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate category", description = "Deactivate a category")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Category deactivated successfully"),
            ApiResponse(responseCode = "404", description = "Category not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun deactivateCategory(
        @Parameter(description = "Category ID") 
        @PathVariable id: String
    ): ResponseEntity<XApiResponse<BlogCategoryDTO>> {
        return try {
            val category = blogCategoryService.deactivateCategory(id).get()
            ResponseEntity.ok(XApiResponse.success(category, "Category deactivated successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to deactivate category: ${e.message}"))
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category", description = "Delete a category")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Category deleted successfully"),
            ApiResponse(responseCode = "404", description = "Category not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun deleteCategory(
        @Parameter(description = "Category ID") 
        @PathVariable id: String
    ): ResponseEntity<XApiResponse<String>> {
        return try {
            blogCategoryService.deleteCategory(id).get()
            ResponseEntity.ok(XApiResponse.success("Category deleted successfully", "Category deleted successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to delete category: ${e.message}"))
        }
    }
    
    @GetMapping("/count")
    @Operation(summary = "Get category count", description = "Get total number of categories")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Category count retrieved successfully"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getCategoryCount(): ResponseEntity<XApiResponse<Long>> {
        return try {
            val count = blogCategoryService.getCategoryCount().get()
            ResponseEntity.ok(XApiResponse.success(count, "Category count retrieved successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to retrieve category count: ${e.message}"))
        }
    }
    
    @GetMapping("/active/count")
    @Operation(summary = "Get active category count", description = "Get number of active categories")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Active category count retrieved successfully"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getActiveCategoryCount(): ResponseEntity<XApiResponse<Long>> {
        return try {
            val count = blogCategoryService.getActiveCategoryCount().get()
            ResponseEntity.ok(XApiResponse.success(count, "Active category count retrieved successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to retrieve active category count: ${e.message}"))
        }
    }
    
    @GetMapping("/top-level/count")
    @Operation(summary = "Get top-level category count", description = "Get number of top-level categories")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Top-level category count retrieved successfully"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getTopLevelCategoryCount(): ResponseEntity<XApiResponse<Long>> {
        return try {
            val count = blogCategoryService.getTopLevelCategoryCount().get()
            ResponseEntity.ok(XApiResponse.success(count, "Top-level category count retrieved successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to retrieve top-level category count: ${e.message}"))
        }
    }
}


