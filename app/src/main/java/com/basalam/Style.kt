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
}