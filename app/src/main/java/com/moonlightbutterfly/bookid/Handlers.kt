package com.moonlightbutterfly.bookid

import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.moonlightbutterfly.bookid.utils.provideLogoId
import com.moonlightbutterfly.bookid.viewmodels.CreateEditShelfViewModel

fun saveIconToViewModel(view: ImageView, iconId: Int, viewModel: CreateEditShelfViewModel) {
    viewModel.iconId = view.provideLogoId(iconId)
    view.setImageDrawable(ContextCompat.getDrawable(view.context, iconId))
}