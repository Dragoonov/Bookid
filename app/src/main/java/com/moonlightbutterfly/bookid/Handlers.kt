package com.moonlightbutterfly.bookid

import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.moonlightbutterfly.bookid.fragments.ProfileFragmentDirections
import com.moonlightbutterfly.bookid.viewmodels.CreateEditShelfViewModel

fun saveIconToViewModel(view: ImageView, iconId: Int, viewModel: CreateEditShelfViewModel) {
    viewModel.iconId = iconId
    view.setImageDrawable(ContextCompat.getDrawable(view.context, iconId))
}

fun changeDescriptionExpansion(firstView: View, secondView: View, view: TextView) {
    val resultLines = if (view.maxLines == 3) view.lineCount else 3
    if (firstView.visibility == View.VISIBLE) {
        firstView.visibility = View.INVISIBLE
        secondView.visibility = View.VISIBLE
    } else {
        firstView.visibility = View.VISIBLE
        secondView.visibility = View.INVISIBLE
    }
    ObjectAnimator.ofInt(view, "maxLines", resultLines).apply {
        duration = 200
        start()
    }
}

fun openLink(view: View, url: String?) {
    url?.let {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(view.context.packageManager) != null) {
            view.context.startActivity(intent)
        }
    }
}

fun signOutClick(view: View, userManager: Manager) {
    userManager.singOutUser(view.context)
    view.findNavController().navigate(ProfileFragmentDirections.actionProfileToNavGraph())
}