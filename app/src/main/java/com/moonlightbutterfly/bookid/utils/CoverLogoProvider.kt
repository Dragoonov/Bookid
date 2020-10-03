package com.moonlightbutterfly.bookid.utils

import com.moonlightbutterfly.bookid.R

class CoverLogoProvider {
    fun provideLogo(logo: Logos): Int =
        when (logo) {
            Logos.FISH -> R.drawable.ic_fish
            Logos.INTERNET -> R.drawable.ic_internet
            Logos.BALL -> R.drawable.ic_soccer
            Logos.SCIENCE -> R.drawable.ic_science
            Logos.RUBICS -> R.drawable.ic_rubiks_cube
            Logos.FAVOURITE -> R.drawable.ic_favorite_24px
            Logos.SAVED -> R.drawable.ic_bookmark_24px
            Logos.RECENT -> R.drawable.ic_back
        }

    fun provideLogo(logo: Int): Logos =
        when (logo) {
            R.drawable.ic_fish -> Logos.FISH
            R.drawable.ic_internet -> Logos.INTERNET
            R.drawable.ic_soccer -> Logos.BALL
            R.drawable.ic_science -> Logos.SCIENCE
            R.drawable.ic_rubiks_cube -> Logos.RUBICS
            else -> Logos.FAVOURITE
        }
}

enum class Logos {
    FISH, INTERNET, BALL, SCIENCE, RUBICS, FAVOURITE, SAVED, RECENT
}