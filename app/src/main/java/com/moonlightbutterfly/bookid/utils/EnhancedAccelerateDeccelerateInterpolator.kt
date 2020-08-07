package com.moonlightbutterfly.bookid.utils

import android.view.animation.Interpolator
import kotlin.math.pow

class EnhancedAccelerateDecelerateInterpolator: Interpolator {

    override fun getInterpolation(input: Float): Float {
        var x = input * 2.0f
        if (input < 0.5f) return 0.5f * x.pow(5)
        x = (input - 0.5f) * 2 - 1
        return 0.5f * x.pow(5) + 1
    }

}