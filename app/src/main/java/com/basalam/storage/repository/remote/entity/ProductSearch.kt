package com.basalam.storage.repository.remote.entity

import com.google.gson.annotations.SerializedName

data class ProductSearch(
    @SerializedName("products")
    val products: List<Product>
)