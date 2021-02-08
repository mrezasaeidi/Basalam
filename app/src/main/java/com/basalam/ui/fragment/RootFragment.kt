package com.basalam.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import com.basalam.R
import com.basalam.ui.utils.Intents

class RootFragment : BaseFragment(), SearchProductStateDelegate {

    private var shoppingBagMenu: MenuItem? = null

    init {
        setRootFragment(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val res = FrameLayout(requireContext())
        val content = FrameLayout(requireContext())
        content.id = R.id.content
        res.addView(
            content, FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        val search = FrameLayout(requireContext())
        search.id = R.id.search
        res.addView(
            search, FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        return res
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.beginTransaction()
            .replace(R.id.content, ProductListFragment())
            .replace(R.id.search, SearchProductFragment())
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.root_fragment_menu, menu)
        shoppingBagMenu = menu.findItem(R.id.shoppingBag)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.shoppingBag) {
            startActivity(Intents.openShoppingBag(requireActivity()))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSearchStarted() {
        shoppingBagMenu?.isVisible = false
    }

    override fun onSearchEnded() {
        shoppingBagMenu?.isVisible = true
    }
}