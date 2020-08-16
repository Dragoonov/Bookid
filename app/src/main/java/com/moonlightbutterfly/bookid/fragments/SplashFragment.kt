package com.moonlightbutterfly.bookid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.moonlightbutterfly.bookid.MainActivity
import com.moonlightbutterfly.bookid.Manager
import com.moonlightbutterfly.bookid.databinding.FragmentSplashBinding
import kotlinx.android.synthetic.main.fragment_splash.*
import javax.inject.Inject

class SplashFragment : BaseFragment<FragmentSplashBinding,ViewModel>() {

    @Inject
    lateinit var userManager: Manager

    override fun inject() = appComponent.inject(this)

    override fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
    }

    override fun initializeCustom(savedInstanceState: Bundle?) {
        authenticateUser()
    }

    private fun authenticateUser() {
        val navController = findNavController()
        userManager.user.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                val extras = FragmentNavigatorExtras(logo to "logo")
                navController.navigate(SplashFragmentDirections.actionSplashFragmentToLoginGraph(), extras)
            } else {
                userManager.signInUser(it)
                (activity as MainActivity).unlockBottomNav()
                navController.navigate(SplashFragmentDirections.actionSplashFragmentToAppGraph())
            }
        })
    }

}