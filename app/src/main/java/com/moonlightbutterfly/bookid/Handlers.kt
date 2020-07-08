package com.moonlightbutterfly.bookid

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.moonlightbutterfly.bookid.dialogs.AddShelfDialog

fun onClickListener(view: View) = (view.context as DrawerManager).openDrawer()

fun openAddShelfDialog(view: View) = AddShelfDialog
    .newInstance()
    .show((view.context as AppCompatActivity).supportFragmentManager, "AddShelfDialog")

fun signOutClick(view: View, userManager: UserManager) {
    userManager.run {
        deleteUserFromDatabase(loggedUser.value!!)
        saveUser(null)
    }
    view.findNavController().navigate(R.id.action_settingsFragment_to_nav_graph)
}

fun addShelfClick(view: View) {
    AddShelfDialog.newInstance().show(
        (view.context as AppCompatActivity).supportFragmentManager,
        "AddShelfDialog")
}