package com.univer.onlinestore.data.model

enum class Currency {
    DOLLAR,
    SUM,
    RUBLE;

    fun showWithCurrency(price: Double): String {
        return when (this) {
            DOLLAR -> "$ $price"
            SUM -> "$price so'm"
            RUBLE -> "$price ₽"
        }
    }

    fun getCurrency(value: String): Currency? {
        return try {
            Currency.valueOf(value)
        } catch (e: Exception) {
            null
        }
    }
}