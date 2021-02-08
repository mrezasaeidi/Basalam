package com.basalam.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.RadioGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.basalam.Application
import com.basalam.R
import com.basalam.Style
import com.basalam.ui.fragment.RootFragment
import com.basalam.ui.utils.Intents
import com.basalam.ui.utils.LayoutUtil
import com.google.android.material.navigation.NavigationView

class RootActivity : BaseFragmentActivity(), NavigationView.OnNavigationItemSelectedListener {

    init {
        canSwipe = false
    }

    private var navMenu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.root_activity)

        // Configure Toolbar
        val toolbar: Toolbar? = findViewById(R.id.toolbar)
        if (toolbar != null) {
            setSupportActionBar(toolbar)
        }
        supportActionBar?.elevation = 16f
        supportFragmentManager.beginTransaction()
            .replace(R.id.root, RootFragment())
            .commitNow()

        val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
        ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.nav_drawer_open,
            R.string.nav_drawer_close
        ).apply {
            isDrawerIndicatorEnabled = false
            setHomeAsUpIndicator(R.drawable.ic_drawer)
            drawerArrowDrawable = DrawerArrowDrawable(this@RootActivity)
            toolbarNavigationClickListener = View.OnClickListener {
                drawer.openDrawer(
                    GravityCompat.START
                )
            }
            drawer.addDrawerListener(this)
            syncState()
        }
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navMenu = navigationView.menu
        navigationView.getChildAt(0).isVerticalScrollBarEnabled = false
        (navMenu!!.findItem(R.id.nav_night).actionView as CompoundButton).isChecked =
            Style.inNightMode(this)

        navigationView.getHeaderView(0).apply {
            findViewById<AppCompatImageView>(R.id.navHeaderAvatar)
                .setImageDrawable(
                    AppCompatResources.getDrawable(
                        this@RootActivity,
                        R.drawable.ic_person_grey
                    )
                )
            findViewById<AppCompatTextView>(R.id.navHeaderName).setText(R.string.user_name)
        }

        navMenu!!.findItem(R.id.nav_version).title =
            "${getString(R.string.app_name)} ${getString(R.string.version)} ${
                LayoutUtil.formatNumber(
                    Application.getAppVersion(this)
                )
            }"

        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onResume() {
        super.onResume()
        val nightMode = (navMenu!!.findItem(R.id.nav_night).actionView) as CompoundButton
        nightMode.setOnCheckedChangeListener { _, isChecked ->
            toggleNightMode(isChecked)
        }
    }

    override fun onBackPressed() {
        val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
        val rootFragment = supportFragmentManager.findFragmentById(R.id.root) as RootFragment?
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else if (rootFragment != null) {
            if (rootFragment.canGoBack()) {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var close = false
        when (item.itemId) {
            R.id.nav_shopping_bag -> {
                close = true
                startActivity(Intents.openShoppingBag(this))
            }

            R.id.nav_font_size -> {
                changeFontSize()
            }

            R.id.nav_language -> {
                changeLanguage()
            }

            R.id.nav_night -> {
                val nightMode = (navMenu!!.findItem(R.id.nav_night).actionView) as CompoundButton
                nightMode.isChecked = !nightMode.isChecked
            }
        }
        if (close) {
            findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
        }
        return true
    }

    private fun toggleNightMode(night: Boolean) {
        Application.toggleNightMode(applicationContext, night)
        LayoutUtil.recreate(this)
    }

    private fun changeFontSize() {
        val dialogView: View =
            LayoutInflater.from(this).inflate(R.layout.dialog_select_font_size, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        builder.setTitle(R.string.dialog_select_font_size_title)
        val fontSize = dialogView.findViewById<RadioGroup>(R.id.fontSize)
        when (Application.currentFontScaleStyle) {
            R.style.FontStyle_Small -> fontSize.check(R.id.small)
            R.style.FontStyle_Normal -> fontSize.check(R.id.normal)
            R.style.FontStyle_Large -> fontSize.check(R.id.large)
            R.style.FontStyle_XLarge -> fontSize.check(R.id.xlarge)
        }
        val dialog = builder.create()
        fontSize.setOnCheckedChangeListener { _, id: Int ->
            when (id) {
                R.id.small -> {
                    Application.setFontSize(
                        applicationContext,
                        Style.FontStyle.Small
                    )
                }
                R.id.normal -> {
                    Application.setFontSize(
                        applicationContext,
                        Style.FontStyle.Normal
                    )
                }
                R.id.large -> {
                    Application.setFontSize(
                        applicationContext,
                        Style.FontStyle.Large
                    )
                }
                R.id.xlarge -> {
                    Application.setFontSize(
                        applicationContext,
                        Style.FontStyle.Xlarge
                    )
                }
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun changeLanguage() {
        val dialogView: View = LayoutInflater.from(this).inflate(R.layout.dialog_select_lang, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        builder.setTitle(R.string.dialog_select_lang_title)
        val lang = dialogView.findViewById<RadioGroup>(R.id.language)
        when (LayoutUtil.getLanguage()) {
            LayoutUtil.Lang.FA -> lang.check(R.id.fa)
            LayoutUtil.Lang.EN -> lang.check(R.id.en)
        }
        val dialog = builder.create()
        lang.setOnCheckedChangeListener { _, id: Int ->
            if (id == R.id.en) {
                Application.setLocale(applicationContext, "en", "US", true)
            } else if (id == R.id.fa) {
                Application.setLocale(applicationContext, "fa", "IR", true)
            }
            dialog.dismiss()
        }
        dialog.show()
    }
}