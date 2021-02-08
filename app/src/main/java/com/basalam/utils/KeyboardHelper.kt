package com.basalam.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager

class KeyboardHelper {

    companion object {
        fun setImeVisibility(view: View?, visible: Boolean) {
            try {
                val imm = view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                        ?: return
                if (visible) {
                    val tries = intArrayOf(0)
                    val isShown = booleanArrayOf(false)
                    view.requestFocus()
                    val runnable = object : Runnable {
                        override fun run() {
                            isShown[0] = imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
                            if (!isShown[0] && tries[0] < 10) {
                                view.postDelayed(this, tries[0] * 100.toLong())
                                tries[0]++
                            }
                        }
                    }
                    view.post(runnable)
                } else {
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
            } catch (ignore: Exception) {
                ignore.printStackTrace()
            }

        }

        fun hideKeyboard(activity: Activity?): Boolean {
            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
            val inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    ?: return false
            val focused = activity.currentFocus
            return focused != null && inputManager.hideSoftInputFromWindow(focused.windowToken, 0)
        }
    }
}