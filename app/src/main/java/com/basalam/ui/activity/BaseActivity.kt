package com.basalam.ui.activity

import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.basalam.Application
import com.basalam.R
import com.basalam.Style
import com.jude.swipbackhelper.SwipeBackHelper

open class BaseActivity : AppCompatActivity() {
    var canSwipe = true
        set(value) {
            field = value
            try {
                SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(value)
            } catch (ignore: Exception) {
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        Application.setLocale(this)
        super.onCreate(savedInstanceState)
        setTheme()
        SwipeBackHelper.onCreate(this)
        SwipeBackHelper.getCurrentPage(this).setSwipeSensitivity(0.2f)
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(canSwipe)
        window.setBackgroundDrawable(
            ColorDrawable(
                if (canSwipe) Color.TRANSPARENT else Style.getBackgroundColor(this)
            )
        )

        //tint back icon
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.grey_500)
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        SwipeBackHelper.onPostCreate(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Application.setLocale(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        SwipeBackHelper.onDestroy(this)
    }

    // Toolbar
    protected open fun setToolbar(text: Int, enableBack: Boolean) {
        if (supportActionBar == null) {
            throw RuntimeException("Action bar is not set!")
        }
        supportActionBar!!.setDisplayShowCustomEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        if (enableBack) {
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setDisplayUseLogoEnabled(false)
        supportActionBar!!.setTitle(text)
    }

    fun setToolbar(view: View, params: ActionBar.LayoutParams?, enableBack: Boolean) {
        if (supportActionBar == null) {
            throw RuntimeException("Action bar is not set!")
        }
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowCustomEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(false)
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayUseLogoEnabled(false)
        actionBar.setDisplayShowHomeEnabled(false)
        if (enableBack) {
            // Loading Toolbar header views
            actionBar.setCustomView(
                view,
                ActionBar.LayoutParams(
                    ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.MATCH_PARENT
                )
            )
            val parent = view.parent as Toolbar
            parent.setContentInsetsAbsolute(0, 0)
            val back: AppCompatImageView = view.findViewById(R.id.backBtn)
            val backContainer = view.findViewById<View>(R.id.backBtnContainer)
            back.setImageResource(R.drawable.ic_back)
            backContainer.setOnClickListener { onBackPressed() }
        } else {
            val parent = view.parent as Toolbar
            parent.setContentInsetsAbsolute(0, 0)
            if (params != null) {
                actionBar.setCustomView(view, params)
            } else {
                actionBar.setCustomView(
                    view,
                    ActionBar.LayoutParams(
                        ActionBar.LayoutParams.MATCH_PARENT,
                        ActionBar.LayoutParams.MATCH_PARENT
                    )
                )
            }
        }
    }

    fun setTheme() {
        if (supportActionBar != null) {
            setTheme(R.style.BaseActivityTheme)
        } else {
            setTheme(R.style.BaseActivityTheme_NoActionBar)
        }
    }
}