package com.basalam.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.basalam.R
import com.basalam.ui.fragment.BaseFragment
import com.basalam.utils.KeyboardHelper

open class BaseFragmentActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configure ActionBar
        supportActionBar?.apply {
            elevation = 0f
            setDisplayShowHomeEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(true)
            setDisplayShowCustomEnabled(false)
        }


        // Setting basic content
        FrameLayout(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            id = R.id.content_frame
        }.let {
            setContentView(it)
        }
    }

    fun showFragment(fragment: Fragment?, addToBackStack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_frame, fragment!!)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (!stayHere()) {
                    finish()
                }
                return true
            }
        }
        return false
    }

    private fun stayHere(): Boolean {
        return KeyboardHelper.hideKeyboard(this) || supportFragmentManager.popBackStackImmediate() || !canGoBack()
    }

    private fun canGoBack(): Boolean {
        for (fragment in supportFragmentManager.fragments) {
            if (fragment is BaseFragment) {
                if (!fragment.canGoBack()) {
                    return false
                }
            }
        }
        return true
    }

    override fun onBackPressed() {
        if (canGoBack()) {
            super.onBackPressed()
        }
    }
}