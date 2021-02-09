package com.basalam.storage.repository.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.basalam.storage.repository.local.entity.ProductModel

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(productModel: ProductModel)

    @Delete
    fun deleteProduct(productModel: ProductModel)

    @Query("DELETE FROM products")
    fun deleteProducts()

    @Query("SELECT * FROM products")
    fun getAllProductsLive(): LiveData<List<ProductModel>>
}