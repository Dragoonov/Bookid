package com.moonlightbutterfly.bookid

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.moonlightbutterfly.bookid.databinding.ActivityMainBinding
import com.moonlightbutterfly.bookid.dialogs.QuitAppDialog
import com.moonlightbutterfly.bookid.fragments.LoginFragmentDirections
import com.moonlightbutterfly.bookid.fragments.SplashFragmentDirections
import com.moonlightbutterfly.bookid.repository.database.entities.User
import com.moonlightbutterfly.bookid.utils.Converters
import com.moonlightbutterfly.bookid.viewmodels.ShelfViewModel
import javax.inject.Inject


class MainActivity : AppCompatActivity(), NavigationController {

    @Inject
    lateinit var communicator: Communicator

    @Inject
    lateinit var userManager: Manager

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ShelfViewModel

    private lateinit var navController: NavController

    private lateinit var binding: ActivityMainBinding


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
            it.bottomNav.setupWithNavController(navController)
        }
        communicator.message.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                communicator.clearMessage()
            }
        })
        userManager.user.observe(this, Observer {
            if (it != null) {
                if (navController.currentDestination?.id == R.id.splashFragment) {
                    navController.navigate(SplashFragmentDirections.actionSplashFragmentToAppGraph())
                } else if (navController.currentDestination?.id == R.id.loginFragment) {
                    val covers = arrayOf(
                        Pair(
                            Color.parseColor("#686868"),
                            R.drawable.ic_favorite_24px
                        ),
                        Pair(
                            Color.parseColor("#686868"),
                            R.drawable.ic_bookmark_24px
                        ),Pair(
                            Color.parseColor("#686868"),
                            R.drawable.ic_back
                        )
                    )
                    viewModel.prepareBasicShelfs(resources.getStringArray(R.array.basic_shelfs), covers)
                    navController.navigate(LoginFragmentDirections.actionGlobalAppGraph())
                }
            }
        })
        viewModel = ViewModelProvider(this, viewModelFactory)[ShelfViewModel::class.java]
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
                userManager.signInUser(loggedUser, this)
            }

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            communicator.postMessage(getString(R.string.login_fail))
            //For now just sign in with default data
            val loggedUser = User(
                "116644458345052983935",
                "Jakub Lipowski",
                "jakub.lipowski01@gmail.com",
                "https://lh3.googleusercontent.com/a-/AOh14GiPou93h951L-XfDmexoG3YKIFM1e7zsNzl5a4B"
            )
            userManager.signInUser(loggedUser, this)
        }
    }

    override fun onBackPressed() {
        if (navController.canGoBack()) {
            super.onBackPressed()
        } else {
            QuitAppDialog.newInstance().show(supportFragmentManager, QuitAppDialog.NAME)
        }
    }

    override fun unlockBottomNav() = run { binding.bottomNav.visibility = View.VISIBLE }

    override fun lockBottomNav() = run { binding.bottomNav.visibility = View.GONE }

    private fun NavController.canGoBack(): Boolean = currentDestination?.id != R.id.loginFragment
                && currentDestination?.id != R.id.splashFragment
                && currentDestination?.id != R.id.search
                && currentDestination?.id != R.id.shelfs
                && currentDestination?.id != R.id.profile
}

fun Context.getNavController() = this as NavigationController
