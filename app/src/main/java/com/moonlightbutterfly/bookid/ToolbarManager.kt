package com.moonlightbutterfly.bookid

import androidx.appcompat.widget.Toolbar

interface ToolbarManager {
    fun showDefaultToolbar()
    fun showCustomToolbar(toolbar: Toolbar)
    fun hideToolbar()
}