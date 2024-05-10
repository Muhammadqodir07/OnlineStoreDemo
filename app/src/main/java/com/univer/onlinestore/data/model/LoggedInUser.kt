package com.univer.onlinestore.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class LoggedInUser(
    val userId: String,
    val displayName: String,
    val cart: List<Product>? = null
) : Parcelable