package com.basalam

import android.content.Context
import android.util.TypedValue

class Style {

    companion object{
        fun getBackgroundColor(context: Context): Int {
            val typedValue = TypedValue()
            context.theme.resolveAttribute(android.R.attr.colorBackground, typedValue, true)
            return typedValue.data
        }
    }

    enum class FontStyle(val resId: Int) {
        Small(R.style.FontStyle_Small), Normal(R.style.FontStyle_Normal), Large(R.style.FontStyle_Large), Xlarge(
            R.style.FontStyle_XLarge
        );

    }
}