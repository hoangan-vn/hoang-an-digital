package com.happyending.product.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size
import java.math.BigDecimal

@Schema(description = "Data transfer object for creating a new product")
data class CreateProductDTO(
    @field:NotEmpty
    @field:Size(max = 255)
    @Schema(description = "Product name", example = "iPhone 15", required = true, maxLength = 255)
    val name: String,

    @field:DecimalMin(value = "0.0", inclusive = false)
    @Schema(description = "Product price", example = "999.99", required = true, minimum = "0.0")
    val price: BigDecimal,

    @Schema(description = "Product description", example = "Latest iPhone with advanced features")
    val description: String?
)
