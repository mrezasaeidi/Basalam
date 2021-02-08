package com.basalam.ui.fragment

import android.os.Bundle
import android.view.*
import com.basalam.R

class RootFragment : BaseFragment() {

    init {
        setRootFragment(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.root_fragment, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.root_fragment_menu, menu)
    }
}