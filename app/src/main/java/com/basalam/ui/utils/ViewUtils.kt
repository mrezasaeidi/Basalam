package com.basalam.ui.utils

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation

object ViewUtils {
    fun goneView(view: View?) {
        goneView(view, true)
    }

    fun goneView(view: View?, isAnimated: Boolean) {
        goneView(view, isAnimated, true)
    }

    fun goneView(view: View?, isAnimated: Boolean, isSlow: Boolean) {
        if (view == null) {
            return
        }
        if (isAnimated) {
            if (view.visibility != View.GONE) {
                val alphaAnimation = AlphaAnimation(1f, 0f)
                alphaAnimation.duration = if (isSlow) 300 else 150.toLong()
                alphaAnimation.interpolator = MaterialInterpolator.instance
                view.clearAnimation()
                view.startAnimation(alphaAnimation)
                view.gone()
            }
        } else {
            view.gone()
        }
    }

    fun goneViews(vararg views: View?) {
        goneViews(true, *views)
    }

    fun goneViews(isAnimated: Boolean, vararg views: View?) {
        goneViews(isAnimated, true, *views)
    }

    fun goneViews(isAnimated: Boolean, isSlow: Boolean, vararg views: View?) {
        for (view in views) {
            goneView(view, isAnimated, isSlow)
        }
    }

    fun hideView(view: View?) {
        hideView(view, true)
    }

    fun hideView(view: View?, isAnimated: Boolean) {
        hideView(view, isAnimated, true)
    }

    fun hideView(view: View?, isAnimated: Boolean, isSlow: Boolean) {
        if (view == null) {
            return
        }
        if (isAnimated) {
            if (view.visibility != View.INVISIBLE) {
                val alphaAnimation = AlphaAnimation(1f, 0f)
                alphaAnimation.duration = if (isSlow) 300 else 150.toLong()
                alphaAnimation.interpolator = MaterialInterpolator.instance
                view.clearAnimation()
                view.startAnimation(alphaAnimation)
                view.invisible()
            }
        } else {
            view.invisible()
        }
    }

    fun hideViews(vararg views: View?) {
        hideViews(true, *views)
    }

    fun hideViews(isAnimated: Boolean, vararg views: View?) {
        hideViews(isAnimated, true, *views)
    }

    fun hideViews(isAnimated: Boolean, isSlow: Boolean, vararg views: View?) {
        for (view in views) {
            hideView(view, isAnimated, isSlow)
        }
    }

    fun zoomOutView(view: View?) {
        zoomOutView(view, false)
    }

    fun zoomOutView(view: View?, isSlow: Boolean) {
        if (view == null) {
            return
        }
        if (view.visibility != View.INVISIBLE) {
            val scaleAnimation = ScaleAnimation(
                1f,
                0f,
                1f,
                0f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
            scaleAnimation.duration = if (isSlow) 300 else 150.toLong()
            scaleAnimation.interpolator = MaterialInterpolator.instance
            view.clearAnimation()
            view.startAnimation(scaleAnimation)
            view.invisible()
        }
    }

    fun zoomInView(view: View?) {
        zoomInView(view, false)
    }

    fun zoomInView(view: View?, isSlow: Boolean) {
        if (view == null) {
            return
        }
        if (view.visibility != View.VISIBLE) {
            val scaleAnimation = ScaleAnimation(
                0f,
                1f,
                0f,
                1f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
            scaleAnimation.duration = if (isSlow) 300 else 150.toLong()
            scaleAnimation.interpolator = MaterialInterpolator.instance
            view.clearAnimation()
            view.startAnimation(scaleAnimation)
            view.visible()
        }
    }

    fun showView(view: View?) {
        showView(view, true)
    }

    fun showView(view: View?, isAnimated: Boolean) {
        showView(view, isAnimated, true)
    }

    fun showView(view: View?, isAnimated: Boolean, isSlow: Boolean) {
        if (view == null) {
            return
        }
        if (isAnimated) {
            if (view.visibility != View.VISIBLE) {
                val alphaAnimation = AlphaAnimation(0f, 1f)
                alphaAnimation.duration = if (isSlow) 300 else 150.toLong()
                alphaAnimation.interpolator = MaterialInterpolator.instance
                view.clearAnimation()
                view.startAnimation(alphaAnimation)
                view.visible()
            }
        } else {
            view.visible()
        }
    }

    fun showViews(vararg views: View?) {
        showViews(true, *views)
    }

    fun showViews(isAnimated: Boolean, vararg views: View?) {
        showViews(isAnimated, true, *views)
    }

    fun showViews(isAnimated: Boolean, isSlow: Boolean, vararg views: View?) {
        for (view in views) {
            showView(view, isAnimated, isSlow)
        }
    }
}