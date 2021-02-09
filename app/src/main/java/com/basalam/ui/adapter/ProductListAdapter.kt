package com.basalam.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.basalam.R
import com.basalam.storage.repository.local.entity.ProductModel
import com.basalam.ui.utils.LayoutUtil
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.product_item.view.*

class ProductListAdapter : RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {
    var products = emptyList<ProductModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private fun getItem(position: Int) = products[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false).let {
            return ProductViewHolder(it)
        }
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount() = products.size

    class ProductViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        fun bind(productModel: ProductModel) {
            itemView.apply {
                Glide.with(itemView.context).load(productModel.photoUrl).into(productItemImageIV)
                productItemRatingTV.text =
                    LayoutUtil.formatNumber(productModel.ratingCount.toString())
                productItemNameTV.text = productModel.name
                productItemVendorTV.text = productModel.vendorName
                productItemWeightTV.text = LayoutUtil.formatNumber(productModel.weight.toString())
                productItemPriceTV.text = LayoutUtil.formatNumber(productModel.price.toString())
            }
        }
    }
}