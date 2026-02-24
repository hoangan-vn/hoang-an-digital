package com.happyending.blog.controllers

import com.happyending.blog.dtos.*
import com.happyending.blog.entities.BlogStatus
import com.happyending.blog.services.BlogService
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
@RequestMapping("/api/v1/blogs")
@Tag(name = "Blog Management", description = "API for managing blog posts")
class BlogController(
    private val blogService: BlogService
) {
    
    @PostMapping
    @Operation(summary = "Create blog post", description = "Create a new blog post")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Blog created successfully"),
            ApiResponse(responseCode = "400", description = "Invalid blog data"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun createBlog(
        @RequestBody createBlogDTO: CreateBlogDTO
    ): ResponseEntity<XApiResponse<BlogDTO>> {
        return try {
            val blog = blogService.createBlog(createBlogDTO).get()
            ResponseEntity.status(HttpStatus.CREATED)
                .body(XApiResponse.success(blog, "Blog created successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to create blog: ${e.message}"))
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get blog by ID", description = "Get a blog post by its ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Blog retrieved successfully"),
            ApiResponse(responseCode = "404", description = "Blog not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getBlogById(
        @Parameter(description = "Blog ID") 
        @PathVariable id: String
    ): ResponseEntity<XApiResponse<BlogDTO>> {
        return try {
            val blog = blogService.getBlogById(id).get()
            ResponseEntity.ok(XApiResponse.success(blog, "Blog retrieved successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to retrieve blog: ${e.message}"))
        }
    }
    
    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get blog by slug", description = "Get a published blog post by its slug")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Blog retrieved successfully"),
            ApiResponse(responseCode = "404", description = "Blog not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getBlogBySlug(
        @Parameter(description = "Blog slug") 
        @PathVariable slug: String
    ): ResponseEntity<XApiResponse<BlogDTO>> {
        return try {
            val blog = blogService.getPublishedBlogBySlug(slug).get()
            ResponseEntity.ok(XApiResponse.success(blog, "Blog retrieved successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to retrieve blog: ${e.message}"))
        }
    }
    
    @GetMapping
    @Operation(summary = "Get blogs", description = "Get list of blog posts with filtering and pagination")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Blogs retrieved successfully"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getBlogs(
        @Parameter(description = "Blog status") 
        @RequestParam(required = false) status: String?,
        
        @Parameter(description = "Author ID") 
        @RequestParam(required = false) authorId: String?,
        
        @Parameter(description = "Tags (comma-separated)") 
        @RequestParam(required = false) tags: String?,
        
        @Parameter(description = "Categories (comma-separated)") 
        @RequestParam(required = false) categories: String?,
        
        @Parameter(description = "Featured blogs only") 
        @RequestParam(required = false) featured: Boolean?,
        
        @Parameter(description = "Pinned blogs only") 
        @RequestParam(required = false) pinned: Boolean?,
        
        @Parameter(description = "Search term") 
        @RequestParam(required = false) search: String?,
        
        pageable: Pageable
    ): ResponseEntity<XApiResponse<Page<BlogDTO>>> {
        return try {
            val filter = BlogFilterDTO(
                status = status?.let { BlogStatus.valueOf(it) },
                authorId = authorId,
                tags = tags?.split(",")?.map { it.trim() },
                categories = categories?.split(",")?.map { it.trim() },
                isFeatured = featured,
                isPinned = pinned,
                searchTerm = search
            )
            
            val blogs = blogService.getBlogs(filter, pageable).get()
            ResponseEntity.ok(XApiResponse.success(blogs, "Blogs retrieved successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to retrieve blogs: ${e.message}"))
        }
    }
    
    @GetMapping("/featured")
    @Operation(summary = "Get featured blogs", description = "Get list of featured blog posts")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Featured blogs retrieved successfully"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getFeaturedBlogs(pageable: Pageable): ResponseEntity<XApiResponse<Page<BlogDTO>>> {
        return try {
            val blogs = blogService.getFeaturedBlogs(pageable).get()
            ResponseEntity.ok(XApiResponse.success(blogs, "Featured blogs retrieved successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to retrieve featured blogs: ${e.message}"))
        }
    }
    
    @GetMapping("/pinned")
    @Operation(summary = "Get pinned blogs", description = "Get list of pinned blog posts")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Pinned blogs retrieved successfully"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getPinnedBlogs(pageable: Pageable): ResponseEntity<XApiResponse<Page<BlogDTO>>> {
        return try {
            val blogs = blogService.getPinnedBlogs(pageable).get()
            ResponseEntity.ok(XApiResponse.success(blogs, "Pinned blogs retrieved successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to retrieve pinned blogs: ${e.message}"))
        }
    }
    
    @GetMapping("/author/{authorId}")
    @Operation(summary = "Get blogs by author", description = "Get list of blog posts by author")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Author blogs retrieved successfully"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getBlogsByAuthor(
        @Parameter(description = "Author ID") 
        @PathVariable authorId: String,
        pageable: Pageable
    ): ResponseEntity<XApiResponse<Page<BlogDTO>>> {
        return try {
            val blogs = blogService.getBlogsByAuthor(authorId, pageable).get()
            ResponseEntity.ok(XApiResponse.success(blogs, "Author blogs retrieved successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to retrieve author blogs: ${e.message}"))
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update blog", description = "Update a blog post")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Blog updated successfully"),
            ApiResponse(responseCode = "404", description = "Blog not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun updateBlog(
        @Parameter(description = "Blog ID") 
        @PathVariable id: String,
        @RequestBody updateBlogDTO: UpdateBlogDTO
    ): ResponseEntity<XApiResponse<BlogDTO>> {
        return try {
            val blog = blogService.updateBlog(id, updateBlogDTO).get()
            ResponseEntity.ok(XApiResponse.success(blog, "Blog updated successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to update blog: ${e.message}"))
        }
    }
    
    @PostMapping("/{id}/publish")
    @Operation(summary = "Publish blog", description = "Publish a blog post")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Blog published successfully"),
            ApiResponse(responseCode = "404", description = "Blog not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun publishBlog(
        @Parameter(description = "Blog ID") 
        @PathVariable id: String
    ): ResponseEntity<XApiResponse<BlogDTO>> {
        return try {
            val blog = blogService.publishBlog(id).get()
            ResponseEntity.ok(XApiResponse.success(blog, "Blog published successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to publish blog: ${e.message}"))
        }
    }
    
    @PostMapping("/{id}/archive")
    @Operation(summary = "Archive blog", description = "Archive a blog post")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Blog archived successfully"),
            ApiResponse(responseCode = "404", description = "Blog not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun archiveBlog(
        @Parameter(description = "Blog ID") 
        @PathVariable id: String
    ): ResponseEntity<XApiResponse<BlogDTO>> {
        return try {
            val blog = blogService.archiveBlog(id).get()
            ResponseEntity.ok(XApiResponse.success(blog, "Blog archived successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to archive blog: ${e.message}"))
        }
    }
    
    @PostMapping("/{id}/like")
    @Operation(summary = "Like blog", description = "Like a blog post")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Blog liked successfully"),
            ApiResponse(responseCode = "404", description = "Blog not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun likeBlog(
        @Parameter(description = "Blog ID") 
        @PathVariable id: String
    ): ResponseEntity<XApiResponse<BlogDTO>> {
        return try {
            val blog = blogService.likeBlog(id).get()
            ResponseEntity.ok(XApiResponse.success(blog, "Blog liked successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to like blog: ${e.message}"))
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete blog", description = "Delete a blog post")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Blog deleted successfully"),
            ApiResponse(responseCode = "404", description = "Blog not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun deleteBlog(
        @Parameter(description = "Blog ID") 
        @PathVariable id: String
    ): ResponseEntity<XApiResponse<String>> {
        return try {
            blogService.deleteBlog(id).get()
            ResponseEntity.ok(XApiResponse.success("Blog deleted successfully", "Blog deleted successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to delete blog: ${e.message}"))
        }
    }
    
    @GetMapping("/summary")
    @Operation(summary = "Get blog summary", description = "Get blog statistics and summary")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Blog summary retrieved successfully"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getBlogSummary(): ResponseEntity<XApiResponse<BlogSummaryDTO>> {
        return try {
            val summary = blogService.getBlogSummary().get()
            ResponseEntity.ok(XApiResponse.success(summary, "Blog summary retrieved successfully"))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(XApiResponse.error("Failed to retrieve blog summary: ${e.message}"))
        }
    }
}


