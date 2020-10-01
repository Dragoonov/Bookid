package com.moonlightbutterfly.bookid.utils

enum class DefaultShelf(val id: Int) {
    FAVORITES(1000), SAVED(1001), RECENTLY_VIEWED(1002);

    companion object {
        fun matches(id: Int): Boolean =
            id == FAVORITES.id || id == SAVED.id || id == RECENTLY_VIEWED.id
    }
}