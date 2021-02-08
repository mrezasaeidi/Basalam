package com.basalam.ui.utils

import android.app.Activity
import android.content.Context
import com.basalam.R
import java.util.*

object LayoutUtil {

    private lateinit var locale: String
    private var rtl = false
    private var fa = false
    private var en = false

    fun init(context: Context) {
        rtl = context.resources.getBoolean(R.bool.rtl)
        locale = Locale.getDefault().language.toLowerCase(Locale.ROOT)
        fa = locale == "fa"
        en = locale == "en"
    }

    enum class Lang {
        FA, EN
    }

    fun getLanguage(): Lang {
        return when {
            fa -> Lang.FA
            en -> Lang.EN
            else -> Lang.FA
        }
    }

    fun formatNumber(value: String): String {
        return NumberFormatting.toLocale(value, locale)
    }

    fun recreate(activity: Activity) {
        activity.recreate()
    }

    object NumberFormatting {
        fun toLocale(text: String, localeName: String): String {
            return if (localeName == "fa") {
                toFa(text)
            } else {
                text
            }
        }

        fun toFa(oldText: String): String {
            var text = oldText
            val eNums = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
            val fNums = charArrayOf(
                '\u06f0',
                '\u06f1',
                '\u06f2',
                '\u06f3',
                '\u06f4',
                '\u06f5',
                '\u06f6',
                '\u06f7',
                '\u06f8',
                '\u06f9'
            )
            for (i in 0..9) {
                text = text.replace(eNums[i], fNums[i])
            }
            return text
        }

        fun toEn(oldText: String): String {
            var text = oldText
            val eNums = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
            val fNums = charArrayOf(
                '\u06f0',
                '\u06f1',
                '\u06f2',
                '\u06f3',
                '\u06f4',
                '\u06f5',
                '\u06f6',
                '\u06f7',
                '\u06f8',
                '\u06f9'
            )
            for (i in 0..9) {
                text = text.replace(fNums[i], eNums[i])
            }
            return text
        }
    }

}