package com.basalam.storage.repository.remote.entity

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("data")
    val data: Data
)