package com.moonlightbutterfly.bookid

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.palette.graphics.Palette
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.moonlightbutterfly.bookid.databinding.ActivityMainBinding
import com.moonlightbutterfly.bookid.dialogs.QuitAppDialog
import com.moonlightbutterfly.bookid.fragments.LoginFragmentDirections
import com.moonlightbutterfly.bookid.repository.database.entities.User
import javax.inject.Inject

fun NavController.canGoBack(): Boolean =
    currentDestination?.id != R.id.loginFragment
            && currentDestination?.id != R.id.splashFragment
            && currentDestination?.id != R.id.searchFragment

class MainActivity : AppCompatActivity(), DrawerManager {

    @Inject
    lateinit var communicator: Communicator

    @Inject
    lateinit var userManager: UserManager

    private lateinit var navController: NavController

    private lateinit var binding: ActivityMainBinding

    private val drawerListener = object : DrawerLayout.DrawerListener {
        override fun onDrawerStateChanged(newState: Int) {}
        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
        override fun onDrawerOpened(drawerView: View) {}
        override fun onDrawerClosed(drawerView: View) =
            (binding.drawerNavigator as DrawerNavigator).navigate()
    }

    companion object {
        const val SIGN_IN_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as BookidApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.let {
            it.lifecycleOwner = this
            it.userManager = userManager
            it.drawerNavigator = DrawerNavigator(this, navController)
            it.drawerManager = this
            it.drawerLayout.addDrawerListener(drawerListener)
        }
        communicator.message.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                communicator.clearMessage()
            }
        })
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
                    account.photoUrl.toString()
                )
                userManager.signInUser(loggedUser)
                navController.navigate(LoginFragmentDirections.actionGlobalAppGraph())
            }

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            communicator.postMessage(getString(R.string.login_fail))
            val loggedUser = User(
                "116644458345052983935",
                "Jakub Lipowski",
                "jakub.lipowski01@gmail.com",
                "https://lh3.googleusercontent.com/a-/AOh14GiPou93h951L-XfDmexoG3YKIFM1e7zsNzl5a4B"
            )
            userManager.signInUser(loggedUser)
            navController.navigate(LoginFragmentDirections.actionGlobalAppGraph())
        }
    }

    override fun lockDrawer() =
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

    override fun unlockDrawer() =
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

    override fun openDrawer() = binding.drawerLayout.open()

    override fun closeDrawer() = binding.drawerLayout.close()

    override fun onBackPressed() {
        if (navController.canGoBack()) {
            super.onBackPressed()
        } else {
            QuitAppDialog.newInstance().show(supportFragmentManager, QuitAppDialog.NAME)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun paintDrawer(bitmap: Bitmap?) {
        if (bitmap != null) {
            val paletteSwatch = Palette.from(bitmap).generate()
            binding.avatarContainer.setBackgroundColor(paletteSwatch.getLightMutedColor(getColor(R.color.primaryColor)))
            binding.avatarName.setTextColor(paletteSwatch.getDarkMutedColor(getColor(R.color.primaryTextColor)))
        }
    }
}
