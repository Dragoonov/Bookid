package com.moonlightbutterfly.bookid.utils

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.view.View

fun View.animatePulse() {
    val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.5f)
    val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.5f)
    ObjectAnimator.ofPropertyValuesHolder(this, scaleX, scaleY).apply {
        repeatCount = 1
        repeatMode = ValueAnimator.REVERSE
        duration = 200
        start()
    }
}