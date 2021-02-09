package com.basalam.ui.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.basalam.R
import com.basalam.storage.ProductViewModel
import com.basalam.ui.adapter.ProductListAdapter
import com.basalam.ui.utils.ViewUtils
import com.basalam.ui.utils.visible
import kotlinx.android.synthetic.main.product_list_fragment.view.*

class ProductListFragment : BaseFragment() {
    private lateinit var productListAdapter: ProductListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val res = inflater.inflate(R.layout.product_list_fragment, container, false)
        productListAdapter = ProductListAdapter(this)
        res.productListFragListSR.setOnRefreshListener {
            loadData(res)
        }
        res.productListFragListRV.apply {
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
        view.productListFragListSR.isRefreshing = true

        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(ProductViewModel::class.java)
            .getProductsLive().observe(viewLifecycleOwner, {
                productListAdapter.products = it
                view.apply {
                    productListFragListSR.isRefreshing = false
                    if (it.isEmpty()) {
                        ViewUtils.zoomOutView(productListFragListRV)
                        ViewUtils.zoomInView(productListFragEmptyTV)
                    } else {
                        ViewUtils.zoomOutView(productListFragEmptyTV)
                        productListFragListRV.apply {
                            visible()
                            scheduleLayoutAnimation()
                        }
                    }
                }
            })
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        (view?.productListFragListRV?.layoutManager as GridLayoutManager).spanCount =
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                3
            } else {
                2
            }
    }

    private fun loadData(res: View) {

    }
}