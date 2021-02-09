package com.basalam.storage.repository.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.basalam.storage.repository.remote.entity.Product
import org.jetbrains.annotations.NotNull

@Entity(tableName = "products")
data class ProductModel(
    @NotNull
    @PrimaryKey
    val id: String,
    val name: String,
    val photoUrl: String,
    val vendorName: String,
    val weight: Int,
    val price: Long,
    val rating: Double?,
    val ratingCount: Int
) {
    companion object {
        fun create(product: Product): ProductModel {
            return ProductModel(
                product.id,
                product.name,
                product.photo.url,
                product.vendor.name,
                product.weight,
                product.price,
                product.rating.rating,
                product.rating.count
            )
        }
    }
}