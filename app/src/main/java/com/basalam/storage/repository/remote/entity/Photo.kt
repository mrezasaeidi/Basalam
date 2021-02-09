package com.basalam.storage.repository.remote.entity

import com.google.gson.annotations.SerializedName

data class Photo(
    @SerializedName("url")
    val url: String
)