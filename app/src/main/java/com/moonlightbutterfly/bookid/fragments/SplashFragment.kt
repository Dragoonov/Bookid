package com.moonlightbutterfly.bookid.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.moonlightbutterfly.bookid.Manager
import com.moonlightbutterfly.bookid.UserManager
import com.moonlightbutterfly.bookid.databinding.FragmentSplashBinding
import com.moonlightbutterfly.bookid.getNavController
import kotlinx.android.synthetic.main.fragment_splash.*
import javax.inject.Inject

class SplashFragment : BaseFragment<FragmentSplashBinding, ViewModel>() {

    @Inject
    lateinit var userManager: Manager

    override fun inject() = appComponent.inject(this)

    override fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
    }

    override fun initializeCustom(savedInstanceState: Bundle?) {
        authenticateUser()
        context?.getNavController()?.lockBottomNav()
    }

    private fun authenticateUser() {
        val navController = findNavController()
        val userId = requireContext()
            .getSharedPreferences(UserManager.FILE_KEY, Context.MODE_PRIVATE)
            .getString(UserManager.ID_KEY, "")
        userManager.receiveUserId(userId!!)
        userManager.user.observe(viewLifecycleOwner, Observer {
            if (it.id.isEmpty()) {
                val extras = FragmentNavigatorExtras(logo to "logo")
                navController.navigate(SplashFragmentDirections.actionSplashFragmentToLoginGraph(), extras)
            }
        })
    }

}