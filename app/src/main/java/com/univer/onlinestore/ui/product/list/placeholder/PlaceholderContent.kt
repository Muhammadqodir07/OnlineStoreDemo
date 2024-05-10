package com.univer.onlinestore.ui.product.list.placeholder

import com.univer.onlinestore.data.model.Currency
import com.univer.onlinestore.data.model.Product
import com.univer.onlinestore.data.model.ProductCategory
import java.util.Date
import java.util.Random

object PlaceholderContent {

    /**
     * An array of sample (placeholder) items.
     */
    val ITEMS: MutableList<Product> = ArrayList()

    /**
     * A map of sample (placeholder) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, Product> = HashMap()

    private val COUNT = 25

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            addItem(createProduct())
        }
    }

    private fun addItem(item: Product) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id.toString(), item)
    }

    private fun createProduct(): Product {
        return Product(
            id = Date().time.toInt(),
            name = "Наушники",
            description = "Просто описание",
            price = Random().nextDouble()*100.0,
            category = ProductCategory.values().random(),
            currency = Currency.values().random(),
            image = null
        )
    }
}