package com.happyending.product.mapper

import com.happyending.product.dtos.ProductDTO
import com.happyending.product.entities.Product
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface ProductMapper {
    fun toDto(user: Product): ProductDTO
    fun toEntity(dto: ProductDTO): Product
}