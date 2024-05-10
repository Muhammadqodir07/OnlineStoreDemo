package com.univer.onlinestore.ui.product.list.filter

import com.univer.onlinestore.data.model.ProductFilter

interface FilterDialogListener {
    fun onFiltersApplied(filter: ProductFilter)
}