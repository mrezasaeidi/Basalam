package com.basalam.storage.repository

import androidx.lifecycle.LiveData
import com.basalam.storage.repository.local.dao.ProductDao
import com.basalam.storage.repository.local.entity.ProductModel
import com.basalam.storage.repository.remote.ProductWebService
import com.basalam.storage.repository.remote.entity.ProductQuery
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProductRepository(private val productDao: ProductDao) {
    private val webservice = ProductWebService.invoke()

    fun getProducts(): LiveData<List<ProductModel>> {
//        refreshProducts()
        return productDao.getAllProductsLive()
    }


    fun refreshProducts() {
        val query =
            ProductQuery("{productSearch(size: 20) {products {id name photo(size: LARGE) { url } vendor { name } weight price rating { rating count: signals } } } }")
        GlobalScope.launch {
            webservice.getProducts(query)
                .body().data.productSearch.products.map { ProductModel.create(it) }
                .forEach {
                    productDao.insertOrUpdate(it)
                }
        }
    }
}