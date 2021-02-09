package com.basalam.storage.repository.remote.entity

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("productSearch")
    val productSearch: ProductSearch
)