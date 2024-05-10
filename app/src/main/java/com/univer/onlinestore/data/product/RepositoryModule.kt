package com.univer.onlinestore.data.product

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductDetailRepositoryImpl
    ): ProductDetailRepository
}