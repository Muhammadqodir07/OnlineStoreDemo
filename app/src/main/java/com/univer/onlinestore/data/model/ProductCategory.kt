package com.univer.onlinestore.data.model

enum class ProductCategory {
    ELECTRONICS,
    CLOTHING_APPAREL,
    HOME_KITCHEN,
    BOOKS_MAGAZINES,
    TOYS_GAMES,
    HEALTH_BEAUTY,
    SPORTS_OUTDOORS,
    AUTOMOTIVE,
    JEWELRY_WATCHES,
    TOOLS_HOMEIMPROVEMENT,
    GROCERY_GOURMETFOOD,
    MOVIES_TVSHOWS,
    PETSUPPLIES,
    FURNITURE_DECOR,
    BABY_KIDS;

    fun getCategory(value: String): ProductCategory? {
        return try {
            ProductCategory.valueOf(value)
        } catch (e: Exception) {
            null
        }
    }
}