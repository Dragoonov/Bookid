package com.moonlightbutterfly.bookid

import androidx.navigation.NavController

class DrawerNavigator(
    private val drawerManager: DrawerManager,
    private val navController: NavController
) {

    var id: Int = 0

    fun navigate() {
        if (!navController.isDestinationHere(id) && id != 0) {
            navController.navigate(id)
        }
        id = 0
    }

    fun setSearchBooksDestination() {
        drawerManager.closeDrawer()
        id = R.id.action_global_searchFragment
    }

    fun setShelfsDestination() {
        drawerManager.closeDrawer()
        id = R.id.action_global_shelfFragment
    }

    fun setEditShelfsDestination() {
        drawerManager.closeDrawer()
        id = R.id.action_global_editShelfsFragment
    }

    fun setProfileDestination() {
        drawerManager.closeDrawer()
        id = R.id.action_global_profileFragment
    }

    fun setSettingsDestination() {
        drawerManager.closeDrawer()
        id = R.id.action_global_settingsFragment
    }

    private fun NavController.isDestinationHere(actionId: Int): Boolean {
        val actionDestination = graph.getAction(actionId)?.destinationId
        val currentDestination = currentDestination?.id
        return actionDestination == currentDestination
    }

}