package com.moonlightbutterfly.bookid

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.moonlightbutterfly.bookid.dialogs.AddShelfDialog


fun openLink(view: View, url: String?) {
    val webpage: Uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, webpage)
    if (intent.resolveActivity(view.context.packageManager) != null) {
        view.context.startActivity(intent)
    }
}

fun onClickListener(view: View) = (view.context as DrawerManager).openDrawer()

fun openAddShelfDialog(view: View) = AddShelfDialog
    .newInstance()
    .show((view.context as AppCompatActivity).supportFragmentManager, AddShelfDialog.NAME)

fun signOutClick(view: View, userManager: Manager) {
    userManager.singOutUser(view.context)
    view.findNavController().navigate(R.id.action_settingsFragment_to_nav_graph)
}

fun addShelfClick(view: View) {
    AddShelfDialog.newInstance().show(
        (view.context as AppCompatActivity).supportFragmentManager,
        AddShelfDialog.NAME
    )
}