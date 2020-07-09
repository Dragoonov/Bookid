package com.moonlightbutterfly.bookid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.moonlightbutterfly.bookid.databinding.ActivityMainBinding
import com.moonlightbutterfly.bookid.dialogs.QuitAppDialog
import com.moonlightbutterfly.bookid.fragments.LoginFragmentDirections
import com.moonlightbutterfly.bookid.repository.database.entities.User
import javax.inject.Inject


class MainActivity : AppCompatActivity(), DrawerManager {

    @Inject
    lateinit var userManager: UserManager

    lateinit var navController: NavController

    private lateinit var binding: ActivityMainBinding

    companion object {
        const val SIGN_IN_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as BookidApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.let {
            it.lifecycleOwner = this
            it.userManager = userManager
            it.drawerNavigator = DrawerNavigator(this, navController)
            it.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
                override fun onDrawerStateChanged(newState: Int) {}
                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
                override fun onDrawerOpened(drawerView: View) {}
                override fun onDrawerClosed(drawerView: View) = (it.drawerNavigator as DrawerNavigator).navigate()
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGN_IN_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                val loggedUser = User(
                    account.id!!,
                    account.displayName,
                    account.email,
                    account.photoUrl.toString())
                userManager.saveUser(loggedUser)
                userManager.saveUserToDatabase(loggedUser)
                navController.navigate(LoginFragmentDirections.actionGlobalAppGraph())
                Log.v("MainActivity", "Zalogowano jako $loggedUser")
            } else {
                Toast.makeText(this, R.string.login_fail, Toast.LENGTH_LONG).show()
            }

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("MainActivity", "signInResult:failed code=" + e.statusCode)
            Toast.makeText(this, R.string.login_fail, Toast.LENGTH_LONG).show()
        }
    }

    override fun lockDrawer() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    override fun unlockDrawer() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    override fun openDrawer() {
        binding.drawerLayout.open()
    }

    override fun closeDrawer() {
        binding.drawerLayout.close()
    }

    override fun onBackPressed() {
        if (navController.currentDestination?.id != R.id.loginFragment
            && navController.currentDestination?.id != R.id.splashFragment
            && navController.currentDestination?.id != R.id.searchFragment) {
            super.onBackPressed()
        } else {
            QuitAppDialog.newInstance().show(supportFragmentManager,"QuitAppDialog")
        }
    }
}
