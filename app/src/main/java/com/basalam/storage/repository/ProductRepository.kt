package com.basalam.storage.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.basalam.storage.repository.local.dao.ProductDao
import com.basalam.storage.repository.local.entity.ProductModel
import com.basalam.storage.repository.remote.ProductWebService
import com.basalam.storage.repository.remote.entity.ProductQuery
import com.basalam.storage.repository.remote.entity.ProductResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductRepository(private val productDao: ProductDao) {
    private val webservice = ProductWebService.invoke()

    fun getProducts(): LiveData<List<ProductModel>> {
        return productDao.getAllProductsLive()
    }

    fun refreshProducts(): LiveData<LoadingState> {
        val loadingState = MutableLiveData<LoadingState>()
        loadingState.value = LoadingState.LOADING
        val query =
            ProductQuery("{productSearch(size: 20) {products {id name photo(size: LARGE) { url } vendor { name } weight price rating { rating count: signals } } } }")

        webservice.getProducts(query).enqueue(object : Callback<ProductResponse> {
            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>
            ) {
                GlobalScope.launch {
                    response.body().data.productSearch.products.map { ProductModel.create(it) }
                        .forEach {
                            productDao.insertOrUpdate(it)
                        }
                }
                loadingState.value = LoadingState.LOAD_SUCCESS
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                call.cancel()
                loadingState.value = LoadingState.LOAD_FAILED
            }
        })
        return loadingState
    }
}