package com.basalam.ui.activity

import android.os.Bundle
import com.basalam.ui.fragment.ShoppingBagFragment
import com.basalam.ui.utils.Constants.FRAGMENT_ID
import com.basalam.ui.utils.Constants.SHOPPING_BAG

class RouterActivity : BaseFragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when (intent!!.getIntExtra(FRAGMENT_ID, 0)) {
            SHOPPING_BAG -> showFragment(ShoppingBagFragment(), false)
        }
    }
}