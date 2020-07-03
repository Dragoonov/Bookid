package com.moonlightbutterfly.bookid.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.moonlightbutterfly.bookid.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [SplashFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SplashFragment : Fragment() {

    @Inject
    lateinit var userManager: UserManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity?.application as BookidApplication).appComponent.inject(this)
        authenticateUser()
        (activity as ToolbarManager).hideToolbar()
        (activity as DrawerLocker).lockDrawer()
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    private fun authenticateUser() {
        val navController = findNavController()
        if (userManager.loggedUser.value == null) {
            userManager.getUserFromDatabase().observe(viewLifecycleOwner, Observer {
                if (it == null) {
                    navController.navigate(SplashFragmentDirections.actionSplashFragmentToLoginGraph())
                } else {
                    userManager.saveUser(it)
                    navController.navigate(SplashFragmentDirections.actionSplashFragmentToAppGraph())
                }
            })
        } else {
            navController.navigate(SplashFragmentDirections.actionSplashFragmentToAppGraph())
        }
    }
}