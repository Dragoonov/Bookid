package com.moonlightbutterfly.bookid

import android.graphics.Canvas
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.moonlightbutterfly.bookid.adapters.LAYOUT

fun View.convertDpsToPixels(dps: Int): Int {
    val scale = context.resources.displayMetrics.density
    return (dps * scale + 0.5f).toInt()
}

class CustomItemDecoration(private val layoutType: LAYOUT) : RecyclerView.ItemDecoration() {


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        val drawable = parent.context.getDrawable(R.drawable.divider)
        var left: Int
        var right: Int
        var top: Int
        var bottom: Int
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            if (layoutType == LAYOUT.VERTICAL) {
                left = parent.convertDpsToPixels(16)
                right = parent.width - parent.convertDpsToPixels(16)
                top = child.bottom
                bottom = top + drawable?.intrinsicHeight!!
            } else {
                left = child.right
                right = left + drawable?.intrinsicWidth!!
                top = child.top + child.convertDpsToPixels(16)
                bottom = parent.bottom - parent.top - child.convertDpsToPixels(16)
            }
            drawable.setBounds(left, top, right, bottom)
            drawable.draw(c)
        }
    }
}