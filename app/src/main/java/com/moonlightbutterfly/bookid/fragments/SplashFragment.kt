package com.moonlightbutterfly.bookid.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.moonlightbutterfly.bookid.BookidApplication
import com.moonlightbutterfly.bookid.R
import com.moonlightbutterfly.bookid.UserManager
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
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    private fun authenticateUser() {
        if (userManager.loggedUser == null) {
            userManager.getUserFromDatabase().observe(viewLifecycleOwner, Observer {
                if (it == null) {
                    findNavController().navigate(R.id.action_global_loginFragment)
                } else {
                    userManager.loggedUser = it
                    findNavController().navigate(R.id.action_global_searchFragment)
                }
            })
        } else {
            findNavController().navigate(R.id.action_global_searchFragment)
        }
    }
}