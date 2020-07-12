package com.moonlightbutterfly.bookid.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.moonlightbutterfly.bookid.*
import com.moonlightbutterfly.bookid.databinding.FragmentSplashBinding
import javax.inject.Inject

class SplashFragment : BaseFragment<FragmentSplashBinding,ViewModel>() {

    @Inject
    lateinit var userManager: UserManager

    override fun inject() = (activity?.application as BookidApplication).appComponent.inject(this)

    override fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
    }

    override fun initializeCustom() {
        authenticateUser()
        (activity as DrawerManager).lockDrawer()
    }

    private fun authenticateUser() {
        val navController = findNavController()
        userManager.user.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                navController.navigate(SplashFragmentDirections.actionSplashFragmentToLoginGraph())
            } else {
                userManager.signInUser(it)
                navController.navigate(SplashFragmentDirections.actionSplashFragmentToAppGraph())
            }
        })
    }

}