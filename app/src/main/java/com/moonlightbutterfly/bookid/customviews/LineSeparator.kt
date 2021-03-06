package com.moonlightbutterfly.bookid.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.View
import com.moonlightbutterfly.bookid.R

class LineSeparator(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint(ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
    }

    private var orientation: Int = 0

    init {
        context.obtainStyledAttributes(
            attrs, R.styleable.LineSeparator, 0, 0
        ).apply {
            try {
                orientation = getInt(R.styleable.LineSeparator_orientation, 0)
            } finally {
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = if (orientation == Orientation.VERTICAL.ordinal) {
            convertDpsToPixels(LINE_SIZE + MARGINS)
        } else {
            measuredWidth
        }
        var height = if (orientation == Orientation.VERTICAL.ordinal) {
            measuredHeight
        } else {
            convertDpsToPixels(LINE_SIZE + MARGINS)
        }
        width = resolveSize(width, widthMeasureSpec)
        height = resolveSize(height, heightMeasureSpec)

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            if (orientation == Orientation.HORIZONTAL.ordinal) {
                drawLine(
                    0f + convertDpsToPixels(16),
                    measuredHeight / 2f,
                    measuredWidth.toFloat() - convertDpsToPixels(16),
                    measuredHeight / 2f, paint
                )
            } else {
                drawLine(
                    measuredWidth / 2f,
                    measuredHeight.toFloat() - convertDpsToPixels(16),
                    measuredWidth / 2f,
                    0f + convertDpsToPixels(16), paint
                )
            }
        }
    }

    private enum class Orientation {
        VERTICAL, HORIZONTAL
    }

    companion object {
        private const val LINE_SIZE: Int = 2
        private const val MARGINS = 8
    }

    private fun View.convertDpsToPixels(dps: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dps * scale + 0.5f).toInt()
    }
}