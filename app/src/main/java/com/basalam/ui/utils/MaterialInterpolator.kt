package com.basalam.ui.utils

import android.view.animation.Interpolator
import kotlin.math.pow

class MaterialInterpolator : Interpolator {
    override fun getInterpolation(x: Float): Float {
        return (6 * x.toDouble().pow(2.0) - 8 * x.toDouble().pow(3.0) + 3 * x.toDouble().pow(4.0)).toFloat()
    }

    companion object {
        val instance = MaterialInterpolator()
    }
}