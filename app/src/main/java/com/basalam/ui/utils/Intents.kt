package com.basalam.ui.utils

import android.app.Activity
import android.content.Intent
import com.basalam.ui.activity.RouterActivity
import com.basalam.ui.utils.Constants.FRAGMENT_ID
import com.basalam.ui.utils.Constants.SHOPPING_BAG

object Intents {

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