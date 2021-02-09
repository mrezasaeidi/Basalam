package com.basalam.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.basalam.R
import com.basalam.storage.repository.local.entity.ProductModel
import com.basalam.ui.utils.LayoutUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import kotlinx.android.synthetic.main.product_item.view.*

class ProductListAdapter(private val fragment: Fragment) :
    RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {
    private val radius = LayoutUtil.dp(fragment.context!!, 16f).toFloat()
    private val currency = fragment.getString(R.string.currency)
    private val weightUnit = fragment.getString(R.string.weight_unit)
    private var query = ""

    var products = emptyList<ProductModel>()
        set(value) {
            field = value
            if (query.isBlank()) {
                filteredList = value
            }
        }

    private var filteredList = products
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun initSearch(query: String) {
        if (this.query != query) {
            this.query = query
            filteredList = if (query.isBlank()) {
                products
            } else {
                products.filter { it.name.contains(query) || it.vendorName.contains(query) }
            }
        }
    }

    private fun getItem(position: Int) = filteredList[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false).let {
            return ProductViewHolder(it)
        }
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount() = filteredList.size

    inner class ProductViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        fun bind(productModel: ProductModel) {
            itemView.apply {
                Glide.with(fragment).load(productModel.photoUrl)
                    .transform(GranularRoundedCorners(radius, radius, 0f, 0f))
                    .placeholder(R.drawable.ic_photo)
                    .error(R.drawable.ic_photo)
                    .into(productItemImageIV)
                productItemRatingTV.text =
                    LayoutUtil.formatNumber("${productModel.rating ?: ""} (${productModel.ratingCount})")
                productItemNameTV.text = productModel.name
                productItemVendorTV.text = productModel.vendorName
                productItemWeightTV.text =
                    LayoutUtil.formatNumber("$weightUnit ${productModel.weight}")
                productItemPriceTV.text =
                    LayoutUtil.formatNumber("$currency ${LayoutUtil.formatPrice(productModel.price)}")
            }
        }
    }
}