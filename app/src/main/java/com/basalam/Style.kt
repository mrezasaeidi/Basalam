package com.basalam

import android.content.Context
import android.content.res.Configuration
import android.util.TypedValue

class Style {

    companion object {
        fun getBackgroundColor(context: Context): Int {
            val typedValue = TypedValue()
            context.theme.resolveAttribute(android.R.attr.colorBackground, typedValue, true)
            return typedValue.data
        }

        fun inNightMode(context: Context): Boolean {
            val currentNightMode =
                context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            return currentNightMode == Configuration.UI_MODE_NIGHT_YES
        }
    }

    enum class FontStyle(val resId: Int) {
        Small(R.style.FontStyle_Small), Normal(R.style.FontStyle_Normal), Large(R.style.FontStyle_Large), Xlarge(
            R.style.FontStyle_XLarge
        );

    }
}