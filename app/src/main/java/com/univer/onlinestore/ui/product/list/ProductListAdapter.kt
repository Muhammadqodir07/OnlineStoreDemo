package com.univer.onlinestore.ui.product.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.univer.onlinestore.R
import com.univer.onlinestore.byteArrayToBitmap
import com.univer.onlinestore.data.model.Product
import com.univer.onlinestore.databinding.ItemProductBinding

class ProductListAdapter(private val onClick: (Product) -> Unit, private val onAddToCartClick: (Int) -> Unit) :
    ListAdapter<Product, ProductListAdapter.ProductViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onClick,
            onAddToCartClick
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ProductViewHolder(
        private val binding: ItemProductBinding,
        private val onClick: (Product) -> Unit,
        private val onAddToCartClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            with(binding) {
                productName.text = product.name
                productPrice.text = product.currency.showWithCurrency(product.price)
                productDescription.text = product.description
                try {
                    val bitmap = byteArrayToBitmap(product.image)
                    productImage.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    binding.productImage.setImageResource(R.drawable.empty_image)
                }

                addToCartButton.setOnClickListener {
                    onAddToCartClick(product.id)
                }
                root.setOnClickListener {
                    onClick(product)
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem == newItem
        }
    }
}