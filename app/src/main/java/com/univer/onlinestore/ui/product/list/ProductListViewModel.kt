package com.univer.onlinestore.ui.product.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.univer.onlinestore.data.model.Product
import com.univer.onlinestore.data.model.ProductFilter
import com.univer.onlinestore.data.product.ProductDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(private val repository: ProductDetailRepository) :
    ViewModel() {
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    init {
        fetchProducts()
    }

    fun fetchProducts() {
        viewModelScope.launch {
            repository.getAllProducts().collect { productList ->
                _products.postValue(productList)
            }
        }
    }

    fun applyFilter(filter: ProductFilter) {
        viewModelScope.launch {
            repository.getFilteredProducts(filter).collect { filteredProducts ->
                _products.postValue(filteredProducts)
            }
        }
    }
}