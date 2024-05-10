package com.univer.onlinestore.data.model

data class ProductFilter(
    val category: ProductCategory? = null,
    val currency: Currency? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null
)
