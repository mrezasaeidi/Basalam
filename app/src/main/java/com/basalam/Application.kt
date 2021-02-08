package com.basalam

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatDelegate
import com.basalam.ui.activity.RootActivity
import com.basalam.ui.utils.Constants.CONFIG_PREF_NAME
import com.basalam.ui.utils.LayoutUtil
import java.util.*
import kotlin.system.exitProcess

class Application : Application(), ActivityLifecycleCallbacks {

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        overrideFont()
        setLocale(this)
        checkNightMode()
        initFontSizeDefault()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setLocale(this)
    }


    private fun overrideFont() {

    }

    private fun checkNightMode() {
        val preferences = getSharedPreferences(CONFIG_PREF_NAME, MODE_PRIVATE)
        val night = preferences.getBoolean("night_mode", false)
        AppCompatDelegate.setDefaultNightMode(if (night) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun initFontSizeDefault() {
        val preferences = getSharedPreferences(CONFIG_PREF_NAME, MODE_PRIVATE)
        currentFontScaleStyle = preferences.getInt("font_size", Style.FontStyle.Normal.resId)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activity.theme.applyStyle(currentFontScaleStyle, true)
    }

    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}

    companion object {

        var currentFontScaleStyle = R.style.FontStyle_Normal

        fun setLocale(context: Context) {
            val pref = context.getSharedPreferences(CONFIG_PREF_NAME, MODE_PRIVATE)
            val lang = pref.getString("lang", "fa")!!
            val country = pref.getString("country", "IR")!!
            setLocale(context, lang, country, false)
        }

        fun setLocale(context: Context, lang: String, country: String, reload: Boolean) {
            if (reload) {
                context.getSharedPreferences(CONFIG_PREF_NAME, MODE_PRIVATE)
                    .edit()
                    .putString("lang", lang)
                    .putString("country", country)
                    .apply()
            }
            val locale = Locale(lang, country)
            Locale.setDefault(locale)
            val res = context.resources
            val resApp = context.applicationContext.resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.setLocale(locale)
            conf.setLayoutDirection(locale)
            res.updateConfiguration(conf, dm)
            resApp.updateConfiguration(conf, dm)
            context.applicationContext.createConfigurationContext(conf)
            context.createConfigurationContext(conf)
            LayoutUtil.init(context)
            if (reload) {
                restart(context, 500)
            }
        }

        fun setFontSize(context: Context, style: Style.FontStyle) {
            currentFontScaleStyle = style.resId
            context.getSharedPreferences(CONFIG_PREF_NAME, MODE_PRIVATE).edit().putInt(
                "font_size",
                currentFontScaleStyle
            ).apply()
            restart(context, 500)
        }

        private fun restart(context: Context, delay: Long) {
            val intentToBeNewRoot = Intent(context, RootActivity::class.java)
            val cn = intentToBeNewRoot.component
            val mainIntent = Intent.makeRestartActivityTask(cn)
            if (delay == 0L) {
                context.startActivity(mainIntent)
                exitProcess(0)
            } else {
                Handler().postDelayed({
                    context.startActivity(mainIntent)
                    exitProcess(0)
                }, delay)
            }
        }

        fun toggleNightMode(context: Context, night: Boolean) {
            AppCompatDelegate.setDefaultNightMode(if (night) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
            context.getSharedPreferences(CONFIG_PREF_NAME, MODE_PRIVATE).edit()
                .putBoolean("night_mode", night).apply()
        }

        fun getAppVersion(context: Context): String {
            try {
                val pm = context.packageManager
                val info = pm.getPackageInfo(context.packageName, 0)
                return info.versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return ""
        }
    }
}