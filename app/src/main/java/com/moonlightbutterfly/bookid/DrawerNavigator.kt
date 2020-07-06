package com.moonlightbutterfly.bookid

import androidx.navigation.NavController

class DrawerNavigator(private val drawerManager: DrawerManager, private val navController: NavController) {

    var id: Int = 0

    fun navigate() {
        if (id != 0) {
            navController.navigate(id)
        }
        id = 0
    }

    fun goToSearchBooks() {
        drawerManager.closeDrawer()
        id = R.id.action_global_searchFragment
    }

    fun goToShelfs() {
        drawerManager.closeDrawer()
        id = R.id.action_global_shelfFragment
    }

    fun goToEditShelfs() {
        drawerManager.closeDrawer()
        id = R.id.action_global_editShelfsFragment
    }

    fun goToProfile() {
        drawerManager.closeDrawer()
        id = R.id.action_global_profileFragment
    }

    fun goToSettings() {
        drawerManager.closeDrawer()
        id = R.id.action_global_settingsFragment
    }

}