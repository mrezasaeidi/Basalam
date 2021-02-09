package com.basalam.ui.fragment

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {
    private var isRootFragment = false
    private var title: String? = null
    private var titleRes = 0
    private var subtitle: String? = null
    private var showTitle = true
    private var homeAsUp = false
    private var showHome = false
    private var showCustom = false

    fun setRootFragment(rootFragment: Boolean) {
        isRootFragment = rootFragment
        setHasOptionsMenu(rootFragment)
    }

    fun setTitle(title: String) {
        this.title = title
        titleRes = 0
        getSupportActionBar()?.title = title
    }

    fun setTitle(titleRes: Int) {
        title = null
        this.titleRes = titleRes
        getSupportActionBar()?.setTitle(titleRes)
    }

    fun setSubtitle(subtitle: String?) {
        this.subtitle = subtitle
        getSupportActionBar()?.subtitle = subtitle
    }

    fun setShowTitle(showTitle: Boolean) {
        this.showTitle = showTitle
        getSupportActionBar()?.setDisplayShowTitleEnabled(showTitle)
    }

    fun setHomeAsUp(homeAsUp: Boolean) {
        this.homeAsUp = homeAsUp
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(homeAsUp)
    }

    fun setShowHome(showHome: Boolean) {
        this.showHome = showHome
        getSupportActionBar()?.setDisplayShowHomeEnabled(showHome)
    }

    fun setShowCustom(showCustom: Boolean) {
        this.showCustom = showCustom
        getSupportActionBar()?.setDisplayShowCustomEnabled(showCustom)
    }

    fun canGoBack(): Boolean {
        return true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (isRootFragment) {
            val actionBar = getSupportActionBar()
            if (actionBar != null) {
                if (titleRes != 0) {
                    actionBar.setTitle(titleRes)
                } else {
                    actionBar.title = title
                }
                actionBar.subtitle = subtitle
                actionBar.setDisplayShowCustomEnabled(showCustom)
                actionBar.setDisplayHomeAsUpEnabled(homeAsUp)
                actionBar.setDisplayShowHomeEnabled(showHome)
                actionBar.setDisplayShowTitleEnabled(showTitle)
                onConfigureActionBar(actionBar)
            }
        }
    }

    open fun getSupportActionBar(): ActionBar? {
        val activity = activity
        if (activity is AppCompatActivity) {
            return activity.supportActionBar
        }
        return null
    }

    fun onConfigureActionBar(actionBar: ActionBar?) {}
}