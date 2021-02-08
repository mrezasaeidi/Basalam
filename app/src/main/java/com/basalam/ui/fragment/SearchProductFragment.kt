package com.basalam.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basalam.R
import com.basalam.ui.utils.gone
import kotlinx.android.synthetic.main.global_search_fragment.*
import kotlinx.android.synthetic.main.global_search_fragment.view.*

class SearchProductFragment : BaseFragment() {
    init {
        setRootFragment(true)
    }

    private var searchMenuItem: MenuItem? = null
    private var searchView: SearchView? = null
    private var isSearchVisible = false
    private var searchQuery: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val res = inflater.inflate(R.layout.global_search_fragment, container, false)
        res.gone()
        initSearchResultList(res)
        return res
    }

    private fun initSearchResultList(res: View) {
        res.searchListRV.layoutManager = GridLayoutManager(res.context, 2)
        res.searchListRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING && isSearchVisible) {
                    searchView?.clearFocus()
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {}
        })
        res.searchHintTV.gone()
        res.searchEmptyTV.gone()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_product_fragment_menu, menu)
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
                searchQuery = s.trim { it <= ' ' }
                if (isSearchVisible) {
                    //TODO
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
        showView(searchHintTV, false)
        goneView(searchEmptyTV, false)
        showView(searchContainerFL, false)
        if (parentFragment is SearchProductStateDelegate) {
            (parentFragment as SearchProductStateDelegate).onSearchStarted()
        }
    }

    private fun hideSearch() {
        if (!isSearchVisible) {
            return
        }
        isSearchVisible = false
        searchQuery = null
        goneView(searchContainerFL, false)
        if (searchMenuItem?.isActionViewExpanded == true) {
            searchMenuItem?.collapseActionView()
        }
        if (parentFragment is SearchProductStateDelegate) {
            (parentFragment as SearchProductStateDelegate).onSearchEnded()
        }
    }
}