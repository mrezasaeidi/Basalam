package com.basalam.ui.activity

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.basalam.R
import com.basalam.Style

class RootActivity : BaseFragmentActivity() {

    init {
        canSwipe = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.root_activity)
        window.setBackgroundDrawable(ColorDrawable(Style.getBackgroundColor(this)))
    }
}