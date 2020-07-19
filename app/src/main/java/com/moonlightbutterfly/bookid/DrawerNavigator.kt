package com.moonlightbutterfly.bookid

import androidx.navigation.NavController

fun NavController.isDestinationHere(actionId: Int): Boolean {
    val actionDestination = graph.getAction(actionId)?.destinationId
    val currentDestination = currentDestination?.id
    return actionDestination == currentDestination
}

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