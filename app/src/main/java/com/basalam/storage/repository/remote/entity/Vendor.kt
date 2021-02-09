package com.basalam.storage.repository.remote.entity

import com.google.gson.annotations.SerializedName

data class Vendor(
    @SerializedName("name")
    val name: String
)