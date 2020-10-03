package com.moonlightbutterfly.bookid.utils

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.view.View
import com.moonlightbutterfly.bookid.MainActivity
import com.moonlightbutterfly.bookid.repository.database.entities.Book

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

fun List<Book>.removeDisplayedBookFromList(book: Book): List<Book>? = this
    .toMutableList()
    .filter { it.id != book.id }

fun View.provideLogoId(logo: Logos): Int = (this.context as MainActivity).logoProvider.provideLogo(logo)
fun View.provideLogoId(logo: Int): Logos = (this.context as MainActivity).logoProvider.provideLogo(logo)
