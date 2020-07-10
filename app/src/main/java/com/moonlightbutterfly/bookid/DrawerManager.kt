package com.moonlightbutterfly.bookid

import android.graphics.Bitmap

interface DrawerManager {
    fun lockDrawer()
    fun unlockDrawer()
    fun openDrawer()
    fun closeDrawer()
    fun paintDrawer(bitmap: Bitmap?)
}