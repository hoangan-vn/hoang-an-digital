package com.happyending.product.repositories

import com.happyending.product.entities.Product
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : MongoRepository<Product, String> {
    // You can add custom query methods here if needed
    // fun findByName(name: String): List<Product>
}