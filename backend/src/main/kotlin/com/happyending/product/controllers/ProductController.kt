package com.happyending.product.controllers

import com.happyending.common.responses.XApiResponse
import com.happyending.product.dtos.CreateProductDTO
import com.happyending.product.dtos.ProductDTO
import com.happyending.product.services.ProductService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Product Management", description = "APIs for managing products")
class ProductController(private val productService: ProductService) {

    @PostMapping
    @Operation(
        summary = "Create a new product",
        description = "Creates a new product with the provided information"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Product created successfully",
                content = [Content(schema = Schema(implementation = ProductDTO::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid input data"
            )
        ]
    )
    @SecurityRequirement(name = "bearerAuth")
    fun createProduct(
        @Parameter(description = "Product creation data")
        @Valid @RequestBody productCreationDto: CreateProductDTO
    ): ResponseEntity<XApiResponse<ProductDTO>> {
        val product = productService.createProduct(productCreationDto)
        val response = XApiResponse.success(product, "Product created successfully")
        return ResponseEntity.created(URI.create("/api/products/${product.id}")).body(response)
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get product by ID",
        description = "Retrieves a product by its unique identifier"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Product found",
                content = [Content(schema = Schema(implementation = ProductDTO::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Product not found"
            )
        ]
    )
    fun getProduct(
        @Parameter(description = "Product ID", required = true)
        @PathVariable id: String
    ): ResponseEntity<XApiResponse<ProductDTO>> {
        return productService.getProductById(id)
            ?.let {
                val response = XApiResponse.success(it, "Product found")
                ResponseEntity.ok(response)
            }
            ?: ResponseEntity.status(HttpStatus.NOT_FOUND).body(XApiResponse.error("Product not found"))
    }

    @GetMapping
    @Operation(
        summary = "Get all products",
        description = "Retrieves a list of all available products"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "List of products retrieved successfully",
                content = [Content(schema = Schema(implementation = Array<ProductDTO>::class))]
            )
        ]
    )
    fun getAllProducts(): ResponseEntity<XApiResponse<List<ProductDTO>>> {
        val products = productService.getAllProducts()
        val response = XApiResponse.success(products, "List of products retrieved successfully")
        return ResponseEntity.ok(response)
    }
}
