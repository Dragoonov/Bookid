package com.moonlightbutterfly.bookid

import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.moonlightbutterfly.bookid.dialogs.AddShelfDialog

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

fun openAddShelfDialog(view: View) = AddShelfDialog
    .newInstance()
    .show((view.context as AppCompatActivity).supportFragmentManager, AddShelfDialog.NAME)

fun signOutClick(view: View, userManager: Manager) {
    userManager.singOutUser(view.context)
    view.findNavController().navigate(R.id.action_global_app_graph)
}