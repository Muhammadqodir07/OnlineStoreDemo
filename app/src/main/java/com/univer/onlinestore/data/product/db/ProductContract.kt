package com.univer.onlinestore.data.product.db

import android.provider.BaseColumns

object ProductContract {
    object ProductEntry : BaseColumns {
        const val TABLE_NAME = "products"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_PRICE = "price"
        const val COLUMN_IMAGES = "images"
        const val COLUMN_CURRENCY = "currency"
        const val COLUMN_CATEGORY = "category"
    }
}