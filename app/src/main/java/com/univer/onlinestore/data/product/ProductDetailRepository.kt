package com.univer.onlinestore.data.product

import com.univer.onlinestore.data.model.Product
import com.univer.onlinestore.data.model.ProductFilter
import kotlinx.coroutines.flow.Flow

interface ProductDetailRepository {
    fun addProduct(product: Product): Long
    fun getProductById(productId: Int): Product?
    fun getFilteredProducts(filter: ProductFilter): Flow<List<Product>>
    fun getAllProducts(): Flow<List<Product>>
    fun updateProduct(product: Product): Int
    fun deleteProduct(productId: Int): Int
}