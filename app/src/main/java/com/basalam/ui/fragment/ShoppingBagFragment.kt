package com.basalam.ui.fragment

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.basalam.Constants
import com.basalam.R
import com.basalam.storage.viewmodel.ProductViewModel
import com.basalam.ui.adapter.ProductListAdapter
import com.basalam.ui.utils.ViewUtils
import com.basalam.ui.utils.visible
import kotlinx.android.synthetic.main.shopping_bag_fragment.view.*

class ShoppingBagFragment : BaseFragment() {

    init {
        setRootFragment(true)
        setHomeAsUp(true)
        setTitle(R.string.shopping_bag)
    }

    private lateinit var productListAdapter: ProductListAdapter
    private lateinit var shoppingBagItemIds: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val res = inflater.inflate(R.layout.shopping_bag_fragment, container, false)
        val pref = requireContext().getSharedPreferences(
            Constants.SHOPPING_BAG_PREF_NAME,
            Context.MODE_PRIVATE
        )
        shoppingBagItemIds =
            ArrayList(pref.getString(Constants.SHOPPING_BAG_ITEMS, "")!!.split(","))
        productListAdapter = ProductListAdapter(this) { product ->
            context ?: return@ProductListAdapter
            shoppingBagItemIds.remove(product.id)
            pref.edit()
                .putString(Constants.SHOPPING_BAG_ITEMS, shoppingBagItemIds.joinToString(","))
                .apply()
            productListAdapter.products =
                productListAdapter.products.filter { shoppingBagItemIds.contains(it.id) }
            checkEmpty(false, res)
            toast(R.string.removed_from_shopping_bag)
        }
        res.shoppingBagFragListRV.apply {
            layoutManager = GridLayoutManager(
                requireContext(),
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 3 else 2
            )
            adapter = productListAdapter
        }
        return res
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(ProductViewModel::class.java).getProductsLive()
            .observe(viewLifecycleOwner, { items ->
                productListAdapter.products = items.filter { shoppingBagItemIds.contains(it.id) }
                checkEmpty(true, view)
            })
    }

    private fun checkEmpty(animate: Boolean, res: View? = view) {
        res?.apply {
            if (productListAdapter.itemCount == 0) {
                ViewUtils.zoomOutView(shoppingBagFragListRV)
                ViewUtils.zoomInView(shoppingBagFragEmptyLL)
            } else {
                ViewUtils.zoomOutView(shoppingBagFragEmptyLL)
                shoppingBagFragListRV.apply {
                    visible()
                    if (animate) {
                        scheduleLayoutAnimation()
                    }
                }
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        (view?.shoppingBagFragListRV?.layoutManager as GridLayoutManager).spanCount =
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                3
            } else {
                2
            }
    }
}