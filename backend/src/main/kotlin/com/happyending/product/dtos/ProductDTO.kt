package com.happyending.product.dtos

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

data class ProductDTO(
    @Schema(description = "Unique identifier for the product", example = "12345")
    val id: String,

    @Schema(description = "Product name", example = "iPhone 15")
    val name: String,

    @Schema(description = "Product price", example = "999.99")
    val price: BigDecimal,

    @Schema(description = "Product description", example = "Latest iPhone with advanced features")
    val description: String?
)
