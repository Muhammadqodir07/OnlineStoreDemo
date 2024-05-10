package com.univer.onlinestore.data.product

import android.content.ContentValues
import android.content.Context
import androidx.core.database.getBlobOrNull
import com.univer.onlinestore.data.model.Currency
import com.univer.onlinestore.data.model.Product
import com.univer.onlinestore.data.model.ProductCategory
import com.univer.onlinestore.data.model.ProductFilter
import com.univer.onlinestore.data.product.db.ProductContract
import com.univer.onlinestore.data.product.db.ProductDbHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProductDetailRepositoryImpl @Inject constructor(@ApplicationContext private val context: Context) : ProductDetailRepository {
    private val dbHelper = ProductDbHelper(context)

    override fun addProduct(product: Product): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(ProductContract.ProductEntry.COLUMN_NAME, product.name)
            put(ProductContract.ProductEntry.COLUMN_DESCRIPTION, product.description)
            put(ProductContract.ProductEntry.COLUMN_PRICE, product.price)
            put(ProductContract.ProductEntry.COLUMN_IMAGES, product.image)
            put(ProductContract.ProductEntry.COLUMN_CURRENCY, product.currency.name)
            put(ProductContract.ProductEntry.COLUMN_CATEGORY, product.category.name)
        }
        return db.insert(ProductContract.ProductEntry.TABLE_NAME, null, values)
    }

    override fun getProductById(productId: Int): Product? {
        val db = dbHelper.readableDatabase
        val projection = arrayOf(ProductContract.ProductEntry.COLUMN_ID, ProductContract.ProductEntry.COLUMN_NAME, ProductContract.ProductEntry.COLUMN_DESCRIPTION, ProductContract.ProductEntry.COLUMN_PRICE, ProductContract.ProductEntry.COLUMN_IMAGES, ProductContract.ProductEntry.COLUMN_CURRENCY, ProductContract.ProductEntry.COLUMN_CATEGORY)
        val selection = "${ProductContract.ProductEntry.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(productId.toString())

        db.query(
            ProductContract.ProductEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        ).use { cursor ->
            if (cursor.moveToFirst()) {
                return Product(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_NAME)),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_DESCRIPTION)),
                    price = cursor.getDouble(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRICE)),
                    image = cursor.getBlobOrNull(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_IMAGES)),
                    currency = Currency.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(
                        ProductContract.ProductEntry.COLUMN_CURRENCY))),
                    category = ProductCategory.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_CATEGORY)))
                )
            }
            return null
        }
    }

    override fun getFilteredProducts(filter: ProductFilter): Flow<List<Product>> = flow {
        val db = dbHelper.readableDatabase
        val products = mutableListOf<Product>()
        val selectionArgs = mutableListOf<String>()
        val selection = StringBuilder()

        filter.category?.let {
            selection.append("${ProductContract.ProductEntry.COLUMN_CATEGORY} = ? ")
            selectionArgs.add(it.name)
        }

        filter.currency?.let {
            if (selection.isNotEmpty()) selection.append("AND ")
            selection.append("${ProductContract.ProductEntry.COLUMN_CURRENCY} = ? ")
            selectionArgs.add(it.name)
        }

        filter.minPrice?.let {
            if (selection.isNotEmpty()) selection.append("AND ")
            selection.append("${ProductContract.ProductEntry.COLUMN_PRICE} >= ? ")
            selectionArgs.add(it.toString())
        }

        filter.maxPrice?.let {
            if (selection.isNotEmpty()) selection.append("AND ")
            selection.append("${ProductContract.ProductEntry.COLUMN_PRICE} <= ? ")
            selectionArgs.add(it.toString())
        }

        db.query(
            ProductContract.ProductEntry.TABLE_NAME,
            null,
            selection.toString(),
            selectionArgs.toTypedArray(),
            null,
            null,
            null
        ).use { cursor ->
            while (cursor.moveToNext()) {
                products.add(
                    Product(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_ID)),
                        name = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_NAME)),
                        description = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_DESCRIPTION)),
                        price = cursor.getDouble(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRICE)),
                        image = cursor.getBlobOrNull(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_IMAGES)),
                        currency = Currency.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_CURRENCY))),
                        category = ProductCategory.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_CATEGORY)))
                    )
                )
            }
        }
        emit(products)
    }.flowOn(Dispatchers.IO)

    override fun getAllProducts(): Flow<List<Product>> = flow {
        val products = mutableListOf<Product>()
        val db = dbHelper.readableDatabase

        db.query(
            ProductContract.ProductEntry.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        ).use { cursor ->
            while (cursor.moveToNext()) {
                products.add(
                    Product(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_ID)),
                        name = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_NAME)),
                        description = cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_DESCRIPTION)),
                        price = cursor.getDouble(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRICE)),
                        image = cursor.getBlobOrNull(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_IMAGES)),
                        currency = Currency.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_CURRENCY))),
                        category = ProductCategory.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_CATEGORY)))
                    )
                )
            }
        }
        emit(products)  // Emit the list of products once all products have been added to the list
    }.flowOn(Dispatchers.IO)

    override fun updateProduct(product: Product): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(ProductContract.ProductEntry.COLUMN_NAME, product.name)
            put(ProductContract.ProductEntry.COLUMN_DESCRIPTION, product.description)
            put(ProductContract.ProductEntry.COLUMN_PRICE, product.price)
            put(ProductContract.ProductEntry.COLUMN_IMAGES, product.image)
            put(ProductContract.ProductEntry.COLUMN_CURRENCY, product.currency.name)
            put(ProductContract.ProductEntry.COLUMN_CATEGORY, product.category.name)
        }
        return db.update(
            ProductContract.ProductEntry.TABLE_NAME,
            values,
            "${ProductContract.ProductEntry.COLUMN_ID} = ?",
            arrayOf(product.id.toString())
        )
    }

    override fun deleteProduct(productId: Int): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            ProductContract.ProductEntry.TABLE_NAME,
            "${ProductContract.ProductEntry.COLUMN_ID} = ?",
            arrayOf(productId.toString())
        )
    }
}