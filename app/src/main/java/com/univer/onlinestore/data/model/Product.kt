package com.univer.onlinestore.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Product (
    var id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val image: ByteArray?,
    val currency: Currency,
    val category: ProductCategory
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        if (id != other.id) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (price != other.price) return false
        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false
        if (currency != other.currency) return false
        if (category != other.category) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + (image?.contentHashCode() ?: 0)
        result = 31 * result + currency.hashCode()
        result = 31 * result + category.hashCode()
        return result
    }
}