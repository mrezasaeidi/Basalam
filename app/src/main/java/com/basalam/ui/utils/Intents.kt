package com.basalam.ui.utils

import android.app.Activity
import android.content.Intent
import com.basalam.ui.activity.RouterActivity

object Intents {

    // Routing Params
    const val FRAGMENT_ID = "FRAGMENT_ID"
    const val PARAM_1 = "PARAM_1"
    const val PARAM_2 = "PARAM_2"
    const val PARAM_3 = "PARAM_3"
    const val PARAM_4 = "PARAM_4"
    const val PARAM_5 = "PARAM_5"

    // Fragments
    const val SHOPPING_BAG = 1

    private fun createIntent(activity: Activity, cls: Class<*>?): Intent {
        val intent = Intent(activity, cls)
        if (activity.intent.extras != null) {
            intent.putExtras(activity.intent.extras!!)
        }
        return intent
    }

    private fun route(activity: Activity, fragmentId: Int): Intent {
        val intent = createIntent(activity, RouterActivity::class.java)
        intent.putExtra(FRAGMENT_ID, fragmentId)
        return intent
    }

    fun openShoppingBag(activity: Activity): Intent {
        return route(activity, SHOPPING_BAG)
    }
}