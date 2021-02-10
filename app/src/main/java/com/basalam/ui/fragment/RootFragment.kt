package com.basalam.ui.fragment

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basalam.Constants
import com.basalam.R
import com.basalam.storage.repository.LoadingState
import com.basalam.storage.viewmodel.ProductViewModel
import com.basalam.ui.adapter.ProductListAdapter
import com.basalam.ui.utils.Intents
import com.basalam.ui.utils.ViewUtils
import com.basalam.ui.utils.visible
import kotlinx.android.synthetic.main.root_fragment.view.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class RootFragment : BaseFragment() {

    init {
        setRootFragment(true)
    }

    private lateinit var productListAdapter: ProductListAdapter
    private var searchMenuItem: MenuItem? = null
    private var searchView: SearchView? = null
    private var isSearchVisible = false
    private var shoppingBagMenu: MenuItem? = null
    private lateinit var viewModel: ProductViewModel
    private var isRefreshed = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val res = inflater.inflate(R.layout.root_fragment, container, false)
        toastLong(R.string.refresh_alert)
        productListAdapter = ProductListAdapter(this) { product ->
            context ?: return@ProductListAdapter
            val pref = requireContext().getSharedPreferences(
                Constants.SHOPPING_BAG_PREF_NAME,
                Context.MODE_PRIVATE
            )
            val items = ArrayList(pref.getString(Constants.SHOPPING_BAG_ITEMS, "")!!.split(","))
            items.removeAll { it == product.id }
            items.add(product.id)
            pref.edit().putString(Constants.SHOPPING_BAG_ITEMS, items.joinToString(",")).apply()
            toast(R.string.added_to_shopping_bag)
        }
        res.productListFragListSR.setOnRefreshListener {
            refreshData(res)
        }
        res.productListFragListRV.apply {
            layoutManager = GridLayoutManager(
                requireContext(),
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 3 else 2
            )
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING && isSearchVisible) {
                        searchView?.clearFocus()
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {}
            })
            adapter = productListAdapter
        }
        return res
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
                .create(ProductViewModel::class.java)
        viewModel.getProductsLive().observe(viewLifecycleOwner, {
            productListAdapter.products = it
            checkEmpty(true, view)
            if (needRefreshData() || (it.isEmpty() && !isRefreshed)) {
                refreshData(view)
                return@observe
            }
        })
    }

    private fun checkEmpty(animate: Boolean, res: View? = view) {
        res?.apply {
            if (productListAdapter.itemCount == 0) {
                ViewUtils.zoomOutView(productListFragListRV)
                ViewUtils.zoomInView(productListFragEmptyTV)
            } else {
                ViewUtils.zoomOutView(productListFragEmptyTV)
                productListFragListRV.apply {
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
        (view?.productListFragListRV?.layoutManager as GridLayoutManager).spanCount =
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                3
            } else {
                2
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.root_fragment_menu, menu)
        shoppingBagMenu = menu.findItem(R.id.shoppingBag)
        searchMenuItem = menu.findItem(R.id.searchMenuItem)
        searchMenuItem!!.isVisible = true
        searchView = searchMenuItem!!.actionView as SearchView
        searchView!!.setIconifiedByDefault(true)
        searchMenuItem!!.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                showSearch()
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                hideSearch()
                return true
            }
        })
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                if (isSearchVisible) {
                    initSearch(s.trim())
                }
                return false
            }
        })
    }

    private fun showSearch() {
        if (isSearchVisible) {
            return
        }
        isSearchVisible = true
        shoppingBagMenu?.isVisible = false
    }

    private fun hideSearch() {
        if (!isSearchVisible) {
            return
        }
        isSearchVisible = false
        shoppingBagMenu?.isVisible = true
        if (searchMenuItem?.isActionViewExpanded == true) {
            searchMenuItem?.collapseActionView()
        }
        initSearch("")
    }

    private fun initSearch(query: String) {
        productListAdapter.initSearch(query).let {
            if (it) {
                checkEmpty(true)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.shoppingBag) {
            startActivity(Intents.openShoppingBag(requireActivity()))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun refreshData(res: View) {
        isRefreshed = true
        viewModel.refreshProducts().observe(viewLifecycleOwner, {
            when (it) {
                LoadingState.LOADING -> {
                    res.productListFragListSR.isRefreshing = true
                }
                LoadingState.LOAD_SUCCESS -> {
                    res.productListFragListSR.isRefreshing = false
                }
                LoadingState.LOAD_FAILED -> {
                    toast(R.string.error_connection)
                    res.productListFragListSR.isRefreshing = false
                }
            }
        })
        context?.getSharedPreferences(Constants.LAST_UPDATE_PREF_NAME, Context.MODE_PRIVATE)?.edit()
            ?.putLong(
                Constants.LAST_UPDATE_DATE,
                Date().time
            )?.apply()
    }

    private fun needRefreshData(): Boolean {
        val lastUpdate =
            (context?.getSharedPreferences(Constants.LAST_UPDATE_PREF_NAME, Context.MODE_PRIVATE)
                ?.getLong(Constants.LAST_UPDATE_DATE, 0) ?: 0)
        val now = Date().time
        return TimeUnit.MILLISECONDS.toMinutes(now - lastUpdate) > Constants.LAST_UPDATE_THRESHOLD
    }
}