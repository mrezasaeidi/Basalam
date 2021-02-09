package com.basalam.ui.utils

import android.content.Context
import android.graphics.Typeface
import java.util.*

object Fonts {
    private val typefaces = HashMap<String, Typeface>()
    private fun load(context: Context, name: String): Typeface {
        if (typefaces.containsKey(name)) {
            return typefaces[name]!!
        }
        val typeface = Typeface.createFromAsset(
            context.assets,
            "vazir$name.ttf"
        )
        if (typeface != null) {
            typefaces[name] = typeface
        }
        return typeface
    }

    fun bold(context: Context): Typeface {
        return load(context, "Bold")
    }

    fun black(context: Context): Typeface {
        return load(context, "Black")
    }

    fun medium(context: Context): Typeface {
        return load(context, "Medium")
    }

    fun regular(context: Context): Typeface {
        return load(context, "")
    }

    fun light(context: Context): Typeface {
        return load(context, "Light")
    }

    fun thin(context: Context): Typeface {
        return load(context, "Thin")
    }
}