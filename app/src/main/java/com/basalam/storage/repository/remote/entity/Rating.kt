package com.basalam.storage.repository.remote.entity

import com.google.gson.annotations.SerializedName

data class Rating(
    @SerializedName("count")
    val count: Int,
    @SerializedName("rating")
    val rating: Double?
)