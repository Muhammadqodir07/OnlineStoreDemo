package com.univer.onlinestore.ui.product.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.univer.onlinestore.data.model.Currency
import com.univer.onlinestore.data.model.Product
import com.univer.onlinestore.data.model.ProductCategory
import com.univer.onlinestore.data.product.ProductDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(private val repository: ProductDetailRepository) : ViewModel() {
    private val _product = MutableLiveData<Product?>()
    val product: LiveData<Product?> = _product

    private var isNewProduct = true

    fun saveProduct(name: String, description: String, price: Double, image: ByteArray?, category: ProductCategory, currency: Currency) {
        val newProduct = Product(
            id = _product.value?.id ?: 0,
            name = name,
            description = description,
            price = price,
            image = image,
            category = category,
            currency = currency
        )

        viewModelScope.launch {
            if (isNewProduct) {
                val result = repository.addProduct(newProduct)
                if (result != -1L) {
                    _product.postValue(newProduct.apply { id = result.toInt() })
                    isNewProduct = false
                }
            } else {
                val result = repository.updateProduct(newProduct)
                if (result > 0) {
                    _product.postValue(newProduct)
                }
            }
        }
    }

    fun loadProduct(productId: Int?) {
        viewModelScope.launch {
            productId?.let {
                val product = repository.getProductById(it)
                _product.postValue(product)
                isNewProduct = product == null
            } ?: run {
                _product.postValue(null)
                isNewProduct = true
            }
        }
    }
}