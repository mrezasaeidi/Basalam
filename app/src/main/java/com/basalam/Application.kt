package com.basalam

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.basalam.ui.activity.RootActivity
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
        const val CONFIG_PREF_NAME = "config"

        var currentFontScaleStyle = R.style.FontStyle_Normal

        fun setLocale(context: Context) {
            val pref = context.getSharedPreferences(CONFIG_PREF_NAME, MODE_PRIVATE)
            val lang = pref.getString("lang", "fa")!!
            val country = pref.getString("country", "IR")!!
            setLocale(context, lang, country, false)
        }

        private fun setLocale(context: Context, lang: String, country: String, reload: Boolean) {
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
            if (reload) {
                restart(context)
            }
        }

        fun restart(context: Context) {
            val intentToBeNewRoot = Intent(context, RootActivity::class.java)
            val cn = intentToBeNewRoot.component
            val mainIntent = Intent.makeRestartActivityTask(cn)
            context.startActivity(mainIntent)
            exitProcess(0)
        }
    }
}