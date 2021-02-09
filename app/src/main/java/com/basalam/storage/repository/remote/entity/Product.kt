package com.basalam.storage.repository.remote.entity

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("photo")
    val photo: Photo,
    @SerializedName("price")
    val price: Long,
    @SerializedName("rating")
    val rating: Rating,
    @SerializedName("vendor")
    val vendor: Vendor,
    @SerializedName("weight")
    val weight: Int
)