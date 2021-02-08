package com.basalam.ui.activity

import android.os.Bundle
import com.basalam.ui.fragment.RootFragment

class RootActivity : BaseFragmentActivity() {

    init {
        canSwipe = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showFragment(RootFragment(), false)
    }
}