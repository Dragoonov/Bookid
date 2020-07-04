package com.moonlightbutterfly.bookid

import androidx.navigation.NavController

class DrawerNavigator(private val drawerManager: DrawerManager, private val navController: NavController) {

    fun goToSearchBooks() {
        drawerManager.closeDrawer()
        navController.navigate(R.id.action_global_searchFragment)
    }

    fun goToShelfs() {
        drawerManager.closeDrawer()
        navController.navigate(R.id.action_global_shelfFragment)
    }

    fun goToEditShelfs() {
        drawerManager.closeDrawer()
        navController.navigate(R.id.action_global_editShelfsFragment)
    }

    fun goToProfile() {
        drawerManager.closeDrawer()
        navController.navigate(R.id.action_global_profileFragment)
    }

    fun goToSettings() {
        drawerManager.closeDrawer()
        navController.navigate(R.id.action_global_settingsFragment)
    }

}