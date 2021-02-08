package com.basalam

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.basalam.ui.activity.RootActivity
import java.util.*
import kotlin.system.exitProcess

class Application : Application() {

    companion object {
        const val CONFIG_PREF_NAME = "config.locale"

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


    override fun onCreate() {
        super.onCreate()
        overrideFont()
        setLocale(this)
        checkNightMode()
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
}