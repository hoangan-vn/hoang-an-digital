package com.happyending.product.services

import com.happyending.product.dtos.CreateProductDTO
import com.happyending.product.dtos.ProductDTO
import com.happyending.product.entities.Product
import com.happyending.product.repositories.ProductRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProductService(private val productRepository: ProductRepository) {

    fun createProduct(productCreationDto: CreateProductDTO): ProductDTO {
        val product = Product(
            name = productCreationDto.name,
            price = productCreationDto.price,
            description = productCreationDto.description
        )
        val savedProduct = productRepository.save(product)
        return toDto(savedProduct)
    }

    @Transactional(readOnly = true)
    fun getProductById(id: String): ProductDTO? {
        return productRepository.findByIdOrNull(id)?.let { toDto(it) }
    }

    @Transactional(readOnly = true)
    fun getAllProducts(): List<ProductDTO> {
        return productRepository.findAll().map { toDto(it) }
    }

    private fun toDto(product: Product): ProductDTO {
        return ProductDTO(
            id = product.id!!,
            name = product.name,
            price = product.price,
            description = product.description
        )
    }
}