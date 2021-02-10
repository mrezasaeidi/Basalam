package com.basalam.storage.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.basalam.storage.repository.ProductRepository
import com.basalam.storage.repository.local.LocalDatabase
import com.basalam.storage.repository.local.entity.ProductModel

class ProductViewModel(application: Application) : AndroidViewModel(application) {
    private val productDao = LocalDatabase.getDatabase(application).productDao()
    private val productRepository = ProductRepository(productDao)

    fun getProductsLive(): LiveData<List<ProductModel>> {
        return productRepository.getProducts()
    }

    fun refreshProducts() {
        productRepository.refreshProducts()
    }
}